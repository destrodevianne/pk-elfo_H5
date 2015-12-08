package pk.elfo.gameserver.network.serverpackets;

public class ExCubeGameCloseUI extends L2GameServerPacket
{
	/**
	 * Close Minigame Waiting List
	 */
	public ExCubeGameCloseUI()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x97);
		writeD(0xffffffff);
	}
}