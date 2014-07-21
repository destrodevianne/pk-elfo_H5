package pk.elfo.gameserver.handler;

import java.util.logging.Logger;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;

public interface IItemHandler
{
	public static Logger _log = Logger.getLogger(IItemHandler.class.getName());
	
	/**
	 * Launch task associated to the item.
	 * @param playable the non-NPC character using the item
	 * @param item L2ItemInstance designating the item to use
	 * @param forceUse ctrl hold on item use
	 * @return 
	 */
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse);
}