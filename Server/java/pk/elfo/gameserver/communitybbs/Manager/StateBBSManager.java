/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. 
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://pk.elfo.ru/>.
 */
package pk.elfo.gameserver.communitybbs.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javolution.text.TextBuilder;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.GameServer;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */
public class StateBBSManager extends BaseBBSManager
{
	public class CBStatMan
	{
		public int PlayerId = 0;
		public String ChName = "";
		public int ChGameTime = 0;
		public int ChPk = 0;
		public int ChPvP = 0;
		public String ChClanName = "";
		public int ChClanLevel = 0;
		public int ChClanRep = 0;
		public String ChClanAlly = "";
		public int ChOnOff = 0;
		public int ChSex = 0;
		public int hCount = 0;
		public int hPlayed = 0;
		public int ChClass = 0;
	}
	
	public static StateBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance player)
	{
		if (command.equals("_bbsstat;"))
		{
			showPvp(player);
		}
		else if (command.startsWith("_bbsstat;pk"))
		{
			showPK(player);
		}
		else if (command.startsWith("_bbsstat;clan"))
		{
			showClan(player);
		}
		else if (command.startsWith("_bbsstat;hero"))
		{
			showHero(player);
		}
		else if (command.startsWith("_bbsstat;rates"))
		{
			showRates(player);
		}
		else if (command.startsWith("_bbsstat;features"))
		{
			showFeatures(player);
		}
		else if (command.startsWith("_bbsstat;events"))
		{
			showEvents(player);
		}
		else if (command.startsWith("_bbsstat;gameplay"))
		{
			showGameplay(player);
		}
		else
		{
			separateAndSend("<html><body><br><br><center>In bbsstat function: " + command + " is not implemented yet.</center><br><br></body></html>", player);
		}
	}
	
	private void showRates(L2PcInstance player)
	{
		{
			TextBuilder html = new TextBuilder();
			{
				
				html.append("");
			}
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/rates.htm");
			adminReply.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
			adminReply.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
			adminReply.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
			adminReply.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
			adminReply.replace("%pet_xp%", String.valueOf(Config.PET_XP_RATE));
			adminReply.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(57)));
			adminReply.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
			adminReply.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
			adminReply.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
			adminReply.replace("%rate_drop_quest%", String.valueOf(Config.RATE_QUEST_DROP));
			adminReply.replace("%Rate_Consumable_Cost%", String.valueOf(Config.RATE_CONSUMABLE_COST));
			adminReply.replace("%Karma_Rate_Drop%", String.valueOf(Config.KARMA_RATE_DROP));
			adminReply.replace("%safe_max_full%", String.valueOf(Config.ENCHANT_SAFE_MAX_FULL));
			adminReply.replace("%Enchant_Max%", String.valueOf(Config.MAX_ENCHANT_LEVEL));
			adminReply.replace("%Enchant_Chance%", String.valueOf(Config.ENCHANT_CHANCE));
			adminReply.replace("%Enchant_Element_Stone%", String.valueOf(Config.ENCHANT_CHANCE_ELEMENT_STONE));
			adminReply.replace("%Enchant_Element_Crystal%", String.valueOf(Config.ENCHANT_CHANCE_ELEMENT_CRYSTAL));
			adminReply.replace("%Buffs_max%", String.valueOf(Config.BUFFS_MAX_AMOUNT));
			adminReply.replace("%Dance_max%", String.valueOf(Config.DANCES_MAX_AMOUNT));
			adminReply.replace("%Buffs_max_tringer%", String.valueOf(Config.TRIGGERED_BUFFS_MAX_AMOUNT));
			separateAndSend(adminReply.getHtm(), player);
		}
		return;
	}
	
	private void showFeatures(L2PcInstance player)
	{
		{
			TextBuilder html = new TextBuilder();
			{
				
				html.append("");
			}
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/features.htm");
			adminReply.replace("%Banking%", String.valueOf(Config.BANKING_SYSTEM_ENABLED));
			adminReply.replace("%Change_Password%", String.valueOf(Config.L2JMOD_ALLOW_CHANGE_PASSWORD));
			adminReply.replace("%Lang_Enable%", String.valueOf(Config.L2JMOD_MULTILANG_ENABLE));
			adminReply.replace("%TvT_Allow_Voice%", String.valueOf(Config.TVT_ALLOW_VOICED_COMMAND));
			adminReply.replace("%Wedding_Allow%", String.valueOf(Config.L2JMOD_ALLOW_WEDDING));
			adminReply.replace("%Ally_max_clans%", String.valueOf(Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY));
			adminReply.replace("%Max_Subs%", String.valueOf(Config.MAX_SUBCLASS));
			adminReply.replace("%Max_Sub_Lv%", String.valueOf(Config.MAX_SUBCLASS_LEVEL));
			separateAndSend(adminReply.getHtm(), player);
		}
		return;
	}
	
	private void showEvents(L2PcInstance player)
	{
		{
			TextBuilder html = new TextBuilder();
			{
				
				html.append("");
			}
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/events.htm");
			adminReply.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
			adminReply.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
			adminReply.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
			adminReply.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
			adminReply.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(57)));
			adminReply.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
			adminReply.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
			adminReply.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
			adminReply.replace("%rate_drop_quest%", String.valueOf(Config.RATE_QUEST_DROP));
			adminReply.replace("%safe_max_full%", String.valueOf(Config.ENCHANT_SAFE_MAX_FULL));
			adminReply.replace("%Enchant_Max%", String.valueOf(Config.MAX_ENCHANT_LEVEL));
			adminReply.replace("%Enchant_Chance%", String.valueOf(Config.ENCHANT_CHANCE));
			adminReply.replace("%Buffs_max%", String.valueOf(Config.BUFFS_MAX_AMOUNT));
			adminReply.replace("%Dance_max%", String.valueOf(Config.DANCES_MAX_AMOUNT));
			adminReply.replace("%Buffs_max_tringer%", String.valueOf(Config.TRIGGERED_BUFFS_MAX_AMOUNT));
			adminReply.replace("%Ally_max_clans%", String.valueOf(Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY));
			adminReply.replace("%Max_Subs%", String.valueOf(Config.MAX_SUBCLASS));
			adminReply.replace("%Max_Sub_Lv%", String.valueOf(Config.MAX_SUBCLASS_LEVEL));
			separateAndSend(adminReply.getHtm(), player);
		}
		return;
	}
	
	private void showGameplay(L2PcInstance player)
	{
		{
			TextBuilder html = new TextBuilder();
			{
				
				html.append("");
			}
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/gameplay.htm");
			adminReply.replace("%rate_xp%", String.valueOf(Config.RATE_XP));
			adminReply.replace("%rate_sp%", String.valueOf(Config.RATE_SP));
			adminReply.replace("%rate_party_xp%", String.valueOf(Config.RATE_PARTY_XP));
			adminReply.replace("%rate_party_sp%", String.valueOf(Config.RATE_PARTY_SP));
			adminReply.replace("%rate_adena%", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(57)));
			adminReply.replace("%rate_items%", String.valueOf(Config.RATE_DROP_ITEMS));
			adminReply.replace("%rate_spoil%", String.valueOf(Config.RATE_DROP_SPOIL));
			adminReply.replace("%rate_drop_manor%", String.valueOf(Config.RATE_DROP_MANOR));
			adminReply.replace("%rate_drop_quest%", String.valueOf(Config.RATE_QUEST_DROP));
			adminReply.replace("%safe_max_full%", String.valueOf(Config.ENCHANT_SAFE_MAX_FULL));
			adminReply.replace("%Enchant_Max%", String.valueOf(Config.MAX_ENCHANT_LEVEL));
			adminReply.replace("%Enchant_Chance%", String.valueOf(Config.ENCHANT_CHANCE));
			adminReply.replace("%Buffs_max%", String.valueOf(Config.BUFFS_MAX_AMOUNT));
			adminReply.replace("%Dance_max%", String.valueOf(Config.DANCES_MAX_AMOUNT));
			adminReply.replace("%Buffs_max_tringer%", String.valueOf(Config.TRIGGERED_BUFFS_MAX_AMOUNT));
			adminReply.replace("%Ally_max_clans%", String.valueOf(Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY));
			adminReply.replace("%Max_Subs%", String.valueOf(Config.MAX_SUBCLASS));
			adminReply.replace("%Max_Sub_Lv%", String.valueOf(Config.MAX_SUBCLASS_LEVEL));
			adminReply.replace("%Players_online%", Integer.toString(L2World.getInstance().getAllPlayersCount()));
			adminReply.replace("%server_restarted%", String.valueOf(GameServer.dateTimeServerStarted.getTime()));
			adminReply.replace("%server_core_version%", String.valueOf(Config.SERVER_VERSION));
			adminReply.replace("%server_os%", String.valueOf(System.getProperty("os.name")));
			adminReply.replace("%server_free_mem%", String.valueOf(((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576));
			adminReply.replace("%server_total_mem%", String.valueOf(Runtime.getRuntime().totalMemory() / 1048576));
			separateAndSend(adminReply.getHtm(), player);
		}
		return;
	}
	
	private void showHero(L2PcInstance player)
	{
		
		CBStatMan tp;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT h.count, h.played, ch.char_name, ch.base_class, ch.online, cl.clan_name, cl.ally_name FROM heroes h LEFT JOIN characters ch ON ch.charId=h.charId LEFT OUTER JOIN clan_data cl ON cl.clan_id=ch.clanid ORDER BY h.count DESC, ch.char_name ASC LIMIT 20");
			rs = statement.executeQuery();
			
			TextBuilder html = new TextBuilder();
			html.append("<center>Top 20 Heros</center>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700 bgcolor=CCCCCC>");
			html.append("<tr>");
			html.append("<td width=300>Name</td>");
			html.append("<td width=300>Class</td>");
			html.append("<td width=300>Clan Name</td>");
			html.append("<td width=300>Ally Name</td>");
			html.append("<td width=100>Played</td>");
			html.append("<td width=100>Count</td>");
			html.append("<td width=100>Status</td>");
			html.append("</tr>");
			html.append("</table>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700>");
			while (rs.next())
			{
				tp = new CBStatMan();
				tp.hCount = rs.getInt("count");
				tp.hPlayed = rs.getInt("played");
				tp.ChName = rs.getString("char_name");
				tp.ChClass = rs.getInt("base_class");
				tp.ChOnOff = rs.getInt("online");
				tp.ChClanName = rs.getString("clan_name");
				tp.ChClanAlly = rs.getString("ally_name");
				tp.ChClanLevel = rs.getInt("clan_level");
				String ClanAlly;
				if (tp.ChClanAlly == null)
				{
					ClanAlly = "---";
				}
				else
				{
					ClanAlly = tp.ChClanAlly;
				}
				String OnOff;
				String color;
				if (tp.ChOnOff == 1)
				{
					OnOff = "ONLINE";
					color = "00CC00";
				}
				else
				{
					OnOff = "OFFLINE";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=300>" + tp.ChName + "</td>");
				html.append("<td width=300>" + tp.ChClass + "</td>");
				html.append("<td width=300>" + tp.ChClanName + "</td>");
				html.append("<td width=300>" + ClanAlly + "</td>");
				html.append("<td width=100>" + tp.hPlayed + "</td>");
				html.append("<td width=100>" + tp.hCount + "</td>");
				html.append("<td width=100><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/11.htm");
			adminReply.replace("%stat%", html.toString());
			separateAndSend(adminReply.getHtm(), player);
			
			statement.close();
			rs.close();
			
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void showPvp(L2PcInstance player)
	{
		
		CBStatMan tp;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pvpkills DESC LIMIT 20;");
			rs = statement.executeQuery();
			
			TextBuilder html = new TextBuilder();
			html.append("<center>Top 20 PvP Kills</center>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700 bgcolor=CCCCCC>");
			html.append("<tr>");
			html.append("<td width=350>Name</td>");
			html.append("<td width=100>Sex</td>");
			html.append("<td width=200>Online Time</td>");
			html.append("<td width=100>PK kills</td>");
			html.append("<td width=100><font color=00CC00>PvP kills</font></td>");
			html.append("<td width=200>Status</td>");
			html.append("</tr>");
			html.append("</table>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700>");
			while (rs.next())
			{
				tp = new CBStatMan();
				tp.PlayerId = rs.getInt("charId");
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");
				String OnOff;
				String color;
				String sex;
				sex = tp.ChSex == 1 ? "Female" : "Male";
				if (tp.ChOnOff == 1)
				{
					OnOff = "ONLINE";
					color = "00CC00";
				}
				else
				{
					OnOff = "OFFLINE";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=350>" + tp.ChName + "</td>");
				html.append("<td width=100>" + sex + "</td>");
				html.append("<td width=200>" + OnlineTime(tp.ChGameTime) + "</td>");
				html.append("<td width=100>" + tp.ChPk + "</td>");
				html.append("<td width=100><font color=00CC00>" + tp.ChPvP + "</font></td>");
				html.append("<td width=200><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/11.htm");
			adminReply.replace("%stat%", html.toString());
			separateAndSend(adminReply.getHtm(), player);
			
			statement.close();
			rs.close();
			
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void showPK(L2PcInstance player)
	{
		
		CBStatMan tp;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT * FROM characters WHERE accesslevel = '0' ORDER BY pkkills DESC LIMIT 20;");
			rs = statement.executeQuery();
			
			TextBuilder html = new TextBuilder();
			html.append("<center>Top 20 PK Kills</center>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700 bgcolor=CCCCCC>");
			html.append("<tr>");
			html.append("<td width=350>Name</td>");
			html.append("<td width=100>Sex</td>");
			html.append("<td width=200>Online Time</td>");
			html.append("<td width=100><font color=00CC00>PK kills</font></td>");
			html.append("<td width=100>PvP kills</td>");
			html.append("<td width=200>Status</td>");
			html.append("</tr>");
			html.append("</table>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700>");
			while (rs.next())
			{
				tp = new CBStatMan();
				tp.PlayerId = rs.getInt("charId");
				tp.ChName = rs.getString("char_name");
				tp.ChSex = rs.getInt("sex");
				tp.ChGameTime = rs.getInt("onlinetime");
				tp.ChPk = rs.getInt("pkkills");
				tp.ChPvP = rs.getInt("pvpkills");
				tp.ChOnOff = rs.getInt("online");
				String OnOff;
				String color;
				String sex;
				sex = tp.ChSex == 1 ? "Female" : "Male";
				if (tp.ChOnOff == 1)
				{
					OnOff = "ONLINE";
					color = "00CC00";
				}
				else
				{
					OnOff = "OFFLINE";
					color = "D70000";
				}
				html.append("<tr>");
				html.append("<td width=350>" + tp.ChName + "</td>");
				html.append("<td width=100>" + sex + "</td>");
				html.append("<td width=200>" + OnlineTime(tp.ChGameTime) + "</td>");
				html.append("<td width=100><font color=00CC00>" + tp.ChPk + "</font></td>");
				html.append("<td width=100>" + tp.ChPvP + "</td>");
				html.append("<td width=200><font color=" + color + ">" + OnOff + "</font></td>");
				html.append("</tr>");
			}
			html.append("</table>");
			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/11.htm");
			adminReply.replace("%stat%", html.toString());
			separateAndSend(adminReply.getHtm(), player);
			
			statement.close();
			rs.close();
			
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void showClan(L2PcInstance player)
	{
		
		CBStatMan tp;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			statement = con.prepareStatement("SELECT clan_name,clan_level,reputation_score,ally_name FROM clan_data WHERE clan_level>0 order by clan_level desc limit 20;");
			rs = statement.executeQuery();
			
			TextBuilder html = new TextBuilder();
			html.append("<center>Top 20 clans</center>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700 bgcolor=CCCCCC>");
			html.append("<tr>");
			html.append("<td width=350>Clan Name</td>");
			html.append("<td width=100>Ally Name</td>");
			html.append("<td width=250>Reputation Score</td>");
			html.append("<td width=200>Clan Level</td>");
			html.append("</tr>");
			html.append("</table>");
			html.append("<img src=L2UI.SquareWhite width=700 height=1>");
			html.append("<table width=700>");
			while (rs.next())
			{
				tp = new CBStatMan();
				tp.ChClanName = rs.getString("clan_name");
				tp.ChClanAlly = rs.getString("ally_name");
				tp.ChClanRep = rs.getInt("reputation_score");
				tp.ChClanLevel = rs.getInt("clan_level");
				String ClanAlly;
				if (tp.ChClanAlly == null)
				{
					ClanAlly = "---";
				}
				else
				{
					ClanAlly = tp.ChClanAlly;
				}
				
				html.append("<tr>");
				html.append("<td width=350>" + tp.ChClanName + "</td>");
				html.append("<td width=100>" + ClanAlly + "</td>");
				html.append("<td width=250>" + tp.ChClanRep + "</td>");
				html.append("<td width=200>" + tp.ChClanLevel + "</td>");
				html.append("</tr>");
			}
			html.append("</table>");
			
			NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
			adminReply.setFile(player.getLang(), "data/html/CommunityBoard/11.htm");
			adminReply.replace("%stat%", html.toString());
			separateAndSend(adminReply.getHtm(), player);
			
			statement.close();
			rs.close();
			
			return;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	String OnlineTime(int time)
	{
		long onlinetimeH;
		if (((time / 60 / 60) - 0.5) <= 0)
		{
			onlinetimeH = 0;
		}
		else
		{
			onlinetimeH = Math.round((time / 60 / 60) - 0.5);
		}
		return "" + onlinetimeH + " h. ";
	}
	
	public String getPlayerRunTime(int secs)
	{
		String timeResult = "";
		if (secs >= 86400)
		{
			timeResult = Integer.toString(secs / 86400) + " Days " + Integer.toString((secs % 86400) / 3600) + " hours";
		}
		else
		{
			timeResult = Integer.toString(secs / 3600) + " Hours " + Integer.toString((secs % 3600) / 60) + " mins";
		}
		return timeResult;
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance player)
	{
		
	}
	
	private static class SingletonHolder
	{
		protected static final StateBBSManager _instance = new StateBBSManager();
	}
}