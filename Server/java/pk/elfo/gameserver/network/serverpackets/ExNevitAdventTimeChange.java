package pk.elfo.gameserver.network.serverpackets;

public class ExNevitAdventTimeChange extends L2GameServerPacket
{
	private final boolean _paused;
	private final int _time;
	
	public ExNevitAdventTimeChange(int time, boolean paused)
	{
		_time = time;
		_paused = paused;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xE1);
		// state 0 - pause 1 - started
		writeC(_paused ? 0x00 : 0x01);
		// left time in ms max is 16000 its 4m and state is automatically changed to quit
		writeD(_time);
	}
}