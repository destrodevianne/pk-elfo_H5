package pk.elfo.gameserver.network.serverpackets;

/**
 * Close the CommandChannel Information window
 */
public class ExCloseMPCC extends L2GameServerPacket
{
	public ExCloseMPCC()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xfe);
		writeH(0x13);
	}
}