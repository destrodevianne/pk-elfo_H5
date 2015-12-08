package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.VehiclePathPoint;

public class ExAirShipTeleportList extends L2GameServerPacket
{
	private final int _dockId;
	private final VehiclePathPoint[][] _teleports;
	private final int[] _fuelConsumption;
	
	public ExAirShipTeleportList(int dockId, VehiclePathPoint[][] teleports, int[] fuelConsumption)
	{
		_dockId = dockId;
		_teleports = teleports;
		_fuelConsumption = fuelConsumption;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x9a);
		
		writeD(_dockId);
		if (_teleports != null)
		{
			writeD(_teleports.length);
			
			VehiclePathPoint[] path;
			VehiclePathPoint dst;
			for (int i = 0; i < _teleports.length; i++)
			{
				writeD(i - 1);
				writeD(_fuelConsumption[i]);
				path = _teleports[i];
				dst = path[path.length - 1];
				writeD(dst.x);
				writeD(dst.y);
				writeD(dst.z);
			}
		}
		else
		{
			writeD(0);
		}
	}
}