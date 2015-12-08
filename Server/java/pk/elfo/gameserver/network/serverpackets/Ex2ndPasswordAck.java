package pk.elfo.gameserver.network.serverpackets;

public class Ex2ndPasswordAck extends L2GameServerPacket
{
	int _response;
	
	public static int SUCCESS = 0x00;
	public static int WRONG_PATTERN = 0x01;
	
	public Ex2ndPasswordAck(int response)
	{
		_response = response;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		// writeH(0x109); GOD
		writeH(0xE7);
		writeC(0x00);
		writeD(_response == WRONG_PATTERN ? 0x01 : 0x00);
		writeD(0x00);
	}
}