package pk.elfo.gameserver.network.serverpackets;

public final class ChooseInventoryItem extends L2GameServerPacket
{
	private final int _itemId;
	
	public ChooseInventoryItem(int itemId)
	{
		_itemId = itemId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7c);
		writeD(_itemId);
	}
}