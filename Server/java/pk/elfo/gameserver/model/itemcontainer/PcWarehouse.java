package pk.elfo.gameserver.model.itemcontainer;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance.ItemLocation;

/**
 * @author PkElfo
 */

public class PcWarehouse extends Warehouse
{
	// private static final Logger _log = Logger.getLogger(PcWarehouse.class.getName());
	
	private final L2PcInstance _owner;
	
	public PcWarehouse(L2PcInstance owner)
	{
		_owner = owner;
	}
	
	@Override
	public String getName()
	{
		return "Warehouse";
	}
	
	@Override
	public L2PcInstance getOwner()
	{
		return _owner;
	}
	
	@Override
	public ItemLocation getBaseLocation()
	{
		return ItemLocation.WAREHOUSE;
	}
	
	@Override
	public boolean validateCapacity(long slots)
	{
		return ((_items.size() + slots) <= _owner.getWareHouseLimit());
	}
}
