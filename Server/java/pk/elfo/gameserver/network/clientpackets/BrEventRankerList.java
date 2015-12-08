package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.network.serverpackets.ExBrLoadEventTopRankers;

/**
 * Halloween rank list client packet. Format: (ch)ddd
 */
public class BrEventRankerList extends L2GameClientPacket
{
	private static final String _C__D0_7B_BREVENTRANKERLIST = "[C] D0:7B BrEventRankerList";
	
	private int _eventId;
	private int _day;
	@SuppressWarnings("unused")
	private int _ranking;
	
	@Override
	protected void readImpl()
	{
		_eventId = readD();
		_day = readD(); // 0 - current, 1 - previous
		_ranking = readD();
	}
	
	@Override
	protected void runImpl()
	{
		// TODO count, bestScore, myScore
		int count = 0;
		int bestScore = 0;
		int myScore = 0;
		getClient().sendPacket(new ExBrLoadEventTopRankers(_eventId, _day, count, bestScore, myScore));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_7B_BREVENTRANKERLIST;
	}
}