package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;

public final class DoorInfo extends L2GameServerPacket
{
	private final L2DoorInstance _door;
	
	public DoorInfo(L2DoorInstance door)
	{
		_door = door;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x4c);
		writeD(_door.getObjectId());
		writeD(_door.getDoorId());
	}
}