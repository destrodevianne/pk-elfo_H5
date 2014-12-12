package handlers.targethandlers;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.zone.ZoneId;

/**
 * Projeto PkElfo
 */

public class TargetEnemySummon implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if (target.isSummon())
		{
			L2Summon targetSummon = (L2Summon) target;
			if ((activeChar.isPlayer() && (activeChar.getSummon() != targetSummon) && !targetSummon.isDead() && ((targetSummon.getOwner().getPvpFlag() != 0) || (targetSummon.getOwner().getKarma() > 0))) || (targetSummon.getOwner().isInsideZone(ZoneId.PVP) && activeChar.getActingPlayer().isInsideZone(ZoneId.PVP)) || (targetSummon.getOwner().isInDuel() && activeChar.getActingPlayer().isInDuel() && (targetSummon.getOwner().getDuelId() == activeChar.getActingPlayer().getDuelId())))
			{
				return new L2Character[]
				{
					targetSummon
				};
			}
		}
		return _emptyTargetList;
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_ENEMY_SUMMON;
	}
}