package pk.elfo.gameserver.network.serverpackets;

import java.util.List;

public class ExCursedWeaponList extends L2GameServerPacket
{
	private final List<Integer> _cursedWeaponIds;
	
	public ExCursedWeaponList(List<Integer> cursedWeaponIds)
	{
		_cursedWeaponIds = cursedWeaponIds;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x46);
		
		writeD(_cursedWeaponIds.size());
		for (int i : _cursedWeaponIds)
		{
			writeD(i);
		}
	}
}