package pk.elfo.gameserver.network.serverpackets;

public class ExAttributeEnchantResult extends L2GameServerPacket
{
	private final int _result;
	
	public ExAttributeEnchantResult(int result)
	{
		_result = result;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x61);
		
		writeD(_result);
	}
}