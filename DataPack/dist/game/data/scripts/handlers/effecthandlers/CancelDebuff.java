package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */

public class CancelDebuff extends L2Effect
{
	public CancelDebuff(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.CANCEL_DEBUFF;
	}
	
	@Override
	public boolean onStart()
	{
		return cancel(getEffector(), getEffected(), getSkill(), getEffectPower());
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	private static boolean cancel(L2Character caster, L2Character target, L2Skill skill, double baseRate)
	{
		if (target.isDead())
		{
			return false;
		}
		
		final int cancelLvl = skill.getMagicLevel();
		int count = skill.getMaxNegatedEffects();
		
		L2Effect effect;
		int lastCanceledSkillId = 0;
		final L2Effect[] effects = target.getAllEffects();
		for (int i = effects.length; --i >= 0;)
		{
			effect = effects[i];
			if (effect == null)
			{
				continue;
			}
			
			if (!effect.getSkill().isDebuff() || !effect.getSkill().canBeDispeled())
			{
				effects[i] = null;
				continue;
			}
			
			if (effect.getSkill().getId() == lastCanceledSkillId)
			{
				effect.exit(); // this skill already canceled
				continue;
			}
			
			if (!calcCancelSuccess(effect, cancelLvl, (int) baseRate))
			{
				continue;
			}
			
			lastCanceledSkillId = effect.getSkill().getId();
			effect.exit();
			count--;
			
			if (count == 0)
			{
				break;
			}
		}
		
		return true;
	}
	
	private static boolean calcCancelSuccess(L2Effect effect, int cancelLvl, int baseRate)
	{
		int rate = 2 * (cancelLvl - effect.getSkill().getMagicLevel());
		rate += (effect.getAbnormalTime() - effect.getTime()) / 1200;
		rate += baseRate;
		
		if (rate < effect.getSkill().getMinChance())
		{
			rate = effect.getSkill().getMinChance();
		}
		else if (rate > effect.getSkill().getMaxChance())
		{
			rate = effect.getSkill().getMaxChance();
		}
		
		return Rnd.get(100) < rate;
	}
}