package pk.elfo.gameserver.network.serverpackets;

public class ExCubeGameEnd extends L2GameServerPacket
{
	boolean _isRedTeamWin;
	
	/**
	 * Show Minigame Results
	 * @param isRedTeamWin Is Red Team Winner?
	 */
	public ExCubeGameEnd(boolean isRedTeamWin)
	{
		_isRedTeamWin = isRedTeamWin;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x98);
		writeD(0x01);
		
		writeD(_isRedTeamWin ? 0x01 : 0x00);
	}
}