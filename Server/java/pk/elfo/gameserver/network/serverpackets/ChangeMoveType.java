package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.L2Character;

public class ChangeMoveType extends L2GameServerPacket
{
	public static final int WALK = 0;
	public static final int RUN = 1;
	
	private final int _charObjId;
	private final boolean _running;
	
	public ChangeMoveType(L2Character character)
	{
		_charObjId = character.getObjectId();
		_running = character.isRunning();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x28);
		writeD(_charObjId);
		writeD(_running ? RUN : WALK);
		writeD(0); // c2
	}
}