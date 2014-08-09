package handlers.itemhandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */
 
public class ItemSkills extends ItemSkillsTemplate
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		final L2PcInstance activeChar = playable.getActingPlayer();
		if ((activeChar != null) && activeChar.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			return false;
		}
		return super.useItem(playable, item, forceUse);
	}
}