package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ChairSit extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	private final int _staticObjectId;
	
	/**
	 * @param player
	 * @param staticObjectId
	 */
	public ChairSit(L2PcInstance player, int staticObjectId)
	{
		_activeChar = player;
		_staticObjectId = staticObjectId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xed);
		writeD(_activeChar.getObjectId());
		writeD(_staticObjectId);
	}
}