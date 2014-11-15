package pk.elfo.gameserver.network.serverpackets;

public class ExBrPremiumState extends L2GameServerPacket
{
	private final int _objId;
	private final int _state;
	
	public ExBrPremiumState(int id, int state)
	{
		_objId = id;
		_state = state;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xD9);
		writeD(_objId);
		writeC(_state);
	}
}