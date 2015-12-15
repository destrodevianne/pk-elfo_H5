package handlers.chathandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IChatHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */

public class ChatPartyRoomAll implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		16
	};
	
	/**
	 * Handle chat type 'party room all'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (activeChar.isInParty())
		{
			if (activeChar.getParty().isInCommandChannel() && activeChar.getParty().isLeader(activeChar))
			{
				if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
				{
					activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
					return;
				}
				
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
				activeChar.getParty().getCommandChannel().broadcastCreatureSay(cs, activeChar);
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