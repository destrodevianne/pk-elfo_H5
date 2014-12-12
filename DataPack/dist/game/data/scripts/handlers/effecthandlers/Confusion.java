package handlers.effecthandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.util.Rnd;
import javolution.util.FastList;

/**
 * Implementation of the Confusion Effect.<br>
 * Zoey76: TODO: Review onActionTime() method.
 * Projeto PkElfo
 */
public class Confusion extends L2Effect
{
	
	public Confusion(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.CONFUSION;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startConfused();
		onActionTime();
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopConfused(this);
	}
	
	@Override
	public boolean onActionTime()
	{
		List<L2Character> targetList = new FastList<>();
		
		// Getting the possible targets
		
		Collection<L2Object> objs = getEffected().getKnownList().getKnownObjects().values();
		// synchronized (getEffected().getKnownList().getKnownObjects())
		{
			for (L2Object obj : objs)
			{
				if ((obj instanceof L2Character) && (obj != getEffected()))
				{
					targetList.add((L2Character) obj);
				}
			}
		}
		// if there is no target, exit function
		if (targetList.isEmpty())
		{
			return true;
		}
		
		// Choosing randomly a new target
		int nextTargetIdx = Rnd.nextInt(targetList.size());
		L2Object target = targetList.get(nextTargetIdx);
		
		// Attacking the target
		getEffected().setTarget(target);
		getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
		
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.CONFUSED.getMask();
	}
}