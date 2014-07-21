package pk.elfo.gameserver.network.serverpackets;

/**
 * PkElfo
 */

public class PremiumState extends L2GameServerPacket
{
	private final int _objectId;
	private final int _state;
	
	public PremiumState(int objectId, int state)
	{
		_objectId = objectId;
		_state = state;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xAA);
		writeD(_objectId);
		writeC(_state);
	}
}