package handlers.admincommandhandlers;

import java.util.Collection;
import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.CursedWeaponsManager;
import pk.elfo.gameserver.model.CursedWeapon;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;
 
/**
 * Projeto PkElfo
 */

public class AdminCursedWeapons implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_cw_info",
		"admin_cw_remove",
		"admin_cw_goto",
		"admin_cw_reload",
		"admin_cw_add",
		"admin_cw_info_menu"
	};
	
	private int itemId;
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		
		CursedWeaponsManager cwm = CursedWeaponsManager.getInstance();
		int id = 0;
		
		StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (command.startsWith("admin_cw_info"))
		{
			if (!command.contains("menu"))
			{
				activeChar.sendMessage("====== Cursed Weapons: ======");
				for (CursedWeapon cw : cwm.getCursedWeapons())
				{
					activeChar.sendMessage("> " + cw.getName() + " (" + cw.getItemId() + ")");
					if (cw.isActivated())
					{
						L2PcInstance pl = cw.getPlayer();
						activeChar.sendMessage("  Player holding: " + (pl == null ? "null" : pl.getName()));
						activeChar.sendMessage("    Player karma: " + cw.getPlayerKarma());
						activeChar.sendMessage("    Time Remaining: " + (cw.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("    Kills : " + cw.getNbKills());
					}
					else if (cw.isDropped())
					{
						activeChar.sendMessage("  Lying on the ground.");
						activeChar.sendMessage("    Time Remaining: " + (cw.getTimeLeft() / 60000) + " min.");
						activeChar.sendMessage("    Kills : " + cw.getNbKills());
					}
					else
					{
						activeChar.sendMessage("  Don't exist in the world.");
					}
					activeChar.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
				}
			}
			else
			{
				final Collection<CursedWeapon> cws = cwm.getCursedWeapons();
				final StringBuilder replyMSG = new StringBuilder(cws.size() * 300);
				NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
				adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/cwinfo.htm");
				for (CursedWeapon cw : cwm.getCursedWeapons())
				{
					itemId = cw.getItemId();
					
					StringUtil.append(replyMSG, "<table width=270><tr><td>Name:</td><td>", cw.getName(), "</td></tr>");
					
					if (cw.isActivated())
					{
						L2PcInstance pl = cw.getPlayer();
						StringUtil.append(replyMSG, "<tr><td>Weilder:</td><td>", (pl == null ? "null" : pl.getName()), "</td></tr>" + "<tr><td>Karma:</td><td>", String.valueOf(cw.getPlayerKarma()), "</td></tr>" + "<tr><td>Kills:</td><td>", String.valueOf(cw.getPlayerPkKills()), "/", String.valueOf(cw.getNbKills()), "</td></tr>" + "<tr><td>Time remaining:</td><td>", String.valueOf(cw.getTimeLeft() / 60000), " min.</td></tr>" + "<tr><td><button value=\"Remove\" action=\"bypass -h admin_cw_remove ", String.valueOf(itemId), "\" width=73 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td><button value=\"Go\" action=\"bypass -h admin_cw_goto ", String.valueOf(itemId), "\" width=73 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
					}
					else if (cw.isDropped())
					{
						StringUtil.append(replyMSG, "<tr><td>Position:</td><td>Lying on the ground</td></tr>" + "<tr><td>Time remaining:</td><td>", String.valueOf(cw.getTimeLeft() / 60000), " min.</td></tr>" + "<tr><td>Kills:</td><td>", String.valueOf(cw.getNbKills()), "</td></tr>" + "<tr><td><button value=\"Remove\" action=\"bypass -h admin_cw_remove ", String.valueOf(itemId), "\" width=73 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td><button value=\"Go\" action=\"bypass -h admin_cw_goto ", String.valueOf(itemId), "\" width=73 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>");
					}
					else
					{
						StringUtil.append(replyMSG, "<tr><td>Position:</td><td>Doesn't exist.</td></tr>" + "<tr><td><button value=\"Give to Target\" action=\"bypass -h admin_cw_add ", String.valueOf(itemId), "\" width=130 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td></td></tr>");
					}
					
					replyMSG.append("</table><br>");
				}
				adminReply.replace("%cwinfo%", replyMSG.toString());
				activeChar.sendPacket(adminReply);
			}
		}
		else if (command.startsWith("admin_cw_reload"))
		{
			cwm.reload();
		}
		else
		{
			CursedWeapon cw = null;
			try
			{
				String parameter = st.nextToken();
				if (parameter.matches("[0-9]*"))
				{
					id = Integer.parseInt(parameter);
				}
				else
				{
					parameter = parameter.replace('_', ' ');
					for (CursedWeapon cwp : cwm.getCursedWeapons())
					{
						if (cwp.getName().toLowerCase().contains(parameter.toLowerCase()))
						{
							id = cwp.getItemId();
							break;
						}
					}
				}
				cw = cwm.getCursedWeapon(id);
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Usage: //cw_remove|//cw_goto|//cw_add <itemid|name>");
			}
			
			if (cw == null)
			{
				activeChar.sendMessage("Unknown cursed weapon ID.");
				return false;
			}
			
			if (command.startsWith("admin_cw_remove "))
			{
				cw.endOfLife();
			}
			else if (command.startsWith("admin_cw_goto "))
			{
				cw.goTo(activeChar);
			}
			else if (command.startsWith("admin_cw_add"))
			{
				if (cw.isActive())
				{
					activeChar.sendMessage("This cursed weapon is already active.");
				}
				else
				{
					L2Object target = activeChar.getTarget();
					if (target instanceof L2PcInstance)
					{
						((L2PcInstance) target).addItem("AdminCursedWeaponAdd", id, 1, target, true);
					}
					else
					{
						activeChar.addItem("AdminCursedWeaponAdd", id, 1, activeChar, true);
					}
					cw.setEndTime(System.currentTimeMillis() + (cw.getDuration() * 60000L));
					cw.reActivate();
				}
			}
			else
			{
				activeChar.sendMessage("Unknown command.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}