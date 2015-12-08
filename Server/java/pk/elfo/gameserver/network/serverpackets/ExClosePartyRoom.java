package pk.elfo.gameserver.network.serverpackets;

public class ExClosePartyRoom extends L2GameServerPacket
{
	public ExClosePartyRoom()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x09);
	}
}