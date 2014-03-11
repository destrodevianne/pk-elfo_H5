package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class AdminDeport implements IAdminCommandHandler
{
	private static String[] _adminCommands =
	{
		"admin_deport"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar.getTarget() instanceof L2PcInstance)
		{
			if (command.startsWith("admin_deport"))
			{
				((L2PcInstance) activeChar.getTarget()).teleToLocation(82698, 148638, -3473);
			}
		}
		return false;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
}