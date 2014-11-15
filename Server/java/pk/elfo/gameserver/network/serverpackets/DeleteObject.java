package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.L2Object;

public final class DeleteObject extends L2GameServerPacket
{
	private final int _objectId;
	
	public DeleteObject(L2Object obj)
	{
		_objectId = obj.getObjectId();
	}
	
	public DeleteObject(int objectId)
	{
		_objectId = objectId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x08);
		writeD(_objectId);
		writeD(0x00); // c2
	}
}