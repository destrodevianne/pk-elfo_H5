package handlers.voicedcommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */
public class ChatAdmin implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"banchat",
		"unbanchat"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (!AdminTable.getInstance().hasAccess(command, activeChar.getAccessLevel()))
		{
			return false;
		}
		
		if (command.equals(VOICED_COMMANDS[0])) // banchat
		{
			if (params == null)
			{
				activeChar.sendMessage("Usage: .banchat name [minutes]");
				return true;
			}
			StringTokenizer st = new StringTokenizer(params);
			if (st.hasMoreTokens())
			{
				String name = st.nextToken();
				int length = 0;
				if (st.hasMoreTokens())
				{
					try
					{
						length = Integer.parseInt(st.nextToken());
					}
					catch (NumberFormatException e)
					{
						activeChar.sendMessage("Wrong ban length !");
						return false;
					}
				}
				if (length < 0)
				{
					length = 0;
				}
				
				int objId = CharNameTable.getInstance().getIdByName(name);
				if (objId > 0)
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objId);
					if ((player == null) || !player.isOnline())
					{
						activeChar.sendMessage("Player not online !");
						return false;
					}
					if (player.getPunishLevel() != L2PcInstance.PunishLevel.NONE)
					{
						activeChar.sendMessage("Player is already punished !");
						return false;
					}
					if (player == activeChar)
					{
						activeChar.sendMessage("You can't ban yourself !");
						return false;
					}
					if (player.isGM())
					{
						activeChar.sendMessage("You can't ban GM !");
						return false;
					}
					if (AdminTable.getInstance().hasAccess(command, player.getAccessLevel()))
					{
						activeChar.sendMessage("You can't ban moderator !");
						return false;
					}
					
					player.setPunishLevel(L2PcInstance.PunishLevel.CHAT, length);
					player.sendMessage("Chat banned by moderator " + activeChar.getName());
					
					if (length > 0)
					{
						activeChar.sendMessage("Player " + player.getName() + " chat banned for " + length + " minutes.");
					}
					else
					{
						activeChar.sendMessage("Player " + player.getName() + " chat banned forever.");
					}
				}
				else
				{
					activeChar.sendMessage("Player not found !");
					return false;
				}
			}
		}
		else if (command.equals(VOICED_COMMANDS[1])) // unbanchat
		{
			if (params == null)
			{
				activeChar.sendMessage("Usage: .unbanchat name");
				return true;
			}
			StringTokenizer st = new StringTokenizer(params);
			if (st.hasMoreTokens())
			{
				String name = st.nextToken();
				
				int objId = CharNameTable.getInstance().getIdByName(name);
				if (objId > 0)
				{
					L2PcInstance player = L2World.getInstance().getPlayer(objId);
					if ((player == null) || !player.isOnline())
					{
						activeChar.sendMessage("Player not online !");
						return false;
					}
					if (player.getPunishLevel() != L2PcInstance.PunishLevel.CHAT)
					{
						activeChar.sendMessage("Player is not chat banned !");
						return false;
					}
					
					player.setPunishLevel(L2PcInstance.PunishLevel.NONE, 0);
					
					activeChar.sendMessage("Player " + player.getName() + " chat unbanned.");
					player.sendMessage("Chat unbanned by moderator " + activeChar.getName());
				}
				else
				{
					activeChar.sendMessage("Player not found !");
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
