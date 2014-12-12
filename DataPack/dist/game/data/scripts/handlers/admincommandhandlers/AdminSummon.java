package handlers.admincommandhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.handler.AdminCommandHandler;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Projeto PkElfo
 */

public class AdminSummon implements IAdminCommandHandler
{
	private static final Logger _log = Logger.getLogger(AdminSummon.class.getName());
	
	public static final String[] ADMIN_COMMANDS =
	{
		"admin_summon"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		
		return ADMIN_COMMANDS;
	}
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		int id;
		int count = 1;
		String[] data = command.split(" ");
		try
		{
			id = Integer.parseInt(data[1]);
			if (data.length > 2)
			{
				count = Integer.parseInt(data[2]);
			}
		}
		catch (NumberFormatException nfe)
		{
			activeChar.sendMessage("Incorrect format for command 'summon'");
			return false;
		}
		
		String subCommand;
		if (id < 1000000)
		{
			subCommand = "admin_create_item";
			if (!AdminTable.getInstance().hasAccess(subCommand, activeChar.getAccessLevel()))
			{
				activeChar.sendMessage("You don't have the access right to use this command!");
				_log.warning("Character " + activeChar.getName() + " tryed to use admin command " + subCommand + ", but have no access to it!");
				return false;
			}
			IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(subCommand);
			ach.useAdminCommand(subCommand + " " + id + " " + count, activeChar);
		}
		else
		{
			subCommand = "admin_spawn_once";
			if (!AdminTable.getInstance().hasAccess(subCommand, activeChar.getAccessLevel()))
			{
				activeChar.sendMessage("You don't have the access right to use this command!");
				_log.warning("Character " + activeChar.getName() + " tryed to use admin command " + subCommand + ", but have no access to it!");
				return false;
			}
			IAdminCommandHandler ach = AdminCommandHandler.getInstance().getHandler(subCommand);
			
			activeChar.sendMessage("This is only a temporary spawn.  The mob(s) will NOT respawn.");
			id -= 1000000;
			ach.useAdminCommand(subCommand + " " + id + " " + count, activeChar);
		}
		return true;
	}
}