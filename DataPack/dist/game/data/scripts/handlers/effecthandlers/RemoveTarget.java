package handlers.effecthandlers;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class RemoveTarget extends L2Effect
{
	public RemoveTarget(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.REMOVE_TARGET;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().setTarget(null);
		getEffected().abortAttack();
		getEffected().abortCast();
		getEffected().getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, getEffector());
		return true;
	}
	
	@Override
	public void onExit()
	{
		// nothing
	}
	
	@Override
	public boolean onActionTime()
	{
		// nothing
		return false;
	}
}