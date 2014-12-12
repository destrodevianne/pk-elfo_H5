package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.LoginServerThread;
import pk.elfo.gameserver.communitybbs.Manager.RegionBBSManager;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.GMAudit;

/**
 * This class handles following admin commands: - ban_acc <account_name> = changes account access level to -1 and logs him off. If no account is specified target's account is used. - ban_char <char_name> = changes a characters access level to -1 and logs him off. If no character is specified target
 * is used. - ban_chat <char_name> <duration> = chat bans a character for the specified duration. If no name is specified the target is chat banned indefinitely. - unban_acc <account_name> = changes account access level to 0. - unban_char <char_name> = changes specified characters access level to 0.
 * - unban_chat <char_name> = lifts chat ban from specified player. If no player name is specified current target is used. - jail charname [penalty_time] = jails character. Time specified in minutes. For ever if no time is specified. - unjail charname = Unjails player, teleport him to Floran.
 * Projeto PkElfo
 */
public class AdminBan implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ban", // returns ban commands
		"admin_ban_acc",
		"admin_ban_char",
		"admin_ban_chat",
		"admin_unban", // returns unban commands
		"admin_unban_acc",
		"admin_unban_char",
		"admin_unban_chat",
		"admin_jail",
		"admin_unjail"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		String player = "";
		int duration = -1;
		L2PcInstance targetPlayer = null;
		
		if (st.hasMoreTokens())
		{
			player = st.nextToken();
			targetPlayer = L2World.getInstance().getPlayer(player);
			
			if (st.hasMoreTokens())
			{
				try
				{
					duration = Integer.parseInt(st.nextToken());
				}
				catch (NumberFormatException nfe)
				{
					activeChar.sendMessage("O formato do numero usado e invalido: " + nfe);
					return false;
				}
			}
		}
		else
		{
			if ((activeChar.getTarget() != null) && activeChar.getTarget().isPlayer())
			{
				targetPlayer = activeChar.getTarget().getActingPlayer();
			}
		}
		
		if ((targetPlayer != null) && targetPlayer.equals(activeChar))
		{
			activeChar.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
			return false;
		}
		
		if (command.startsWith("admin_ban ") || command.equalsIgnoreCase("admin_ban"))
		{
			activeChar.sendMessage("Comandos de ban disponiveis: //ban_acc, //ban_char, //ban_chat");
			return false;
		}
		else if (command.startsWith("admin_ban_acc"))
		{
			// May need to check usage in admin_ban_menu as well.
			
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //ban_acc <account_name>");
				return false;
			}
			else if (targetPlayer == null)
			{
				LoginServerThread.getInstance().sendAccessLevel(player, -1);
				activeChar.sendMessage("Pedido de ban enviado para a conta " + player);
				auditAction(command, activeChar, player);
			}
			else
			{
				targetPlayer.setPunishLevel(L2PcInstance.PunishLevel.ACC, 0);
				Announcements.getInstance().announceToAll("A conta de " + targetPlayer.getAccountName() + " foi Banida.");
				auditAction(command, activeChar, targetPlayer.getAccountName());
			}
		}
		else if (command.startsWith("admin_ban_char"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //ban_char <char_name>");
				return false;
			}
			auditAction(command, activeChar, (targetPlayer == null ? player : targetPlayer.getName()));
			return changeCharAccessLevel(targetPlayer, player, activeChar, -1);
		}
		else if (command.startsWith("admin_ban_chat"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //ban_chat <char_name> [penalty_minutes]");
				return false;
			}
			if (targetPlayer != null)
			{
				if (targetPlayer.getPunishLevel().value() > 0)
				{
					activeChar.sendMessage(targetPlayer.getName() + " ja esta preso ou banido.");
					return false;
				}
				String banLengthStr = "";
				
				targetPlayer.setPunishLevel(L2PcInstance.PunishLevel.CHAT, duration);
				if (duration > 0)
				{
					banLengthStr = " para " + duration + " minutos";
				}
				activeChar.sendMessage(targetPlayer.getName() + " agora esta proibido bate-papo" + banLengthStr + ".");
				auditAction(command, activeChar, targetPlayer.getName());
			}
			else
			{
				banChatOfflinePlayer(activeChar, player, duration, true);
				auditAction(command, activeChar, player);
			}
		}
		else if (command.startsWith("admin_unban_chat"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //unban_chat <char_name>");
				return false;
			}
			if (targetPlayer != null)
			{
				if (targetPlayer.isChatBanned())
				{
					targetPlayer.setPunishLevel(L2PcInstance.PunishLevel.NONE, 0);
					activeChar.sendMessage(targetPlayer.getName() + "'s proibicao de bate-papo foi agora ativada.");
					auditAction(command, activeChar, targetPlayer.getName());
				}
				else
				{
					activeChar.sendMessage(targetPlayer.getName() + " atualmente nao esta proibido conversar.");
				}
			}
			else
			{
				banChatOfflinePlayer(activeChar, player, 0, false);
				auditAction(command, activeChar, player);
			}
		}
		else if (command.startsWith("admin_unban ") || command.equalsIgnoreCase("admin_unban"))
		{
			activeChar.sendMessage("Comandos de desban disponiveis: //unban_acc, //unban_char, //unban_chat");
			return false;
		}
		else if (command.startsWith("admin_unban_acc"))
		{
			// Need to check admin_unban_menu command as well in AdminMenu.java handler.
			
			if (targetPlayer != null)
			{
				activeChar.sendMessage(targetPlayer.getName() + " atualmente esta online por isso nao deve ser banido.");
				return false;
			}
			else if (!player.isEmpty())
			{
				LoginServerThread.getInstance().sendAccessLevel(player, 0);
				activeChar.sendMessage("pedido de desban enviado para a conta " + player);
				auditAction(command, activeChar, player);
			}
			else
			{
				activeChar.sendMessage("Use: //unban_acc <account_name>");
				return false;
			}
		}
		else if (command.startsWith("admin_unban_char"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //unban_char <char_name>");
				return false;
			}
			else if (targetPlayer != null)
			{
				activeChar.sendMessage(targetPlayer.getName() + " atualmente esta online por isso nao deve ser banido.");
				return false;
			}
			else
			{
				auditAction(command, activeChar, player);
				return changeCharAccessLevel(null, player, activeChar, 0);
			}
		}
		else if (command.startsWith("admin_jail"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //jail <charname> [penalty_minutes] (Se nenhum nome for dado, alvo selecionado sera preso por tempo indeterminado)");
				return false;
			}
			if (targetPlayer != null)
			{
				if (targetPlayer.isFlyingMounted())
				{
					targetPlayer.untransform();
				}
				targetPlayer.setPunishLevel(L2PcInstance.PunishLevel.JAIL, duration);
				Announcements.getInstance().announceToAll("" + targetPlayer.getName() + " esta preso por " + (duration > 0 ? duration + " minutos." : "tempo indeterminado!"));
				auditAction(command, activeChar, targetPlayer.getName());
				}
			else
			{
				jailOfflinePlayer(activeChar, player, duration);
				auditAction(command, activeChar, player);
			}
		}
		else if (command.startsWith("admin_unjail"))
		{
			if ((targetPlayer == null) && player.isEmpty())
			{
				activeChar.sendMessage("Use: //unjail <charname> (If no name is given target is used)");
				return false;
			}
			else if (targetPlayer != null)
			{
				targetPlayer.setPunishLevel(L2PcInstance.PunishLevel.NONE, 0);
				activeChar.sendMessage("O Personagem " + targetPlayer.getName() + " foi removido da cadeia");
				auditAction(command, activeChar, targetPlayer.getName());
			}
			else
			{
				unjailOfflinePlayer(activeChar, player);
				auditAction(command, activeChar, player);
			}
		}
		return true;
	}
	
	private void auditAction(String fullCommand, L2PcInstance activeChar, String target)
	{
		if (!Config.GMAUDIT)
		{
			return;
		}
		
		String[] command = fullCommand.split(" ");
		GMAudit.auditGMAction(activeChar.getName() + " [" + activeChar.getObjectId() + "]", command[0], (target.isEmpty() ? "no-target" : target), (command.length > 2 ? command[2] : ""));
	}
	
	private void banChatOfflinePlayer(L2PcInstance activeChar, String name, int delay, boolean ban)
	{
		int level = 0;
		long value = 0;
		if (ban)
		{
			level = L2PcInstance.PunishLevel.CHAT.value();
			value = (delay > 0 ? delay * 60000L : 60000);
		}
		else
		{
			level = L2PcInstance.PunishLevel.NONE.value();
			value = 0;
		}
		
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET punish_level=?, punish_timer=? WHERE char_name=?");
			statement.setInt(1, level);
			statement.setLong(2, value);
			statement.setString(3, name);
			
			statement.execute();
			int count = statement.getUpdateCount();
			statement.close();
			
			if (count == 0)
			{
				activeChar.sendMessage("Jogador nao encontrado!");
			}
			else if (ban)
			{
				activeChar.sendMessage("Jogador " + name + " chat-banido por " + (delay > 0 ? delay + " minutos." : "sempre!"));
			}
			else
			{
				activeChar.sendMessage("Jogador " + name + "'s chat-banido ativado");
			}
		}
		catch (SQLException se)
		{
			activeChar.sendMessage("SQLException enquanto o chat estiver proibido ao jogador");
			if (Config.DEBUG)
			{
				se.printStackTrace();
			}
		}
	}
	
	private void jailOfflinePlayer(L2PcInstance activeChar, String name, int delay)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE char_name=?");
			statement.setInt(1, -114356);
			statement.setInt(2, -249645);
			statement.setInt(3, -2984);
			statement.setInt(4, L2PcInstance.PunishLevel.JAIL.value());
			statement.setLong(5, (delay > 0 ? delay * 60000L : 0));
			statement.setString(6, name);
			statement.execute();
			int count = statement.getUpdateCount();
			statement.close();
			
			if (count == 0)
			{
				activeChar.sendMessage("Jogador nao encontrado!");
			}
			else
			{
				activeChar.sendMessage("Jogador " + name + " preso por " + (delay > 0 ? delay + " minutos." : "sempre!"));
			}
		}
		catch (SQLException se)
		{
			activeChar.sendMessage("SQLException enquanto o jogador estiver na prisao");
			if (Config.DEBUG)
			{
				se.printStackTrace();
			}
		}
	}
	
	private void unjailOfflinePlayer(L2PcInstance activeChar, String name)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=?, punish_level=?, punish_timer=? WHERE char_name=?");
			statement.setInt(1, 17836);
			statement.setInt(2, 170178);
			statement.setInt(3, -3507);
			statement.setInt(4, 0);
			statement.setLong(5, 0);
			statement.setString(6, name);
			statement.execute();
			int count = statement.getUpdateCount();
			statement.close();
			if (count == 0)
			{
				activeChar.sendMessage("Jogador nao encontrado!");
			}
			else
			{
				activeChar.sendMessage("Jogador " + name + " removido da cadeia");
			}
		}
		catch (SQLException se)
		{
			activeChar.sendMessage("SQLException enquanto o jogador estiver na prisao");
			if (Config.DEBUG)
			{
				se.printStackTrace();
			}
		}
	}
	
	private boolean changeCharAccessLevel(L2PcInstance targetPlayer, String player, L2PcInstance activeChar, int lvl)
	{
		if (targetPlayer != null)
		{
			targetPlayer.setAccessLevel(lvl);
			targetPlayer.sendMessage("Seu personagem foi banido. adeus.");
			targetPlayer.logout();
			RegionBBSManager.getInstance().changeCommunityBoard();
			activeChar.sendMessage("O Jogador " + targetPlayer.getName() + " ja foi banido.");
			}
		else
		{
			try (Connection con = L2DatabaseFactory.getInstance().getConnection())
			{
				PreparedStatement statement = con.prepareStatement("UPDATE characters SET accesslevel=? WHERE char_name=?");
				statement.setInt(1, lvl);
				statement.setString(2, player);
				statement.execute();
				int count = statement.getUpdateCount();
				statement.close();
				if (count == 0)
				{
					activeChar.sendMessage("Personagem nao encontrado ou nivel de acesso inalterado.");
					return false;
				}
				activeChar.sendMessage(player + " agora tem um nivel de acesso de " + lvl);
			}
			catch (SQLException se)
			{
				activeChar.sendMessage("SQLException ao alterar o nivel de acesso do personagem");
				if (Config.DEBUG)
				{
					se.printStackTrace();
				}
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}