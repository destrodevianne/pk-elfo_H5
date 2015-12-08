package pk.elfo.gameserver.network.communityserver.writepackets;

import java.util.List;

import javolution.util.FastList;

import org.netcon.BaseWritePacket;

import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public final class WorldInfo extends BaseWritePacket
{
	public static final byte TYPE_INIT_PACKET = 0;
	public static final byte TYPE_UPDATE_PLAYER_DATA = 1;
	public static final byte TYPE_UPDATE_PLAYER_STATUS = 2;
	public static final byte TYPE_UPDATE_CLAN_DATA = 3;
	public static final byte TYPE_SEND_CLAN_NOTICE = 4;
	
	public WorldInfo(L2PcInstance player, L2Clan clan, final byte type)
	{
		super.writeC(0x01);
		
		switch (type)
		{
			case TYPE_INIT_PACKET:
			{
				// this should never happen
				super.writeC(0x00);
				break;
			}
			
			case TYPE_UPDATE_PLAYER_DATA:
			{
				super.writeC(0x01);
				super.writeC(0x00);
				super.writeD(player.getObjectId());
				super.writeS(player.getName());
				super.writeS(player.getAccountName());
				super.writeD(player.getLevel());
				super.writeD(player.getClanId());
				super.writeD(player.getAccessLevel().getLevel());
				super.writeC(player.isOnlineInt());
				List<Integer> list = player.getFriendList();
				super.writeD(list.size());
				for (int j : list)
				{
					super.writeD(j);
				}
				break;
			}
			
			case TYPE_UPDATE_PLAYER_STATUS:
			{
				super.writeC(0x01);
				super.writeC(0x01);
				super.writeD(player.getObjectId());
				super.writeC(player.isOnlineInt());
				break;
			}
			
			case TYPE_UPDATE_CLAN_DATA:
			{
				super.writeC(0x02);
				super.writeD(clan.getClanId());
				super.writeS(clan.getName());
				super.writeD(clan.getLevel());
				super.writeD(clan.getLeader().getObjectId());
				super.writeS(clan.getLeader().getName());
				super.writeD(clan.getMembersCount());
				super.writeC((clan.isNoticeEnabled() ? 1 : 0));
				super.writeS(clan.getAllyName());
				FastList<Integer> allyClanIdList = FastList.newInstance();
				if (clan.getAllyId() != 0)
				{
					for (L2Clan c : ClanTable.getInstance().getClanAllies(clan.getAllyId()))
					{
						allyClanIdList.add(c.getClanId());
					}
				}
				super.writeD(allyClanIdList.size());
				for (int k : allyClanIdList)
				{
					super.writeD(k);
				}
				FastList.recycle(allyClanIdList);
				break;
			}
			case TYPE_SEND_CLAN_NOTICE:
				super.writeC(0x03);
				super.writeD(clan.getClanId());
				super.writeS(clan.getNotice());
				break;
		}
	}
}