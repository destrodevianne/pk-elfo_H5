package handlers.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * This class handles following admin commands: - changelvl = change a character's access level Can be used for character ban (as opposed to regular //ban that affects accounts) or to grant mod/GM privileges ingame
 * Projeto PkElfo
 */
public class AdminChangeAccessLevel implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_changelvl"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		handleChangeLevel(command, activeChar);
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * If no character name is specified, tries to change GM's target access level. Else if a character name is provided, will try to reach it either from L2World or from a database connection.
	 * @param command
	 * @param activeChar
	 */
	private void handleChangeLevel(String command, L2PcInstance activeChar)
	{
		String[] parts = command.split(" ");
		if (parts.length == 2)
		{
			try
			{
				int lvl = Integer.parseInt(parts[1]);
				if (activeChar.getTarget() instanceof L2PcInstance)
				{
					onLineChange(activeChar, (L2PcInstance) activeChar.getTarget(), lvl);
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
				}
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use: //changelvl <target_new_level> | <player_name> <new_level>");
			}
		}
		else if (parts.length == 3)
		{
			String name = parts[1];
			int lvl = Integer.parseInt(parts[2]);
			L2PcInstance player = L2World.getInstance().getPlayer(name);
			if (player != null)
			{
				onLineChange(activeChar, player, lvl);
			}
			else
			{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement statement = con.prepareStatement("UPDATE characters SET accesslevel=? WHERE char_name=?");
					statement.setInt(1, lvl);
					statement.setString(2, name);
					statement.execute();
					int count = statement.getUpdateCount();
					statement.close();
					if (count == 0)
					{
						activeChar.sendMessage("Personagem nao encontrado ou nivel de acesso inalterado.");
					}
					else
					{
						activeChar.sendMessage("Nivel de acesso do personagem ja esta definido para " + lvl);
					}
				}
				catch (SQLException se)
				{
					activeChar.sendMessage("SQLException ao alterar o nivel de acesso do personagem");
					if (Config.DEBUG)
					{
						se.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * @param activeChar
	 * @param player
	 * @param lvl
	 */
	private void onLineChange(L2PcInstance activeChar, L2PcInstance player, int lvl)
	{
		if (lvl >= 0)
		{
			if (AdminTable.getInstance().hasAccessLevel(lvl))
			{
				player.setAccessLevel(lvl);
				player.sendMessage("Seu nivel de acesso foi alterado para " + lvl);
				activeChar.sendMessage("Nivel de acesso do personagem ja esta definido para " + lvl + ". Os Efeitos nao serao perceptivels ate a proxima sessao.");
			}
			else
			{
				activeChar.sendMessage("Voce esta tentando definir um nivel de acesso inexistente: " + lvl + " por favor, tente novamente com um valido!");
			}
		}
		else
		{
			player.setAccessLevel(lvl);
			player.sendMessage("Seu personagem foi banido. tchau.");
			player.logout();
		}
	}
}