package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * PkElfo
 */
 
public class ShiftTarget implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SHIFT_TARGET
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (targets == null)
		{
			return;
		}
		L2Character target = (L2Character) targets[0];
		
		if (activeChar.isAlikeDead() || (target == null))
		{
			return;
		}
		
		for (L2Character obj : activeChar.getKnownList().getKnownCharactersInRadius(skill.getSkillRadius()))
		{
			if (!obj.isL2Attackable() || obj.isDead())
			{
				continue;
			}
			L2Attackable hater = ((L2Attackable) obj);
			if (hater.getHating(activeChar) == 0)
			{
				continue;
			}
			hater.addDamageHate(target, 0, hater.getHating(activeChar));
			
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}