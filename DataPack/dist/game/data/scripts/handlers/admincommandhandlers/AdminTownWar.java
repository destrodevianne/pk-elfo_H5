package handlers.admincommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.TownManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class AdminTownWar implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS = {
		"admin_townwar_start",
		"admin_townwar_end"
	};
	
	private static final int ALL_TOWNS_INT = 17;
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_townwar_start"))
		{
			startTownWar();
		}
		if (command.startsWith("admin_townwar_end"))
		{
			endTownWar();
		}
		return true;
	}
	
	private void startTownWar()
	{
		if (Config.TW_ALL_TOWNS)
		{
			for (int i = 1; i <= ALL_TOWNS_INT; i++)
			{
				TownManager.getTown(i).setIsTWZone(true);
				TownManager.getTown(i).updateForCharactersInside();
			}
			TownManager.getTown(20).setIsTWZone(true);
			TownManager.getTown(20).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: All towns are war zone.");
		}
		else
		{
			TownManager.getTown(Config.TW_TOWN_ID).setIsTWZone(true);
			TownManager.getTown(Config.TW_TOWN_ID).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: " + Config.TW_TOWN_NAME + " is a war zone.");
		}
	}
	
	private void endTownWar()
	{
		if (Config.TW_ALL_TOWNS)
		{
			for (int i = 1; i <= ALL_TOWNS_INT; i++)
			{
				TownManager.getTown(i).setIsTWZone(false);
				TownManager.getTown(i).updateForCharactersInside();
			}
			TownManager.getTown(20).setIsTWZone(false);
			TownManager.getTown(20).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: All towns are returned normal.");
		}
		else
		{
			TownManager.getTown(Config.TW_TOWN_ID).setIsTWZone(false);
			TownManager.getTown(Config.TW_TOWN_ID).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: " + Config.TW_TOWN_NAME + " is returned normal.");
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}