package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2FortBallistaInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class BallistaBomb implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.BALLISTA
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2Object[] targetList = skill.getTargetList(activeChar);
		
		if ((targetList == null) || (targetList.length == 0))
		{
			return;
		}
		L2Character target = (L2Character) targetList[0];
		if (target instanceof L2FortBallistaInstance)
		{
			if (Rnd.get(3) == 0)
			{
				target.setIsInvul(false);
				target.reduceCurrentHp(target.getMaxHp() + 1, activeChar, skill);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}