package handlers.admincommandhandlers;

import java.util.List;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.taskmanager.AutoAnnounceTaskManager;
import pk.elfo.gameserver.taskmanager.AutoAnnounceTaskManager.AutoAnnouncement;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.StringUtil;
import javolution.text.TextBuilder;

/**
 * This class handles following admin commands: - announce text = announces text to all players - list_announcements = show menu - reload_announcements = reloads announcements from txt file - announce_announcements = announce all stored announcements to all players - add_announcement text = adds
 * text to startup announcements - del_announcement id = deletes announcement with respective id
 * PkElfo
 */
public class AdminAnnouncements implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_list_announcements",
		"admin_list_critannouncements",
		"admin_reload_announcements",
		"admin_announce_announcements",
		"admin_add_announcement",
		"admin_del_announcement",
		"admin_add_critannouncement",
		"admin_del_critannouncement",
		"admin_announce",
		"admin_critannounce",
		"admin_announce_menu",
		"admin_critannounce_menu",
		"admin_list_autoann",
		"admin_reload_autoann",
		"admin_add_autoann",
		"admin_del_autoann"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_list_announcements"))
		{
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.equals("admin_list_critannouncements"))
		{
			Announcements.getInstance().listCritAnnouncements(activeChar);
		}
		else if (command.equals("admin_reload_announcements"))
		{
			Announcements.getInstance().loadAnnouncements();
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.startsWith("admin_announce_menu"))
		{
			if (Config.GM_ANNOUNCER_NAME && (command.length() > 20))
			{
				command += " (" + activeChar.getName() + ")";
			}
			Announcements.getInstance().handleAnnounce(command, 20, false);
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.startsWith("admin_critannounce_menu"))
		{
			try
			{
				command = command.substring(24);
				
				if (Config.GM_CRITANNOUNCER_NAME && (command.length() > 0))
				{
					command = activeChar.getName() + ": " + command;
				}
				Announcements.getInstance().handleAnnounce(command, 0, true);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
			
			AdminHelpPage.showHelpPage(activeChar, "gm_menu.htm");
		}
		else if (command.equals("admin_announce_announcements"))
		{
			for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
			{
				Announcements.getInstance().showAnnouncements(player);
			}
			Announcements.getInstance().listAnnouncements(activeChar);
		}
		else if (command.startsWith("admin_add_announcement"))
		{
			// FIXME the player can send only 16 chars (if you try to send more
			// it sends null), remove this function or not?
			if (!command.equals("admin_add_announcement"))
			{
				try
				{
					String val = command.substring(23);
					Announcements.getInstance().addAnnouncement(val);
					Announcements.getInstance().listAnnouncements(activeChar);
				}
				catch (StringIndexOutOfBoundsException e)
				{
				}// ignore errors
			}
		}
		else if (command.startsWith("admin_add_critannouncement"))
		{
			// FIXME the player can send only 16 chars (if you try to send more
			// it sends null), remove this function or not?
			if (!command.equals("admin_add_critannouncement"))
			{
				try
				{
					String val = command.substring(27);
					Announcements.getInstance().addCritAnnouncement(val);
					Announcements.getInstance().listCritAnnouncements(activeChar);
				}
				catch (StringIndexOutOfBoundsException e)
				{
				}// ignore errors
			}
		}
		else if (command.startsWith("admin_del_announcement"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(23));
				Announcements.getInstance().delAnnouncement(val);
				Announcements.getInstance().listAnnouncements(activeChar);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (command.startsWith("admin_del_critannouncement"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(27));
				Announcements.getInstance().delCritAnnouncement(val);
				Announcements.getInstance().listCritAnnouncements(activeChar);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		
		// Command is admin announce
		else if (command.startsWith("admin_announce"))
		{
			if (Config.GM_ANNOUNCER_NAME && (command.length() > 15))
			{
				command += " (" + activeChar.getName() + ")";
			}
			// Call method from another class
			Announcements.getInstance().handleAnnounce(command, 15, false);
		}
		else if (command.startsWith("admin_critannounce"))
		{
			try
			{
				command = command.substring(19);
				
				if (Config.GM_CRITANNOUNCER_NAME && (command.length() > 0))
				{
					command = activeChar.getName() + ": " + command;
				}
				Announcements.getInstance().handleAnnounce(command, 0, true);
			}
			catch (StringIndexOutOfBoundsException e)
			{
			}
		}
		else if (command.startsWith("admin_list_autoann"))
		{
			listAutoAnnouncements(activeChar);
		}
		else if (command.startsWith("admin_reload_autoann"))
		{
			AutoAnnounceTaskManager.getInstance().restore();
			activeChar.sendMessage("AutoAnnouncement Reloaded.");
			listAutoAnnouncements(activeChar);
		}
		else if (command.startsWith("admin_add_autoann"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para a adicao de auto anuncioamento!");
				return false;
			}
			String token = st.nextToken();
			if (!Util.isDigit(token))
			{
				activeChar.sendMessage("Nao e um valor inicial valido!");
				return false;
			}
			long initial = Long.parseLong(token);
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para a adicao de auto anuncioamento!");
				return false;
			}
			token = st.nextToken();
			if (!Util.isDigit(token))
			{
				activeChar.sendMessage("Nao e um valor de atraso valido!");
				return false;
			}
			long delay = Long.parseLong(token);
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para a adicao de auto anuncioamento!");
				return false;
			}
			token = st.nextToken();
			if (!token.equals("-1") && !Util.isDigit(token))
			{
				activeChar.sendMessage("Nao e um valor de repeticao valido!");
				return false;
			}
			int repeat = Integer.parseInt(token);
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para a adicao de auto anuncioamento!");
				return false;
			}
			boolean isCritical = Boolean.valueOf(st.nextToken());
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para a adicao de auto anuncioamento!");
				return false;
			}
			TextBuilder memo = new TextBuilder();
			while (st.hasMoreTokens())
			{
				memo.append(st.nextToken());
				memo.append(" ");
			}
			
			AutoAnnounceTaskManager.getInstance().addAutoAnnounce(initial * 1000, delay * 1000, repeat, memo.toString().trim(), isCritical);
			listAutoAnnouncements(activeChar);
		}
		
		else if (command.startsWith("admin_del_autoann"))
		{
			StringTokenizer st = new StringTokenizer(command);
			st.nextToken();
			
			if (!st.hasMoreTokens())
			{
				activeChar.sendMessage("Os Parametros nao sao suficientes para deletar o auto anuncioamento!");
				return false;
			}
			String token = st.nextToken();
			if (!Util.isDigit(token))
			{
				activeChar.sendMessage("Nao e um auto anuncio valido o valor do Id!");
				return false;
			}
			AutoAnnounceTaskManager.getInstance().deleteAutoAnnounce(Integer.parseInt(token));
			listAutoAnnouncements(activeChar);
		}
		return true;
	}
	
	private void listAutoAnnouncements(L2PcInstance activeChar)
	{
		String content = HtmCache.getInstance().getHtmForce(activeChar.getHtmlPrefix(), "data/html/admin/autoannounce.htm");
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setHtml(content);
		
		final StringBuilder replyMSG = StringUtil.startAppend(500, "<br>");
		List<AutoAnnouncement> autoannouncements = AutoAnnounceTaskManager.getInstance().getAutoAnnouncements();
		for (int i = 0; i < autoannouncements.size(); i++)
		{
			AutoAnnouncement autoann = autoannouncements.get(i);
			TextBuilder memo2 = new TextBuilder();
			for (String memo0 : autoann.getMemo())
			{
				memo2.append(memo0);
				memo2.append("/n");
			}
			replyMSG.append("<table width=260><tr><td width=220><font color=\"" + (autoann.isCritical() ? "00FCFC" : "7FFCFC") + "\">");
			replyMSG.append(memo2.toString().trim());
			replyMSG.append("</font></td><td width=40><button value=\"Delete\" action=\"bypass -h admin_del_autoann ");
			replyMSG.append(i);
			replyMSG.append("\" width=60 height=15 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>");
		}
		adminReply.replace("%announces%", replyMSG.toString());
		
		activeChar.sendPacket(adminReply);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}