package pk.elfo.gameserver.network.serverpackets;

public class CharCreateOk extends L2GameServerPacket
{
	@Override
	protected final void writeImpl()
	{
		writeC(0x0f);
		writeD(0x01);
	}
}