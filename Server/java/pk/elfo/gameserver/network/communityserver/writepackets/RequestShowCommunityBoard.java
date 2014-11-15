package pk.elfo.gameserver.network.communityserver.writepackets;

import org.netcon.BaseWritePacket;

public final class RequestShowCommunityBoard extends BaseWritePacket
{
	public RequestShowCommunityBoard(final int playerObjId, final String cmd)
	{
		super.writeC(0x02);
		super.writeC(0x00);
		super.writeD(playerObjId);
		super.writeS(cmd);
	}
}