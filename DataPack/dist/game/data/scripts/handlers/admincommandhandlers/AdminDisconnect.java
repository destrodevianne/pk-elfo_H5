package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands: - character_disconnect = disconnects target player
 * Projeto PkElfo
 */
public class AdminDisconnect implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_character_disconnect"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_character_disconnect"))
		{
			disconnectCharacter(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void disconnectCharacter(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			return;
		}
		
		if (player == activeChar)
		{
			activeChar.sendMessage("Voce nao pode deslogar seu proprio personagem.");
		}
		else
		{
			activeChar.sendMessage("O jogador " + player.getName() + " foi desconectado do servidor.");
			
			player.logout();
		}
	}
}