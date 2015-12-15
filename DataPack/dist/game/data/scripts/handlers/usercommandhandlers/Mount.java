package handlers.usercommandhandlers;

import pk.elfo.gameserver.handler.IUserCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class Mount implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		61
	};
	
	@Override
	public synchronized boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		return activeChar.mountPlayer(activeChar.getSummon());
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}