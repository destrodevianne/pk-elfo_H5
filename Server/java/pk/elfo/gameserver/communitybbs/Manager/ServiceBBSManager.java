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

import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.datatables.ExperienceTable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.stat.PcStat;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExBrExtraUserInfo;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;
import pk.elfo.gameserver.network.serverpackets.SocialAction;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

public class ServiceBBSManager extends BaseBBSManager
{
	private static String _val = "85";
	
	private ServiceBBSManager()
	{
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if (command.equals("_bbsservice"))
		{
			sendHtm(activeChar, "data/html/CommunityBoard/42.htm");
		}
		else if (command.startsWith("_bbsservice;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			String param;
			
			param = st.nextToken();
			
			if (param.equalsIgnoreCase("Noobles"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.NoblItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.NoblItemId).getCount() < Config.NoblItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getClassId().level() < 3)
				{
					activeChar.sendMessage("You must have third class.");
					return;
				}
				if (activeChar.isNoble())
				{
					activeChar.sendMessage("You already have nobless.");
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.NoblItemId, Config.NoblItemCount, activeChar, true);
				activeChar.setNoble(true);
				activeChar.broadcastUserInfo();
			}
			else if (param.equalsIgnoreCase("Gender"))
			{
				if (activeChar.getRace().ordinal() == 5)
				{
					activeChar.sendMessage("Kamael can't change gender.");
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.GenderItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.GenderItemId).getCount() < Config.GenderItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.GenderItemId, Config.GenderItemCount, activeChar, true);
				activeChar.getAppearance().setSex(activeChar.getAppearance().getSex() ? false : true);
				activeChar.broadcastUserInfo();
				activeChar.decayMe();
				activeChar.spawnMe(activeChar.getX(), activeChar.getY(), activeChar.getZ());
			}
			else if (param.equalsIgnoreCase("Delevel"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.DelevelItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.DelevelItemId).getCount() < Config.DelevelItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getStat().getLevel() < 10)
				{
					activeChar.sendMessage("You must be more than 10 Lv.");
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.DelevelItemId, Config.DelevelItemCount, activeChar, true);
				activeChar.getStat().removeExpAndSp((activeChar.getExp() - ExperienceTable.getInstance().getExpForLevel(activeChar.getStat().getLevel() - 1)), 0);
			}
			else if (param.equalsIgnoreCase("SetHero"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.HeroItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.HeroItemId).getCount() < Config.HeroItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getClassId().level() < 3)
				{
					activeChar.sendMessage("You must have third class.");
					return;
				}
				if (activeChar.isHero())
				{
					activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
					activeChar.sendMessage("You already have Hero.");
				}
				else
				{
					activeChar.destroyItemByItemId("ShopBBS", Config.HeroItemId, Config.HeroItemCount, activeChar, true);
					activeChar.getLevel();
					activeChar.getStat().addLevel(Byte.parseByte(_val));
					activeChar.setHero(true);
					activeChar.broadcastUserInfo();
				}
			}
			else if (param.equalsIgnoreCase("RecoveryPK"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.RecoveryPKItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.RecoveryPKItemId).getCount() < Config.RecoveryPKItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.RecoveryPKItemId, Config.RecoveryPKItemCount, activeChar, true);
				activeChar.setKarma(0);
				activeChar.setPkKills(0);
				activeChar.broadcastUserInfo();
				activeChar.sendPacket(new UserInfo(activeChar));
				activeChar.sendPacket(new ExBrExtraUserInfo(activeChar));
			}
			else if (param.equalsIgnoreCase("RecoveryVitality"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.RecoveryVitalityItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.RecoveryVitalityItemId).getCount() < Config.RecoveryVitalityItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.RecoveryVitalityItemId, Config.RecoveryVitalityItemCount, activeChar, true);
				activeChar.setVitalityPoints(Math.min(Config.STARTING_VITALITY_POINTS, PcStat.MAX_VITALITY_POINTS), true);
			}
			else if (param.equalsIgnoreCase("AddSP"))
			{
				if (activeChar.getInventory().getItemByItemId(Config.SPItemId) == null)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				if (activeChar.getInventory().getItemByItemId(Config.SPItemId).getCount() < Config.SPItemCount)
				{
					activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_ITEMS));
					return;
				}
				activeChar.destroyItemByItemId("ShopBBS", Config.SPItemId, Config.SPItemCount, activeChar, true);
				activeChar.setSp(activeChar.getSp() + 10000000);
				activeChar.sendMessage("You received 10kk SP.");
				activeChar.broadcastPacket(new SocialAction(activeChar.getObjectId(), 16));
				activeChar.broadcastUserInfo();
			}
		}
		else
		{
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
	}
	
	private boolean sendHtm(L2PcInstance player, String path)
	{
		String oriPath = path;
		if ((player.getLang() != null) && !player.getLang().equalsIgnoreCase("en"))
		{
			if (path.contains("html/"))
			{
				path = path.replace("html/", "html-" + player.getLang() + "/");
			}
		}
		String content = HtmCache.getInstance().getHtm(path);
		if ((content == null) && !oriPath.equals(path))
		{
			content = HtmCache.getInstance().getHtm(oriPath);
		}
		if (content == null)
		{
			return false;
		}
		
		separateAndSend(content, player);
		return true;
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
	}
	
	public static ServiceBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ServiceBBSManager _instance = new ServiceBBSManager();
	}
}