package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.geoeditorcon.GeoEditorListener;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Projeto PkElfo
 */

public class AdminGeoEditor implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_ge_status",
		"admin_ge_mode",
		"admin_ge_join",
		"admin_ge_leave"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (!Config.ACCEPT_GEOEDITOR_CONN)
		{
			activeChar.sendMessage("Server do not accepts geoeditor connections now.");
			return true;
		}
		if (command.startsWith("admin_ge_status"))
		{
			activeChar.sendMessage(GeoEditorListener.getInstance().getStatus());
		}
		else if (command.startsWith("admin_ge_mode"))
		{
			if (GeoEditorListener.getInstance().getThread() == null)
			{
				activeChar.sendMessage("Geoeditor not connected.");
				return true;
			}
			try
			{
				String val = command.substring("admin_ge_mode".length());
				StringTokenizer st = new StringTokenizer(val);
				
				if (st.countTokens() < 1)
				{
					activeChar.sendMessage("Usage: //ge_mode X");
					activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
					activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
					activeChar.sendMessage("Mode 2: Send coordinates each second.");
					return true;
				}
				int m;
				m = Integer.parseInt(st.nextToken());
				GeoEditorListener.getInstance().getThread().setMode(m);
				activeChar.sendMessage("Geoeditor connection mode set to " + m + ".");
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //ge_mode X");
				activeChar.sendMessage("Mode 0: Don't send coordinates to geoeditor.");
				activeChar.sendMessage("Mode 1: Send coordinates at ValidatePosition from clients.");
				activeChar.sendMessage("Mode 2: Send coordinates each second.");
				e.printStackTrace();
			}
			return true;
		}
		else if (command.equals("admin_ge_join"))
		{
			if (GeoEditorListener.getInstance().getThread() == null)
			{
				activeChar.sendMessage("Geoeditor not connected.");
				return true;
			}
			GeoEditorListener.getInstance().getThread().addGM(activeChar);
			activeChar.sendMessage("You added to list for geoeditor.");
		}
		else if (command.equals("admin_ge_leave"))
		{
			if (GeoEditorListener.getInstance().getThread() == null)
			{
				activeChar.sendMessage("Geoeditor not connected.");
				return true;
			}
			GeoEditorListener.getInstance().getThread().removeGM(activeChar);
			activeChar.sendMessage("You removed from list for geoeditor.");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
