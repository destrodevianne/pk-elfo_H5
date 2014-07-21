package pk.elfo.gameserver.handler.custom;

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
				activeChar.sendMessage("Use: .banchat + nome + [minutos]");
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
						activeChar.sendMessage("Comprimento excedido !");
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
						activeChar.sendMessage("O jogador nao esta online !");
						return false;
					}
					if (player.getPunishLevel() != L2PcInstance.PunishLevel.NONE)
					{
						activeChar.sendMessage("O jogador ja esta punido !");
						return false;
					}
					if (player == activeChar)
					{
						activeChar.sendMessage("Voce nao pode proibir-se !");
						return false;
					}
					if (player.isGM())
					{
						activeChar.sendMessage("Voce nao pode proibir um GM !");
						return false;
					}
					if (AdminTable.getInstance().hasAccess(command, player.getAccessLevel()))
					{
						activeChar.sendMessage("Voce nao pode proibir um moderator !");
						return false;
					}
					
					player.setPunishLevel(L2PcInstance.PunishLevel.CHAT, length);
					player.sendMessage("Chat banido pelo moderator " + activeChar.getName());
					
					if (length > 0)
					{
						activeChar.sendMessage("O Jogador " + player.getName() + " teve o chat banido por " + length + " minutos.");
					}
					else
					{
						activeChar.sendMessage("O Jogador " + player.getName() + " teve o chat banido para sempre.");
					}
				}
				else
				{
					activeChar.sendMessage("Jogador nao encontrado !");
					return false;
				}
			}
		}
		else if (command.equals(VOICED_COMMANDS[1])) // unbanchat
		{
			if (params == null)
			{
				activeChar.sendMessage("Use: .unbanchat + nome");
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
						activeChar.sendMessage("O jogador nao esta online !");
						return false;
					}
					if (player.getPunishLevel() != L2PcInstance.PunishLevel.CHAT)
					{
						activeChar.sendMessage("O jogador nao esta com o chat banido !");
						return false;
					}
					
					player.setPunishLevel(L2PcInstance.PunishLevel.NONE, 0);
					
					activeChar.sendMessage("O jogador " + player.getName() + " teve o seu chat desbanido.");
					player.sendMessage("Chat desbanido pelo moderator " + activeChar.getName());
				}
				else
				{
					activeChar.sendMessage("Jogador nao encontrado !");
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