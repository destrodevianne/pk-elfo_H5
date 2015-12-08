package pk.elfo.gameserver.network.communityserver.readpackets;

import java.util.logging.Logger;

import org.netcon.BaseReadPacket;

import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.network.communityserver.CommunityServerThread;
import pk.elfo.gameserver.network.communityserver.writepackets.WorldInfo;

public final class ClanNoticeInfo extends BaseReadPacket
{
	private static Logger _log = Logger.getLogger(ClanNoticeInfo.class.getName());
	private final int _type;
	private final CommunityServerThread _cst;
	
	public ClanNoticeInfo(final byte[] data, int type)
	{
		super(data);
		_cst = null;
		_type = type;
	}
	
	public ClanNoticeInfo(final byte[] data, final CommunityServerThread cst, int type)
	{
		super(data);
		_type = type;
		_cst = cst;
	}
	
	@Override
	public final void run()
	{
		switch (_type)
		{
			case 0:
				int clanId = super.readD();
				L2Clan c = ClanTable.getInstance().getClan(clanId);
				String notice = super.readS();
				c.setNotice(notice);
				boolean noticeEnabled = (super.readC() == 1 ? true : false);
				c.setNoticeEnabled(noticeEnabled);
				break;
			case 1:
				clanId = super.readD();
				c = ClanTable.getInstance().getClan(clanId);
				noticeEnabled = (super.readC() == 1 ? true : false);
				c.setNoticeEnabled(noticeEnabled);
				break;
			case 2:
				clanId = super.readD();
				L2Clan clan = ClanTable.getInstance().getClan(clanId);
				if (clan != null)
				{
					_cst.sendPacket(new WorldInfo(null, clan, WorldInfo.TYPE_SEND_CLAN_NOTICE));
				}
				else
				{
					_log.warning("Can't find clan with id: " + clanId);
				}
				break;
		}
	}
}