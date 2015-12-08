package pk.elfo.gameserver.network.serverpackets;

/**
 * Possibly trigger packet only, need to be verified.
 */
public class ExDominionWarEnd extends L2GameServerPacket
{
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xA4);
	}
}