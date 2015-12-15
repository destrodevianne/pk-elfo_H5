package handlers.admincommandhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class AdminGm implements IAdminCommandHandler
{
	private static Logger _log = Logger.getLogger(AdminGm.class.getName());
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_gm"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_gm") && activeChar.isGM())
		{
			AdminTable.getInstance().deleteGm(activeChar);
			activeChar.setAccessLevel(0);
			activeChar.sendMessage("You no longer have GM status.");
			_log.info("GM: " + activeChar.getName() + "(" + activeChar.getObjectId() + ") turned his GM status off");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
