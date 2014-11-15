package pk.elfo.gameserver.network.serverpackets;

public class ExBaseAttributeCancelResult extends L2GameServerPacket
{
	private final int _objId;
	private final byte _attribute;
	
	public ExBaseAttributeCancelResult(int objId, byte attribute)
	{
		_objId = objId;
		_attribute = attribute;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x75);
		writeD(0x01); // result
		writeD(_objId);
		writeD(_attribute);
	}
}