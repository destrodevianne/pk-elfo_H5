package handlers.itemhandlers;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ShowMiniMap;

/**
 * This class provides handling for items that should display a map when double clicked.
 * Projeto PkElfo
 */
public class Maps implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		playable.sendPacket(new ShowMiniMap(item.getItemId()));
		return true;
	}
}