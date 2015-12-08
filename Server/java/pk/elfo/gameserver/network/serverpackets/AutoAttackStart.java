package pk.elfo.gameserver.network.serverpackets;

public final class AutoAttackStart extends L2GameServerPacket
{
	private final int _targetObjId;
	
	/**
	 * @param targetId
	 */
	public AutoAttackStart(int targetId)
	{
		_targetObjId = targetId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x25);
		writeD(_targetObjId);
	}
}