package pk.elfo.gameserver.network.serverpackets;

public class ExBirthdayPopup extends L2GameServerPacket
{
	public ExBirthdayPopup()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x8f);
	}
}