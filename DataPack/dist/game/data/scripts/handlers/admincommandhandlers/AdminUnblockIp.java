package handlers.admincommandhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class AdminUnblockIp implements IAdminCommandHandler
{
	private static final Logger _log = Logger.getLogger(AdminUnblockIp.class.getName());
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_unblockip"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_unblockip "))
		{
			try
			{
				String ipAddress = command.substring(16);
				if (unblockIp(ipAddress, activeChar))
				{
					activeChar.sendMessage("Removed IP " + ipAddress + " from blocklist!");
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Usage: //unblockip <ip>");
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private boolean unblockIp(String ipAddress, L2PcInstance activeChar)
	{
		// LoginServerThread.getInstance().unBlockip(ipAddress);
		_log.warning("IP removed by GM " + activeChar.getName());
		return true;
	}
}