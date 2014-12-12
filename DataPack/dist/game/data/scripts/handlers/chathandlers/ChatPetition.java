package handlers.chathandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IChatHandler;
import pk.elfo.gameserver.instancemanager.PetitionManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */

public class ChatPetition implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		6,
		7
	};
	
	/**
	 * Handle chat type 'petition player'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
		{
			activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
			return;
		}
		
		if (!PetitionManager.getInstance().isPlayerInConsultation(activeChar))
		{
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_IN_PETITION_CHAT);
			return;
		}
		
		PetitionManager.getInstance().sendActivePetitionMessage(activeChar, text);
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