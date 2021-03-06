package pk.elfo.gameserver.network.communityserver.writepackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastList;

import org.netcon.BaseWritePacket;

import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.entity.Castle;

public final class InitWorldInfo extends BaseWritePacket
{
	public static final byte TYPE_INFO = 0;
	public static final byte TYPE_PLAYER = 1;
	public static final byte TYPE_CLAN = 2;
	public static final byte TYPE_CASTLE = 3;
	private static Logger _log = Logger.getLogger(InitWorldInfo.class.getName());
	
	public InitWorldInfo(StatsSet[] players, L2Clan[] clans, final byte type, int info)
	{
		super.writeC(0x01);
		super.writeC(0x00);
		super.writeD(type);
		
		int i;
		switch (type)
		{
			case TYPE_INFO:
				super.writeD(info);
				break;
			case TYPE_CLAN:
				super.writeD(info != -1 ? info : clans.length);
				i = 0;
				for (L2Clan c : clans)
				{
					if (c == null)
					{
						continue;
					}
					if (i++ == info)
					{
						break;
					}
					super.writeD(c.getClanId());
					super.writeS(c.getName());
					super.writeD(c.getLevel());
					if (c.getLeader() == null)
					{
						writeD(0);
						writeS("");
						_log.info("Clan Id: " + c.getClanId() + " has null clan leader!");
					}
					else
					{
						super.writeD(c.getLeader().getObjectId());
						super.writeS(c.getLeader().getName());
					}
					super.writeD(c.getMembersCount());
					super.writeC((c.isNoticeEnabled() ? 1 : 0));
					// Alliance info:
					super.writeS(c.getAllyName());
					final List<L2Clan> clanAllies = ClanTable.getInstance().getClanAllies(c.getAllyId());
					super.writeD(clanAllies.size());
					for (L2Clan allies : clanAllies)
					{
						super.writeD(allies.getClanId());
					}
				}
				break;
			case TYPE_PLAYER:
				super.writeD(info != -1 ? info : players.length);
				i = 0;
				for (StatsSet p : players)
				{
					if (i++ == info)
					{
						break;
					}
					super.writeD(p.getInteger("charId"));
					super.writeS(p.getString("char_name"));
					super.writeS(p.getString("account_name"));
					super.writeD(p.getInteger("level"));
					super.writeD(p.getInteger("clanid"));
					super.writeD(p.getInteger("accesslevel"));
					super.writeC(p.getInteger("online"));
					FastList<Integer> list = FastList.newInstance();
					try (Connection con = L2DatabaseFactory.getInstance().getConnection();
						PreparedStatement statement = con.prepareStatement("SELECT friendId FROM character_friends WHERE charId=?"))
					{
						statement.setInt(1, p.getInteger("charId"));
						try (ResultSet rset = statement.executeQuery())
						{
							while (rset.next())
							{
								list.add(rset.getInt("friendId"));
							}
						}
					}
					catch (Exception e)
					{
						_log.log(Level.SEVERE, "Error restoring friend data for Community Board transfer.", e);
					}
					super.writeD(list.size());
					for (int j : list)
					{
						super.writeD(j);
					}
					FastList.recycle(list);
				}
				break;
			case TYPE_CASTLE:
				List<Castle> castles = CastleManager.getInstance().getCastles();
				writeD(castles.size());
				_log.info("Transfering " + castles.size() + " castles data to CB server.");
				for (Castle castle : castles)
				{
					writeD(castle.getCastleId());
					writeS(castle.getName());
					writeD(castle.getOwnerId());
					writeD(castle.getTaxPercent());
					writeD((int) (castle.getSiege().getSiegeDate().getTimeInMillis() / 1000));
				}
				break;
		}
	}
}