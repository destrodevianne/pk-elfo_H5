package handlers.skillhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * Projeto PkElfo
 */
 
public class Charge implements ISkillHandler
{
	static Logger _log = Logger.getLogger(Charge.class.getName());
	
	private static final L2SkillType[] SKILL_IDS = {/* L2SkillType.CHARGE */};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		
		for (L2Object target : targets)
		{
			if (!target.isPlayer())
			{
				continue;
			}
			skill.getEffects(activeChar, target.getActingPlayer());
		}
		
		// self Effect :]
		if (skill.hasSelfEffects())
		{
			final L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if ((effect != null) && effect.isSelfEffect())
			{
				// Replace old effect with new one.
				effect.exit();
			}
			skill.getEffectsSelf(activeChar);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}