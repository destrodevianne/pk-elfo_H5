package pk.elfo.gameserver.network.serverpackets;

public class ExCubeGameChangeTimeToStart extends L2GameServerPacket
{
	int _seconds;
	
	/**
	 * Update Minigame Waiting List Time to Start
	 * @param seconds
	 */
	public ExCubeGameChangeTimeToStart(int seconds)
	{
		_seconds = seconds;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x97);
		writeD(0x03);
		
		writeD(_seconds);
	}
}