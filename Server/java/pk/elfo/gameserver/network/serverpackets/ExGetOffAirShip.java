package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.L2Character;

public class ExGetOffAirShip extends L2GameServerPacket
{
	private final int _playerId, _airShipId, _x, _y, _z;
	
	public ExGetOffAirShip(L2Character player, L2Character ship, int x, int y, int z)
	{
		_playerId = player.getObjectId();
		_airShipId = ship.getObjectId();
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x64);
		
		writeD(_playerId);
		writeD(_airShipId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}