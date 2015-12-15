package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Projeto PkElfo
 */

public class AdminHellbound implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_hellbound_setlevel",
		"admin_hellbound"
	};
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		if (command.startsWith(ADMIN_COMMANDS[0])) // setlevel
		{
			try
			{
				StringTokenizer st = new StringTokenizer(command, " ");
				st.nextToken();
				final int level = Integer.parseInt(st.nextToken());
				if ((level < 0) || (level > 11))
				{
					throw new NumberFormatException();
				}
				HellboundManager.getInstance().setLevel(level);
				activeChar.sendMessage("Hellbound level set to " + level);
				return true;
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //hellbound_setlevel 0-11");
				return false;
			}
		}
		else if (command.startsWith(ADMIN_COMMANDS[1])) // Admin menu by Gladicek
		{
			showMenu(activeChar);
			return true;
		}
		return false;
	}
	
	private void showMenu(L2PcInstance activeChar)
	{
		NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(activeChar.getHtmlPrefix(), "data/html/admin/hellbound.htm");
		html.replace("%hbstage%", String.valueOf(HellboundManager.getInstance().getLevel()));
		html.replace("%trust%", String.valueOf(HellboundManager.getInstance().getTrust()));
		html.replace("%maxtrust%", String.valueOf(HellboundManager.getInstance().getMaxTrust()));
		html.replace("%mintrust%", String.valueOf(HellboundManager.getInstance().getMinTrust()));
		activeChar.sendPacket(html);
	}
}
