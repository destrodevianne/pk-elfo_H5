package handlers.chathandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IChatHandler;
import pk.elfo.gameserver.model.BlockList;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */

public class ChatHeroVoice implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		17
	};
	
	/**
	 * Handle chat type 'hero voice'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
        if (Config.CHAT_HERO_NEED_PVPS)
        {
                if (activeChar.getPvpKills() < Config.PVPS_TO_USE_CHAT_HERO)
                {
                         CreatureSay ct = new CreatureSay(0, Say2.TELL,"Hero Voice","Voce presisa ter " + Config.PVPS_TO_USE_CHAT_HERO + " PvPs para usar o Chat Hero.");
                         activeChar.sendPacket(ct);
                         return;
                }
        }
		if (activeChar.isHero() || activeChar.canOverrideCond(PcCondOverride.CHAT_CONDITIONS))
		{
			if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
			
			if (!activeChar.getFloodProtectors().getHeroVoice().tryPerformAction("hero voice"))
			{
				activeChar.sendMessage("Falha na acao. Herois so sao capazes de falar no canal global uma vez a cada 10 segundos.");
				return;
			}
			CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
			
			for (L2PcInstance player : L2World.getInstance().getAllPlayersArray())
			{
				if ((player != null) && !BlockList.isBlocked(player, activeChar))
				{
					player.sendPacket(cs);
				}
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
