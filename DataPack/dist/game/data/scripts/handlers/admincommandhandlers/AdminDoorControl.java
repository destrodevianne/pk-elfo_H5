package handlers.admincommandhandlers;

import pk.elfo.gameserver.datatables.DoorTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
 
/**
 * Projeto PkElfo
 */

public class AdminDoorControl implements IAdminCommandHandler
{
	private static DoorTable _doorTable = DoorTable.getInstance();
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_open",
		"admin_close",
		"admin_openall",
		"admin_closeall"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		try
		{
			if (command.startsWith("admin_open "))
			{
				int doorId = Integer.parseInt(command.substring(11));
				if (_doorTable.getDoor(doorId) != null)
				{
					_doorTable.getDoor(doorId).openMe();
				}
				else
				{
					for (Castle castle : CastleManager.getInstance().getCastles())
					{
						if (castle.getDoor(doorId) != null)
						{
							castle.getDoor(doorId).openMe();
						}
					}
				}
			}
			else if (command.startsWith("admin_close "))
			{
				int doorId = Integer.parseInt(command.substring(12));
				if (_doorTable.getDoor(doorId) != null)
				{
					_doorTable.getDoor(doorId).closeMe();
				}
				else
				{
					for (Castle castle : CastleManager.getInstance().getCastles())
					{
						if (castle.getDoor(doorId) != null)
						{
							castle.getDoor(doorId).closeMe();
						}
					}
				}
			}
			if (command.equals("admin_closeall"))
			{
				for (L2DoorInstance door : _doorTable.getDoors())
				{
					door.closeMe();
				}
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					for (L2DoorInstance door : castle.getDoors())
					{
						door.closeMe();
					}
				}
			}
			if (command.equals("admin_openall"))
			{
				for (L2DoorInstance door : _doorTable.getDoors())
				{
					door.openMe();
				}
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					for (L2DoorInstance door : castle.getDoors())
					{
						door.openMe();
					}
				}
			}
			if (command.equals("admin_open"))
			{
				L2Object target = activeChar.getTarget();
				if (target instanceof L2DoorInstance)
				{
					((L2DoorInstance) target).openMe();
				}
				else
				{
					activeChar.sendMessage("Incorrect target.");
				}
			}
			
			if (command.equals("admin_close"))
			{
				L2Object target = activeChar.getTarget();
				if (target instanceof L2DoorInstance)
				{
					((L2DoorInstance) target).closeMe();
				}
				else
				{
					activeChar.sendMessage("Incorrect target.");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
