package pk.elfo.gameserver.network.serverpackets;

/**
 * Format: (chd)
 */
public class ExCubeGameRequestReady extends L2GameServerPacket
{
	/**
	 * Show Confirm Dialog for 10 seconds
	 */
	public ExCubeGameRequestReady()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x97);
		writeD(0x04);
	}
}