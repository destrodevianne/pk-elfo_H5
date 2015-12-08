package pk.elfo.gameserver.network.serverpackets;

public class ExAutoSoulShot extends L2GameServerPacket
{
	private final int _itemId;
	private final int _type;
	
	/**
	 * @param itemId
	 * @param type
	 */
	public ExAutoSoulShot(int itemId, int type)
	{
		_itemId = itemId;
		_type = type;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x0c); // sub id
		writeD(_itemId);
		writeD(_type);
	}
}