package pk.elfo.gameserver.network.communityserver.writepackets;

import org.netcon.BaseWritePacket;

import pk.elfo.Config;

public final class GameServerAuth extends BaseWritePacket
{
	public GameServerAuth()
	{
		super.writeC(0x00);
		super.writeC(0x01);
		super.writeD(Config.COMMUNITY_SERVER_HEX_ID.length);
		super.writeB(Config.COMMUNITY_SERVER_HEX_ID);
		super.writeD(Config.COMMUNITY_SERVER_SQL_DP_ID);
	}
}