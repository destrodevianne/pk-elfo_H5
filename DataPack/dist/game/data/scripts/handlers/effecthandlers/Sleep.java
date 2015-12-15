package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class Sleep extends L2Effect
{
	public Sleep(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SLEEP;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startSleeping();
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopSleeping(false);
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
		return EffectFlag.SLEEP.getMask();
	}
}