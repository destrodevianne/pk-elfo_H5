package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class ImmobileBuff extends Buff
{
	public ImmobileBuff(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	// Special constructor to steal this effect
	public ImmobileBuff(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().setIsImmobilized(true);
		return super.onStart();
	}
	
	@Override
	public void onExit()
	{
		getEffected().setIsImmobilized(false);
		super.onExit();
	}
	
	@Override
	public boolean onActionTime()
	{
		// just stop this effect
		return false;
	}
}