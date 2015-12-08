package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Point3D;

public class ExMoveToLocationInAirShip extends L2GameServerPacket
{
	private final int _charObjId;
	private final int _airShipId;
	private final Point3D _destination;
	private final int _heading;
	
	/**
	 * @param player
	 */
	public ExMoveToLocationInAirShip(L2PcInstance player)
	{
		_charObjId = player.getObjectId();
		_airShipId = player.getAirShip().getObjectId();
		_destination = player.getInVehiclePosition();
		_heading = player.getHeading();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x6D);
		writeD(_charObjId);
		writeD(_airShipId);
		writeD(_destination.getX());
		writeD(_destination.getY());
		writeD(_destination.getZ());
		writeD(_heading);
	}
}