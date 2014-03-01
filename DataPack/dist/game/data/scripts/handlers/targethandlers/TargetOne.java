package handlers.targethandlers;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */

public class TargetOne implements ITargetTypeHandler
{
	
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		boolean canTargetSelf = skill.getId() == 1335; // TODO: Unhardcode Balance Life
		switch (skill.getSkillType())
		{
			case BUFF:
			case HEAL:
			case HOT:
			case HEAL_PERCENT:
			case MANARECHARGE:
			case MANA_BY_LEVEL:
			case MANAHEAL:
			case NEGATE:
			case CANCEL_DEBUFF:
			case COMBATPOINTHEAL:
			case HPMPCPHEAL_PERCENT:
			case HPMPHEAL_PERCENT:
			case HPCPHEAL_PERCENT:
			case DUMMY:
				canTargetSelf = true;
				break;
			default:
				break;
		}
		
		// Check for null target or any other invalid target
		if ((target == null) || target.isDead() || ((target == activeChar) && !canTargetSelf))
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return _emptyTargetList;
		}
		
		// If a target is found, return it in a table else send a system message TARGET_IS_INCORRECT
		return new L2Character[]
		{
			target
		};
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_ONE;
	}
}