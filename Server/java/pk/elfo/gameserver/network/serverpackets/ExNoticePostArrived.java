package pk.elfo.gameserver.network.serverpackets;

public class ExNoticePostArrived extends L2GameServerPacket
{
	private static final ExNoticePostArrived STATIC_PACKET_TRUE = new ExNoticePostArrived(true);
	private static final ExNoticePostArrived STATIC_PACKET_FALSE = new ExNoticePostArrived(false);
	
	public static final ExNoticePostArrived valueOf(boolean result)
	{
		return result ? STATIC_PACKET_TRUE : STATIC_PACKET_FALSE;
	}
	
	private final boolean _showAnim;
	
	public ExNoticePostArrived(boolean showAnimation)
	{
		_showAnim = showAnimation;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xA9);
		writeD(_showAnim ? 0x01 : 0x00);
	}
}