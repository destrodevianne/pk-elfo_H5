package pk.elfo.gameserver.network.serverpackets;

public class Ex2ndPasswordCheck extends L2GameServerPacket
{
	public static final int PASSWORD_NEW = 0x00;
	public static final int PASSWORD_PROMPT = 0x01;
	public static final int PASSWORD_OK = 0x02;
	
	private final int _windowType;
	
	public Ex2ndPasswordCheck(int windowType)
	{
		_windowType = windowType;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		// writeH(0x109); GOD
		writeH(0xe5);
		writeD(_windowType);
		writeD(0x00);
	}
}