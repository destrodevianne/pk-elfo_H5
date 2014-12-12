package handlers.itemhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class ManaPotion extends ItemSkills
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!Config.CUSTOMMOD_ENABLE_MANA_POTIONS_SUPPORT)
		{
			playable.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return false;
		}
		if(!Config.PVP_MANAPOTION && playable.getPvpFlag()!=0)
		{
			playable.sendPacket(SystemMessageId.NOTHING_HAPPENED);
			return false;
		}
		return super.useItem(playable, item, forceUse);
	}
}