package pk.elfo.gameserver.network.serverpackets;

/**
 * (just a trigger)
 */
public class ExMailArrived extends L2GameServerPacket
{
	public static final ExMailArrived STATIC_PACKET = new ExMailArrived();
	
	private ExMailArrived()
	{
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x2E);
	}
}
