package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.L2Character;

public class ExFishingStart extends L2GameServerPacket
{
	private final L2Character _activeChar;
	private final int _x, _y, _z, _fishType;
	private final boolean _isNightLure;
	
	public ExFishingStart(L2Character character, int fishType, int x, int y, int z, boolean isNightLure)
	{
		_activeChar = character;
		_fishType = fishType;
		_x = x;
		_y = y;
		_z = z;
		_isNightLure = isNightLure;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x1E);
		writeD(_activeChar.getObjectId());
		writeD(_fishType); // fish type
		writeD(_x); // x position
		writeD(_y); // y position
		writeD(_z); // z position
		writeC(_isNightLure ? 0x01 : 0x00); // night lure
		writeC(0x00); // show fish rank result button
	}
}