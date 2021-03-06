package handlers.admincommandhandlers;

import java.util.List;
import java.util.StringTokenizer;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.CastleManorManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;
 
/**
 * Projeto PkElfo
 */

public class AdminManor implements IAdminCommandHandler
{
	private static final String[] _adminCommands =
	{
		"admin_manor",
		"admin_manor_approve",
		"admin_manor_setnext",
		"admin_manor_reset",
		"admin_manor_setmaintenance",
		"admin_manor_save",
		"admin_manor_disable"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		StringTokenizer st = new StringTokenizer(command);
		command = st.nextToken();
		
		if (command.equals("admin_manor"))
		{
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_setnext"))
		{
			CastleManorManager.getInstance().setNextPeriod();
			CastleManorManager.getInstance().setNewManorRefresh();
			CastleManorManager.getInstance().updateManorRefresh();
			activeChar.sendMessage("Manor System: set to next period");
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_approve"))
		{
			CastleManorManager.getInstance().approveNextPeriod();
			CastleManorManager.getInstance().setNewPeriodApprove();
			CastleManorManager.getInstance().updatePeriodApprove();
			activeChar.sendMessage("Manor System: next period approved");
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_reset"))
		{
			int castleId = 0;
			try
			{
				castleId = Integer.parseInt(st.nextToken());
			}
			catch (Exception e)
			{
			}
			
			if (castleId > 0)
			{
				Castle castle = CastleManager.getInstance().getCastleById(castleId);
				castle.resetManor();
				activeChar.sendMessage("Manor data for " + castle.getName() + " was nulled");
			}
			else
			{
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					castle.resetManor();
				}
				activeChar.sendMessage("Manor data was nulled");
			}
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_setmaintenance"))
		{
			boolean mode = CastleManorManager.getInstance().isUnderMaintenance();
			CastleManorManager.getInstance().setUnderMaintenance(!mode);
			if (mode)
			{
				activeChar.sendMessage("Manor System: not under maintenance");
			}
			else
			{
				activeChar.sendMessage("Manor System: under maintenance");
			}
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_save"))
		{
			CastleManorManager.getInstance().save();
			activeChar.sendMessage("Manor System: all data saved");
			showMainPage(activeChar);
		}
		else if (command.equals("admin_manor_disable"))
		{
			boolean mode = CastleManorManager.getInstance().isDisabled();
			CastleManorManager.getInstance().setDisabled(!mode);
			if (mode)
			{
				activeChar.sendMessage("Manor System: enabled");
			}
			else
			{
				activeChar.sendMessage("Manor System: disabled");
			}
			showMainPage(activeChar);
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return _adminCommands;
	}
	
	private String formatTime(long millis)
	{
		String s = "";
		int secs = (int) millis / 1000;
		int mins = secs / 60;
		secs -= mins * 60;
		int hours = mins / 60;
		mins -= hours * 60;
		
		if (hours > 0)
		{
			s += hours + ":";
		}
		s += mins + ":";
		s += secs;
		return s;
	}
	
	private void showMainPage(L2PcInstance activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		final List<Castle> castles = CastleManager.getInstance().getCastles();
		final StringBuilder replyMSG = StringUtil.startAppend(1000 + (castles.size() * 50), "<html><body>" + "<center><table width=270><tr>" + "<td width=45><button value=\"Main\" action=\"bypass -h admin_admin\" width=45 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "<td width=180><center>Manor Info</center></td>" + "<td width=45><button value=\"Back\" action=\"bypass -h admin_admin2\" width=45 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" + "</tr></table><font color=\"LEVEL\"> [Manor System] </font></center><br>" + "<table width=\"100%\"><tr><td>" + "Disabled: ", CastleManorManager.getInstance().isDisabled() ? "yes" : "no", "</td><td>" + "Under Maintenance: ", CastleManorManager.getInstance().isUnderMaintenance() ? "yes" : "no", "</td></tr><tr><td>" + "Time to refresh: ", formatTime(CastleManorManager.getInstance().getMillisToManorRefresh()), "</td><td>" + "Time to approve: ", formatTime(CastleManorManager.getInstance().getMillisToNextPeriodApprove()), "</td></tr>" + "</table>" + "<center><table><tr><td>" + "<button value=\"Set Next\" action=\"bypass -h admin_manor_setnext\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td>" + "<button value=\"Approve Next\" action=\"bypass -h admin_manor_approve\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><tr><td>" + "<button value=\"", CastleManorManager.getInstance().isUnderMaintenance() ? "Set normal" : "Set mainteance", "\" action=\"bypass -h admin_manor_setmaintenance\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td>" + "<button value=\"", CastleManorManager.getInstance().isDisabled() ? "Enable" : "Disable", "\" action=\"bypass -h admin_manor_disable\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><tr><td>" + "<button value=\"Refresh\" action=\"bypass -h admin_manor\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td>" + "<button value=\"Back\" action=\"bypass -h admin_admin\" width=110 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>" + "</table></center>" + "<br><center>Castle Information:<table width=\"100%\">" + "<tr><td></td><td>Current Period</td><td>Next Period</td></tr>");
		
		for (Castle c : CastleManager.getInstance().getCastles())
		{
			StringUtil.append(replyMSG, "<tr><td>", c.getName(), "</td>" + "<td>", String.valueOf(c.getManorCost(CastleManorManager.PERIOD_CURRENT)), "a</td>" + "<td>", String.valueOf(c.getManorCost(CastleManorManager.PERIOD_NEXT)), "a</td>" + "</tr>");
		}
		
		replyMSG.append("</table><br>" + "</body></html>");
		
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}
}
