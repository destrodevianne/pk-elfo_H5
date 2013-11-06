/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.chathandlers;

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import king.server.Config;
import king.server.gameserver.handler.IChatHandler;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.handler.VoicedCommandHandler;
import king.server.gameserver.model.BlockList;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.CreatureSay;
import king.server.gameserver.util.Util;

/**
 * A chat handler
 * @author durgus
 */
public class ChatAll implements IChatHandler
{
	private static Logger _log = Logger.getLogger(ChatAll.class.getName());
	
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	/**
	 * Handle chat type 'all'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String params, String text)
	{
		boolean vcd_used = false;
		if (text.startsWith("."))
		{
			StringTokenizer st = new StringTokenizer(text);
			IVoicedCommandHandler vch;
			String command = "";
			
			if (st.countTokens() > 1)
			{
				command = st.nextToken().substring(1);
				params = text.substring(command.length() + 2);
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			else
			{
				command = text.substring(1);
				if (Config.DEBUG)
				{
					_log.info("Command: " + command);
				}
				vch = VoicedCommandHandler.getInstance().getHandler(command);
			}
			if (vch != null)
			{
				vch.useVoicedCommand(command, activeChar, params);
				vcd_used = true;
			}
			else
			{
				if (Config.DEBUG)
				{
					_log.warning("No handler registered for bypass '" + command + "'");
				}
				vcd_used = false;
			}
		}
		if (!vcd_used)
		{
			if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
			
			/**
			 * Match the character "." literally (Exactly 1 time) Match any character that is NOT a . character. Between one and unlimited times as possible, giving back as needed (greedy)
			 */
			if (text.matches("\\.{1}[^\\.]+"))
			{
				activeChar.sendPacket(SystemMessageId.INCORRECT_SYNTAX);
			}
			else
			{
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getAppearance().getVisibleName(), text);
				Collection<L2PcInstance> plrs = activeChar.getKnownList().getKnownPlayers().values();
				for (L2PcInstance player : plrs)
				{
					if ((player != null) && activeChar.isInsideRadius(player, 1250, false, true) && !BlockList.isBlocked(player, activeChar))
					{
						player.sendPacket(cs);
					}
				}
				
				activeChar.sendPacket(cs);
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler.
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}