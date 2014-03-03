package handlers.itemhandlers;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExChooseInventoryAttributeItem;

/**
 * PkElfo
 */
 
public class EnchantAttribute implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		if (activeChar.isCastingNow())
		{
			return false;
		}
		
		if (activeChar.isEnchanting())
		{
			activeChar.sendPacket(SystemMessageId.ENCHANTMENT_ALREADY_IN_PROGRESS);
			return false;
		}
		activeChar.setActiveEnchantAttrItem(item);
		activeChar.sendPacket(new ExChooseInventoryAttributeItem(item));
		return true;
	}
}