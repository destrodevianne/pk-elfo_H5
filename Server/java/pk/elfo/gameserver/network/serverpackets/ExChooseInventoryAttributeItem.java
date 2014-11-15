package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.Elementals;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;

public class ExChooseInventoryAttributeItem extends L2GameServerPacket
{
	private final int _itemId;
	private final byte _atribute;
	private final int _level;
	
	public ExChooseInventoryAttributeItem(L2ItemInstance item)
	{
		_itemId = item.getDisplayId();
		_atribute = Elementals.getItemElement(_itemId);
		if (_atribute == Elementals.NONE)
		{
			throw new IllegalArgumentException("Undefined Atribute item: " + item);
		}
		_level = Elementals.getMaxElementLevel(_itemId);
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x62);
		writeD(_itemId);
		// Structure for now
		// Must be 0x01 for stone/crystal attribute type
		writeD(_atribute == Elementals.FIRE ? 1 : 0); // Fire
		writeD(_atribute == Elementals.WATER ? 1 : 0); // Water
		writeD(_atribute == Elementals.WIND ? 1 : 0); // Wind
		writeD(_atribute == Elementals.EARTH ? 1 : 0); // Earth
		writeD(_atribute == Elementals.HOLY ? 1 : 0); // Holy
		writeD(_atribute == Elementals.DARK ? 1 : 0); // Unholy
		writeD(_level); // Item max attribute level
	}
}