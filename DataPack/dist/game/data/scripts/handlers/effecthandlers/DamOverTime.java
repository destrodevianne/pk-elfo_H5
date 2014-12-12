package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */

public class DamOverTime extends L2Effect
{
	public DamOverTime(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.DMG_OVER_TIME;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
		{
			return false;
		}
		
		double damage = calc();
		if (damage >= (getEffected().getCurrentHp() - 1))
		{
			if (getSkill().isToggle())
			{
				getEffected().sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_HP);
				return false;
			}
			
			// For DOT skills that will not kill effected player.
			if (!getSkill().killByDOT())
			{
				// Fix for players dying by DOTs if HP < 1 since reduceCurrentHP method will kill them
				if (getEffected().getCurrentHp() <= 1)
				{
					return false;
				}
				
				damage = getEffected().getCurrentHp() - 1;
			}
		}
		getEffected().reduceCurrentHpByDOT(damage, getEffector(), getSkill());
		
		return getSkill().isToggle();
	}
}