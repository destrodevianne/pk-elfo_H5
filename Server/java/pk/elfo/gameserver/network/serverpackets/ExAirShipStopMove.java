package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExAirShipStopMove extends L2GameServerPacket
{
	private final int _playerId, _airShipId, _x, _y, _z;
	
	public ExAirShipStopMove(L2PcInstance player, L2AirShipInstance ship, int x, int y, int z)
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
		writeC(0xfe);
		writeH(0x66);

		writeD(_airShipId);
		writeD(_playerId);
		writeD(_x);
		writeD(_y);
		writeD(_z);
	}
}