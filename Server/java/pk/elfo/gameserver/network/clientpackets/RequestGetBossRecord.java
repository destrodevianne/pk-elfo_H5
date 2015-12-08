package pk.elfo.gameserver.network.clientpackets;

import java.util.Map;

import pk.elfo.gameserver.instancemanager.RaidBossPointsManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExGetBossRecord;

/**
 * Format: (ch) d
 */
public class RequestGetBossRecord extends L2GameClientPacket
{
	private static final String _C__D0_40_REQUESTGETBOSSRECORD = "[C] D0:40 RequestGetBossRecord";
	private int _bossId;
	
	@Override
	protected void readImpl()
	{
		_bossId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (_bossId != 0)
		{
			_log.info("C5: RequestGetBossRecord: d: " + _bossId + " ActiveChar: " + activeChar); // should be always 0, log it if isnt 0 for furture research
		}
		
		int points = RaidBossPointsManager.getInstance().getPointsByOwnerId(activeChar.getObjectId());
		int ranking = RaidBossPointsManager.getInstance().calculateRanking(activeChar.getObjectId());
		
		Map<Integer, Integer> list = RaidBossPointsManager.getInstance().getList(activeChar);
		
		// trigger packet
		activeChar.sendPacket(new ExGetBossRecord(ranking, points, list));
	}
	
	@Override
	public String getType()
	{
		return _C__D0_40_REQUESTGETBOSSRECORD;
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}