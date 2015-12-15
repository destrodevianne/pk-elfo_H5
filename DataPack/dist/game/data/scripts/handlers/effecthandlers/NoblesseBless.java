package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class NoblesseBless extends L2Effect
{
	public NoblesseBless(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	// Special constructor to steal this effect
	public NoblesseBless(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	protected boolean effectCanBeStolen()
	{
		return true;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.NOBLESSE_BLESSING;
	}
	
	@Override
	public boolean onStart()
	{
		return true;
	}
	
	@Override
	public void onExit()
	{
	}
	
	@Override
	public boolean onActionTime()
	{
		// just stop this effect
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.NOBLESS_BLESSING.getMask();
	}
}