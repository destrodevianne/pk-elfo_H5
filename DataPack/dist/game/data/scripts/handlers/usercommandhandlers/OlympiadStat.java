package handlers.usercommandhandlers;

import pk.elfo.gameserver.handler.IUserCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
 
/**
 * Projeto PkElfo
 */

public class OlympiadStat implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		109
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		int nobleObjId = activeChar.getObjectId();
		final L2Object target = activeChar.getTarget();
		if (target != null)
		{
			if (target.isPlayer() && target.getActingPlayer().isNoble())
			{
				nobleObjId = target.getObjectId();
			}
			else
			{
				activeChar.sendPacket(SystemMessageId.NOBLESSE_ONLY);
				return false;
			}
		}
		else if (!activeChar.isNoble())
		{
			activeChar.sendPacket(SystemMessageId.NOBLESSE_ONLY);
			return false;
		}
		
		final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.THE_CURRENT_RECORD_FOR_THIS_OLYMPIAD_SESSION_IS_S1_MATCHES_S2_WINS_S3_DEFEATS_YOU_HAVE_EARNED_S4_OLYMPIAD_POINTS);
		sm.addNumber(Olympiad.getInstance().getCompetitionDone(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getCompetitionWon(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getCompetitionLost(nobleObjId));
		sm.addNumber(Olympiad.getInstance().getNoblePoints(nobleObjId));
		activeChar.sendPacket(sm);
		
		final SystemMessage sm2 = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_S1_MATCHES_REMAINING_THAT_YOU_CAN_PARTECIPATE_IN_THIS_WEEK_S2_CLASSED_S3_NON_CLASSED_S4_TEAM);
		sm2.addNumber(Olympiad.getInstance().getRemainingWeeklyMatches(nobleObjId));
		sm2.addNumber(Olympiad.getInstance().getRemainingWeeklyMatchesClassed(nobleObjId));
		sm2.addNumber(Olympiad.getInstance().getRemainingWeeklyMatchesNonClassed(nobleObjId));
		sm2.addNumber(Olympiad.getInstance().getRemainingWeeklyMatchesTeam(nobleObjId));
		activeChar.sendPacket(sm2);
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}