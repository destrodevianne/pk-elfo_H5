package pk.elfo.gameserver.network.communityserver.writepackets;

import org.netcon.BaseWritePacket;

public final class RequestCommunityBoardWrite extends BaseWritePacket
{
	public RequestCommunityBoardWrite(final int playerObjId, final String url, final String arg1, final String arg2, final String arg3, final String arg4, final String arg5)
	{
		super.writeC(0x02);
		super.writeC(0x01);
		super.writeD(playerObjId);
		super.writeS(url);
		super.writeS(arg1);
		super.writeS(arg2);
		super.writeS(arg3);
		super.writeS(arg4);
		super.writeS(arg5);
	}
}