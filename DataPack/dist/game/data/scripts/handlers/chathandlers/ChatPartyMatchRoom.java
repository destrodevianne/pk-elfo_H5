package handlers.chathandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IChatHandler;
import pk.elfo.gameserver.model.PartyMatchRoom;
import pk.elfo.gameserver.model.PartyMatchRoomList;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */

public class ChatPartyMatchRoom implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		14
	};
	
	/**
	 * Handle chat type 'partymatchroom'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (activeChar.isInPartyMatchRoom())
		{
			PartyMatchRoom _room = PartyMatchRoomList.getInstance().getPlayerRoom(activeChar);
			if (_room != null)
			{
				if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
				{
					activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
					return;
				}
				
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
				for (L2PcInstance _member : _room.getPartyMembers())
				{
					_member.sendPacket(cs);
				}
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
	
	public static void main(String[] args)
	{
		new ChatPartyMatchRoom();
	}
}