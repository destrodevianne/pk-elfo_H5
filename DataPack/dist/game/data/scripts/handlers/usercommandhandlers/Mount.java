package handlers.usercommandhandlers;

import king.server.gameserver.handler.IUserCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
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
