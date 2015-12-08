package pk.elfo.gameserver.network.serverpackets;

import java.util.Collection;

import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.model.ClanInfo;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.network.clientpackets.RequestAllyInfo;

/**
 * Sent in response to {@link RequestAllyInfo}, if applicable.<BR>
 */
public class AllianceInfo extends L2GameServerPacket
{
	private final String _name;
	private final int _total;
	private final int _online;
	private final String _leaderC;
	private final String _leaderP;
	private final ClanInfo[] _allies;
	
	public AllianceInfo(int allianceId)
	{
		final L2Clan leader = ClanTable.getInstance().getClan(allianceId);
		_name = leader.getAllyName();
		_leaderC = leader.getName();
		_leaderP = leader.getLeaderName();
		
		final Collection<L2Clan> allies = ClanTable.getInstance().getClanAllies(allianceId);
		_allies = new ClanInfo[allies.size()];
		int idx = 0, total = 0, online = 0;
		for (final L2Clan clan : allies)
		{
			final ClanInfo ci = new ClanInfo(clan);
			_allies[idx++] = ci;
			total += ci.getTotal();
			online += ci.getOnline();
		}
		_total = total;
		_online = online;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xB5);
		
		writeS(_name);
		writeD(_total);
		writeD(_online);
		writeS(_leaderC);
		writeS(_leaderP);
		
		writeD(_allies.length);
		for (final ClanInfo aci : _allies)
		{
			writeS(aci.getClan().getName());
			writeD(0x00);
			writeD(aci.getClan().getLevel());
			writeS(aci.getClan().getLeaderName());
			writeD(aci.getTotal());
			writeD(aci.getOnline());
		}
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getTotal()
	{
		return _total;
	}
	
	public int getOnline()
	{
		return _online;
	}
	
	public String getLeaderC()
	{
		return _leaderC;
	}
	
	public String getLeaderP()
	{
		return _leaderP;
	}
	
	public ClanInfo[] getAllies()
	{
		return _allies;
	}
}