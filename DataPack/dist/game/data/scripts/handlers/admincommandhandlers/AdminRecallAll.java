package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class AdminRecallAll implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_recallall"
	};
	
	private void teleportTo(L2PcInstance activeChar, int x, int y, int z)
	{
		activeChar.teleToLocation(x, y, z, false);
	}
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_recallall"))
		{
			for (L2PcInstance players : L2World.getInstance().getAllPlayers().valueCollection())
			{
				teleportTo(players, activeChar.getX(), activeChar.getY(), activeChar.getZ());
			}
		}
		return false;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}