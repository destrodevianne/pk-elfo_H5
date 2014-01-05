/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package pk.elfo.gameserver.communitybbs;

import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.communitybbs.Manager.BuffBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.ClanBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.ClassBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.EnchantBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.PostBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.RegionBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.ServiceBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.ShopBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.StateBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.TeleportBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.TopBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.TopicBBSManager;
import pk.elfo.gameserver.datatables.MultiSell;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExShowVariationCancelWindow;
import pk.elfo.gameserver.network.serverpackets.ExShowVariationMakeWindow;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

public class CommunityBoard
{
	public static CommunityBoard getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void handleCommands(L2GameClient client, String command)
	{
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (activeChar.isInOlympiadMode())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.inObserverMode())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isAlikeDead())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInSiege())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInsideZone(ZoneId.PVP))
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInCombat())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isDead())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isCastingNow())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isAttackingNow())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInJail())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isFlying())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInDuel())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isIn7sDungeon())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInBoat())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		if (activeChar.isInAirShip())
		{
			activeChar.sendMessage("You cant use Community Board for now.");
			return;
		}
		
		switch (Config.COMMUNITY_TYPE)
		{
			default:
			case 0: // disabled
				activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
				break;
			case 1: // old
				RegionBBSManager.getInstance().parsecmd(command, activeChar);
				break;
			case 2: // new
				if (command.startsWith("_bbsclan"))
				{
					ClanBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsmemo"))
				{
					TopicBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbstopics"))
				{
					TopicBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsposts"))
				{
					PostBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbstop"))
				{
					TopBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbshome"))
				{
					TopBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsloc"))
				{
					RegionBBSManager.getInstance().parsecmd(command, activeChar);
				}
				else if (command.startsWith("_bbsstat;"))
				{
					if (Config.ALLOW_COMMUNITY_STATS)
					{
						StateBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant see stats!");
					}
					
				}
				else if (command.startsWith("_bbsteleport;"))
				{
					if (Config.ALLOW_COMMUNITY_TELEPORT)
					{
						if ((activeChar.isDead()) || (activeChar.isAlikeDead()) || (activeChar.isInSiege()) || (activeChar.isCastingNow()) || (activeChar.isInCombat()) || (activeChar.isAttackingNow()) || (activeChar.isInOlympiadMode()) || (activeChar.isInJail()) || (activeChar.isFlying()) || (activeChar.getKarma() > 0) || (activeChar.isInDuel()))
						{
							activeChar.sendMessage("You cant use this service!");
							return;
						}
						TeleportBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
					
				}
				else if (command.startsWith("_bbs_buff"))
				{
					if (Config.ALLOW_COMMUNITY_BUFF)
					{
						if ((activeChar.isDead()) || (activeChar.isAlikeDead()) || (activeChar.isInSiege()) || (activeChar.isCastingNow()) || (activeChar.isInCombat()) || (activeChar.isAttackingNow()) || (activeChar.isInOlympiadMode()) || (activeChar.isInJail()) || (activeChar.isFlying()) || (activeChar.getKarma() > 0) || (activeChar.isInDuel()))
						{
							activeChar.sendMessage("You cant use this service!");
							return;
						}
						BuffBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
					
				}
				else if (command.startsWith("_bbsservice"))
				{
					if (Config.ALLOW_COMMUNITY_SERVICES)
					{
						ServiceBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
					
				}
				else if (command.startsWith("_bbsechant"))
				{
					if (Config.ALLOW_COMMUNITY_ENCHANT)
					{
						EnchantBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
					
				}
				else if (command.startsWith("_bbsclass"))
				{
					if (Config.ALLOW_COMMUNITY_CLASS)
					{
						ClassBBSManager.getInstance().parsecmd(command, activeChar);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
					
				}
				else if (command.startsWith("_bbsmultisell;"))
				{
					if (Config.ALLOW_COMMUNITY_MULTISELL)
					{
						if ((activeChar.isDead()) || (activeChar.isAlikeDead()) || (activeChar.isInSiege()) || (activeChar.isCastingNow()) || (activeChar.isInCombat()) || (activeChar.isAttackingNow()) || (activeChar.isInOlympiadMode()) || (activeChar.isInJail()) || (activeChar.isFlying()) || (activeChar.getKarma() > 0) || (activeChar.isInDuel()))
						{
							activeChar.sendMessage("You cant use this service!");
							return;
						}
						StringTokenizer st = new StringTokenizer(command, ";");
						st.nextToken();
						ShopBBSManager.getInstance().parsecmd("_bbsshop;" + st.nextToken(), activeChar);
						MultiSell.getInstance().separateAndSend(Integer.parseInt(st.nextToken()), activeChar, null, false);
					}
					else
					{
						activeChar.sendMessage("You cant use this service!");
					}
				}
				else if (command.startsWith("_bbsAugment;add"))
				{
					if (Config.ALLOW_COMMUNITY_MULTISELL)
					{
						TopBBSManager.getInstance().parsecmd(command, activeChar);
						activeChar.sendPacket(SystemMessageId.SELECT_THE_ITEM_TO_BE_AUGMENTED);
						activeChar.sendPacket(new ExShowVariationMakeWindow());
						activeChar.cancelActiveTrade();
						TopBBSManager.getInstance().parsecmd(command, activeChar);
						return;
					}
					
					activeChar.sendMessage("You cant use this service!");
					return;
				}
				else if (command.startsWith("_bbsAugment;remove"))
				{
					if (Config.ALLOW_COMMUNITY_MULTISELL)
					{
						TopBBSManager.getInstance().parsecmd(command, activeChar);
						activeChar.sendPacket(SystemMessageId.SELECT_THE_ITEM_FROM_WHICH_YOU_WISH_TO_REMOVE_AUGMENTATION);
						activeChar.sendPacket(new ExShowVariationCancelWindow());
						activeChar.cancelActiveTrade();
						TopBBSManager.getInstance().parsecmd(command, activeChar);
						return;
					}
					
					activeChar.sendMessage("You cant use this service!");
					return;
				}
				else
				{
					ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
					activeChar.sendPacket(sb);
					activeChar.sendPacket(new ShowBoard(null, "102"));
					activeChar.sendPacket(new ShowBoard(null, "103"));
				}
				break;
		}
	}
	
	/**
	 * @param client
	 * @param url
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 * @param arg4
	 * @param arg5
	 */
	public void handleWriteCommands(L2GameClient client, String url, String arg1, String arg2, String arg3, String arg4, String arg5)
	{
		L2PcInstance activeChar = client.getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		switch (Config.COMMUNITY_TYPE)
		{
			case 2:
				if (url.equals("Topic"))
				{
					TopicBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Post"))
				{
					PostBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Region"))
				{
					RegionBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else if (url.equals("Notice"))
				{
					ClanBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				}
				else
				{
					ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + url + " is not implemented yet</center><br><br></body></html>", "101");
					activeChar.sendPacket(sb);
					activeChar.sendPacket(new ShowBoard(null, "102"));
					activeChar.sendPacket(new ShowBoard(null, "103"));
				}
				break;
			case 1:
				RegionBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
				break;
			default:
			case 0:
				ShowBoard sb = new ShowBoard("<html><body><br><br><center>The Community board is currently disabled</center><br><br></body></html>", "101");
				activeChar.sendPacket(sb);
				activeChar.sendPacket(new ShowBoard(null, "102"));
				activeChar.sendPacket(new ShowBoard(null, "103"));
				break;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final CommunityBoard _instance = new CommunityBoard();
	}
}
