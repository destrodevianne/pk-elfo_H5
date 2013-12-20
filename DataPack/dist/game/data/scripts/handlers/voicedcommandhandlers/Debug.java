package handlers.voicedcommandhandlers;

import king.server.gameserver.datatables.AdminTable;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */
public class Debug implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"debug"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (!AdminTable.getInstance().hasAccess(command, activeChar.getAccessLevel()))
		{
			return false;
		}
		
		if (VOICED_COMMANDS[0].equalsIgnoreCase(command))
		{
			if (activeChar.isDebug())
			{
				activeChar.setDebug(null);
				activeChar.sendMessage("Debugging disabled.");
			}
			else
			{
				activeChar.setDebug(activeChar);
				activeChar.sendMessage("Debugging enabled.");
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