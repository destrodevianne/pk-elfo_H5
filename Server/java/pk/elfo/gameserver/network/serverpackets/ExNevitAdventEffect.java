package pk.elfo.gameserver.network.serverpackets;

public class ExNevitAdventEffect extends L2GameServerPacket
{
	private final int _timeLeft;
	
	public ExNevitAdventEffect(int timeLeft)
	{
		_timeLeft = timeLeft;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xE0);
		writeD(_timeLeft);
	}
}