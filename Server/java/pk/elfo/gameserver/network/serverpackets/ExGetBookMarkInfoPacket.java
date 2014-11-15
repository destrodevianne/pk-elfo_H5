package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance.TeleportBookmark;

public class ExGetBookMarkInfoPacket extends L2GameServerPacket
{
	private final L2PcInstance player;
	
	public ExGetBookMarkInfoPacket(L2PcInstance cha)
	{
		player = cha;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x84);
		writeD(0x00); // Dummy
		writeD(player.getBookmarkslot());
		writeD(player.getTpbookmark().size());
		
		for (TeleportBookmark tpbm : player.getTpbookmark())
		{
			writeD(tpbm._id);
			writeD(tpbm._x);
			writeD(tpbm._y);
			writeD(tpbm._z);
			writeS(tpbm._name);
			writeD(tpbm._icon);
			writeS(tpbm._tag);
		}
	}
}