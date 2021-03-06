package handlers.admincommandhandlers;

import java.util.List;
import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;
 
/**
 * Projeto PkElfo
 */

public class AdminFortSiege implements IAdminCommandHandler
{
	// private static Logger _log = Logger.getLogger(AdminFortSiege.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_fortsiege",
		"admin_add_fortattacker",
		"admin_list_fortsiege_clans",
		"admin_clear_fortsiege_list",
		"admin_spawn_fortdoors",
		"admin_endfortsiege",
		"admin_startfortsiege",
		"admin_setfort",
		"admin_removefort"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command, " ");
		command = st.nextToken(); // Get actual command
		
		// Get fort
		Fort fort = null;
		int fortId = 0;
		if (st.hasMoreTokens())
		{
			fortId = Integer.parseInt(st.nextToken());
			fort = FortManager.getInstance().getFortById(fortId);
		}
		// Get fort
		if (((fort == null) || (fortId == 0)))
		{
			// No fort specified
			showFortSelectPage(activeChar);
		}
		else
		{
			L2Object target = activeChar.getTarget();
			L2PcInstance player = null;
			if (target instanceof L2PcInstance)
			{
				player = (L2PcInstance) target;
			}
			
			if (command.equalsIgnoreCase("admin_add_fortattacker"))
			{
				if (player == null)
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
				}
				else
				{
					if (fort.getSiege().checkIfCanRegister(player))
					{
						fort.getSiege().registerAttacker(player, true);
					}
				}
			}
			else if (command.equalsIgnoreCase("admin_clear_fortsiege_list"))
			{
				fort.getSiege().clearSiegeClan();
			}
			else if (command.equalsIgnoreCase("admin_endfortsiege"))
			{
				fort.getSiege().endSiege();
			}
			else if (command.equalsIgnoreCase("admin_list_fortsiege_clans"))
			{
				activeChar.sendMessage("Not implemented yet.");
			}
			else if (command.equalsIgnoreCase("admin_setfort"))
			{
				if ((player == null) || (player.getClan() == null))
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
				}
				else
				{
					fort.setOwner(player.getClan(), false);
				}
			}
			else if (command.equalsIgnoreCase("admin_removefort"))
			{
				L2Clan clan = fort.getOwnerClan();
				if (clan != null)
				{
					fort.removeOwner(true);
				}
				else
				{
					activeChar.sendMessage("Unable to remove fort");
				}
			}
			else if (command.equalsIgnoreCase("admin_spawn_fortdoors"))
			{
				fort.resetDoors();
			}
			else if (command.equalsIgnoreCase("admin_startfortsiege"))
			{
				fort.getSiege().startSiege();
			}
			
			showFortSiegePage(activeChar, fort);
		}
		return true;
	}
	
	private void showFortSelectPage(L2PcInstance activeChar)
	{
		int i = 0;
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/forts.htm");
		
		final List<Fort> forts = FortManager.getInstance().getForts();
		final StringBuilder cList = new StringBuilder(forts.size() * 100);
		
		for (Fort fort : forts)
		{
			if (fort != null)
			{
				StringUtil.append(cList, "<td fixwidth=90><a action=\"bypass -h admin_fortsiege ", String.valueOf(fort.getFortId()), "\">", fort.getName(), " id: ", String.valueOf(fort.getFortId()), "</a></td>");
				i++;
			}
			
			if (i > 2)
			{
				cList.append("</tr><tr>");
				i = 0;
			}
		}
		
		adminReply.replace("%forts%", cList.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private void showFortSiegePage(L2PcInstance activeChar, Fort fort)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/fort.htm");
		adminReply.replace("%fortName%", fort.getName());
		adminReply.replace("%fortId%", String.valueOf(fort.getFortId()));
		activeChar.sendPacket(adminReply);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
}
