package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.datatables.AdventBonus;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExNevitAdventPointInfoPacket extends L2GameServerPacket
{
	private final int _points;
	
	public ExNevitAdventPointInfoPacket(L2PcInstance player)
	{
		_points = AdventBonus.getInstance().getAdventPoints(player.getObjectId());
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xDF);
		writeD(_points); // 72 = 1%, max 7200 = 100%
	}
}