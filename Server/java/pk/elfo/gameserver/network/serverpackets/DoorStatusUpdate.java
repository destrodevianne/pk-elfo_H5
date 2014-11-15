package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;

public final class DoorStatusUpdate extends L2GameServerPacket
{
	private final L2DoorInstance _door;
	
	public DoorStatusUpdate(L2DoorInstance door)
	{
		_door = door;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4d);
		writeD(_door.getObjectId());
		writeD(_door.getOpen() ? 0 : 1);
		writeD(_door.getDamage());
		writeD(_door.isEnemy() ? 1 : 0);
		writeD(_door.getDoorId());
		writeD((int) _door.getCurrentHp());
		writeD(_door.getMaxHp());
	}
}