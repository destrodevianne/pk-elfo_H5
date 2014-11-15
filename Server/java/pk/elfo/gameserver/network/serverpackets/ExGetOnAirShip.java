package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Point3D;

public class ExGetOnAirShip extends L2GameServerPacket
{
	private final int _playerId, _airShipId;
	private final Point3D _pos;
	
	public ExGetOnAirShip(L2PcInstance player, L2Character ship)
	{
		_playerId = player.getObjectId();
		_airShipId = ship.getObjectId();
		_pos = player.getInVehiclePosition();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x63);
		
		writeD(_playerId);
		writeD(_airShipId);
		writeD(_pos.getX());
		writeD(_pos.getY());
		writeD(_pos.getZ());
	}
}