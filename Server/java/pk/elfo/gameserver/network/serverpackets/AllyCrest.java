package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.cache.CrestCache;

public class AllyCrest extends L2GameServerPacket
{
	private final int _crestId;
	private final byte[] _data;
	
	public AllyCrest(int crestId)
	{
		_crestId = crestId;
		_data = CrestCache.getInstance().getAllyCrest(_crestId);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xaf);
		writeD(_crestId);
		if (_data != null)
		{
			writeD(_data.length);
			writeB(_data);
		}
		else
		{
			writeD(0);
		}
	}
}