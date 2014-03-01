package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class DispelBySlot extends L2Effect
{
	public DispelBySlot(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.NEGATE;
	}
	
	@Override
	public boolean onStart()
	{
		L2Character target = getEffected();
		if ((target == null) || target.isDead())
		{
			return false;
		}
		
		String stackType = getAbnormalType();
		float stackOrder = getAbnormalLvl();
		int skillCast = getSkill().getId();
		
		// If order is 0 don't remove effect
		if (stackOrder == 0)
		{
			return true;
		}
		
		final L2Effect[] effects = target.getAllEffects();
		
		for (L2Effect e : effects)
		{
			if (!e.getSkill().canBeDispeled())
			{
				continue;
			}
			
			// Fist check for stacktype
			if (stackType.equalsIgnoreCase(e.getAbnormalType()) && (e.getSkill().getId() != skillCast))
			{
				if (stackOrder == -1)
				{
					e.exit();
				}
				else if (stackOrder >= e.getAbnormalLvl())
				{
					e.exit();
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}