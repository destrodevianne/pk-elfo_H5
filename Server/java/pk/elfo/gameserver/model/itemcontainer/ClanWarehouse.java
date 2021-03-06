package pk.elfo.gameserver.model.itemcontainer;

import pk.elfo.Config;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance.ItemLocation;
import pk.elfo.gameserver.scripting.scriptengine.events.ClanWarehouseAddItemEvent;
import pk.elfo.gameserver.scripting.scriptengine.events.ClanWarehouseDeleteItemEvent;
import pk.elfo.gameserver.scripting.scriptengine.events.ClanWarehouseTransferEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.clan.ClanWarehouseListener;
import javolution.util.FastList;

/**
 * @author PkElfo
 */

public final class ClanWarehouse extends Warehouse
{
	private final L2Clan _clan;
	
	private final FastList<ClanWarehouseListener> clanWarehouseListeners = new FastList<ClanWarehouseListener>().shared();
	
	public ClanWarehouse(L2Clan clan)
	{
		_clan = clan;
	}
	
	@Override
	public String getName()
	{
		return "ClanWarehouse";
	}
	
	@Override
	public int getOwnerId()
	{
		return _clan.getClanId();
	}
	
	@Override
	public L2PcInstance getOwner()
	{
		return _clan.getLeader().getPlayerInstance();
	}
	
	@Override
	public ItemLocation getBaseLocation()
	{
		return ItemLocation.CLANWH;
	}
	
	public String getLocationId()
	{
		return "0";
	}
	
	public int getLocationId(boolean dummy)
	{
		return 0;
	}
	
	public void setLocationId(L2PcInstance dummy)
	{
	}
	
	@Override
	public boolean validateCapacity(long slots)
	{
		return ((_items.size() + slots) <= Config.WAREHOUSE_SLOTS_CLAN);
	}
	
	@Override
	public L2ItemInstance addItem(String process, int itemId, long count, L2PcInstance actor, Object reference)
	{
		L2ItemInstance item = getItemByItemId(itemId);
		if (!fireClanWarehouseAddItemListeners(process, item, actor, count))
		{
			return null;
		}
		return super.addItem(process, itemId, count, actor, reference);
	}
	
	@Override
	public L2ItemInstance addItem(String process, L2ItemInstance item, L2PcInstance actor, Object reference)
	{
		if (!fireClanWarehouseAddItemListeners(process, item, actor, item.getCount()))
		{
			return null;
		}
		return super.addItem(process, item, actor, reference);
	}
	
	@Override
	public L2ItemInstance destroyItem(String process, L2ItemInstance item, long count, L2PcInstance actor, Object reference)
	{
		if (!fireClanWarehouseDeleteItemListeners(process, item, actor, count))
		{
			return null;
		}
		return super.destroyItem(process, item, count, actor, reference);
	}
	
	@Override
	public L2ItemInstance transferItem(String process, int objectId, long count, ItemContainer target, L2PcInstance actor, Object reference)
	{
		L2ItemInstance sourceitem = getItemByObjectId(objectId);
		if (!fireClanWarehouseTransferListeners(process, sourceitem, count, target, actor))
		{
			return null;
		}
		return super.transferItem(process, objectId, count, target, actor, reference);
	}
	
	// Listeners
	/**
	 * Fires all the ClanWarehouse add item listeners, if any.<br>
	 * Action is cancelled if it returns false
	 * @param process
	 * @param item
	 * @param actor
	 * @param count
	 * @return
	 */
	private boolean fireClanWarehouseAddItemListeners(String process, L2ItemInstance item, L2PcInstance actor, long count)
	{
		if (!clanWarehouseListeners.isEmpty() && (actor != null) && (item != null))
		{
			ClanWarehouseAddItemEvent event = new ClanWarehouseAddItemEvent();
			event.setActor(actor);
			event.setItem(item);
			event.setCount(count);
			event.setProcess(process);
			for (ClanWarehouseListener listener : clanWarehouseListeners)
			{
				if (!listener.onAddItem(event))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Fires all the ClanWarehouse delete item listeners, if any.<br>
	 * Action is cancelled if it returns false
	 * @param process
	 * @param item
	 * @param actor
	 * @param count
	 * @return
	 */
	private boolean fireClanWarehouseDeleteItemListeners(String process, L2ItemInstance item, L2PcInstance actor, long count)
	{
		if (!clanWarehouseListeners.isEmpty() && (actor != null) && (item != null))
		{
			ClanWarehouseDeleteItemEvent event = new ClanWarehouseDeleteItemEvent();
			event.setActor(actor);
			event.setCount(count);
			event.setItem(item);
			event.setProcess(process);
			for (ClanWarehouseListener listener : clanWarehouseListeners)
			{
				if (!listener.onDeleteItem(event))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Fires all the ClanWarehouse transfer listeners, if any.<br>
	 * Action is cancelled if it returns false
	 * @param process
	 * @param item
	 * @param count
	 * @param target
	 * @param actor
	 * @return
	 */
	private boolean fireClanWarehouseTransferListeners(String process, L2ItemInstance item, long count, ItemContainer target, L2PcInstance actor)
	{
		if (!clanWarehouseListeners.isEmpty() && (actor != null) && (item != null) && (target != null))
		{
			ClanWarehouseTransferEvent event = new ClanWarehouseTransferEvent();
			event.setActor(actor);
			event.setCount(count);
			event.setItem(item);
			event.setProcess(process);
			event.setTarget(target);
			for (ClanWarehouseListener listener : clanWarehouseListeners)
			{
				if (!listener.onTransferItem(event))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Adds a clan warehouse listener
	 * @param listener
	 */
	public void addWarehouseListener(ClanWarehouseListener listener)
	{
		if (!clanWarehouseListeners.contains(listener))
		{
			clanWarehouseListeners.add(listener);
		}
	}
	
	/**
	 * Removes a clan warehouse listener
	 * @param listener
	 */
	public void removeWarehouseListener(ClanWarehouseListener listener)
	{
		clanWarehouseListeners.remove(listener);
	}
}
