package pk.elfo.gameserver.network.serverpackets;

public class AutoAttackStop extends L2GameServerPacket
{
	private final int _targetObjId;
	
	/**
	 * @param targetObjId
	 */
	public AutoAttackStop(int targetObjId)
	{
		_targetObjId = targetObjId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x26);
		writeD(_targetObjId);
	}
}