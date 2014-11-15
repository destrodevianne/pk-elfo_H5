package pk.elfo.gameserver.network.serverpackets;

public class CharDeleteSuccess extends L2GameServerPacket
{
	@Override
	protected final void writeImpl()
	{
		writeC(0x1d);
	}
}