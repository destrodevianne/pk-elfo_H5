package pk.elfo.gameserver.network.serverpackets;

public class EnchantResult extends L2GameServerPacket
{
	private final int _result;
	private final int _crystal;
	private final int _count;
	
	public EnchantResult(int result, int crystal, int count)
	{
		_result = result;
		_crystal = crystal;
		_count = count;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x87);
		writeD(_result);
		writeD(_crystal);
		writeQ(_count);
	}
}