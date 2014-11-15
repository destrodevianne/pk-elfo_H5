package pk.elfo.gameserver.network.serverpackets;

public class ExChangeNpcState extends L2GameServerPacket
{
	private final int _objId;
	private final int _state;
	
	public ExChangeNpcState(int objId, int state)
	{
		_objId = objId;
		_state = state;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xBE);
		writeD(_objId);
		writeD(_state);
	}
}