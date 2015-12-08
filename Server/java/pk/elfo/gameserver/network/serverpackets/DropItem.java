package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.items.instance.L2ItemInstance;

public class DropItem extends L2GameServerPacket
{
	private final L2ItemInstance _item;
	private final int _charObjId;
	
	/**
	 * Constructor of the DropItem server packet
	 * @param item : L2ItemInstance designating the item
	 * @param playerObjId : int designating the player ID who dropped the item
	 */
	public DropItem(L2ItemInstance item, int playerObjId)
	{
		_item = item;
		_charObjId = playerObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x16);
		writeD(_charObjId);
		writeD(_item.getObjectId());
		writeD(_item.getDisplayId());
		
		writeD(_item.getX());
		writeD(_item.getY());
		writeD(_item.getZ());
		// only show item count if it is a stackable item
		writeD(_item.isStackable() ? 0x01 : 0x00);
		writeQ(_item.getCount());
		
		writeD(0x01); // unknown
	}
}