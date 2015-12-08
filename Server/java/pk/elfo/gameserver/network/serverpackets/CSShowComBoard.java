package pk.elfo.gameserver.network.serverpackets;

public final class CSShowComBoard extends L2GameServerPacket
{
	private final byte[] _html;
	
	public CSShowComBoard(final byte[] html)
	{
		_html = html;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0x7b);
		writeC(0x01); // c4 1 to show community 00 to hide
		writeB(_html);
	}
}