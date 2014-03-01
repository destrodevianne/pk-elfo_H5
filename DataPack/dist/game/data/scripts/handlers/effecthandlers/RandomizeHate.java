package handlers.effecthandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.util.Rnd;
import javolution.util.FastList;

/**
 * PkElfo
 */
 
public class RandomizeHate extends L2Effect
{
	public RandomizeHate(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.RANDOMIZE_HATE;
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffected() == null) || (getEffected() == getEffector()))
		{
			return false;
		}
		
		// Effect is for mobs only.
		if (!getEffected().isL2Attackable())
		{
			return false;
		}
		
		L2Attackable effectedMob = (L2Attackable) getEffected();
		
		List<L2Character> targetList = new FastList<>();
		
		// Getting the possible targets
		
		Collection<L2Character> chars = getEffected().getKnownList().getKnownCharacters();
		for (L2Character cha : chars)
		{
			if ((cha != null) && (cha != effectedMob) && (cha != getEffector()))
			{
				// Aggro cannot be transfered to a mob of the same faction.
				if (cha.isL2Attackable() && (((L2Attackable) cha).getFactionId() != null) && ((L2Attackable) cha).getFactionId().equals(effectedMob.getFactionId()))
				{
					continue;
				}
				
				targetList.add(cha);
			}
		}
		// if there is no target, exit function
		if (targetList.isEmpty())
		{
			return true;
		}
		
		// Choosing randomly a new target
		final L2Character target = targetList.get(Rnd.get(targetList.size()));
		
		final int hate = effectedMob.getHating(getEffector());
		effectedMob.stopHating(getEffector());
		effectedMob.addDamageHate(target, 0, hate);
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}