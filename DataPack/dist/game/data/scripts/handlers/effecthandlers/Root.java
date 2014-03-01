package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class Root extends L2Effect
{
	public Root(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.ROOT;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startRooted();
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopRooting(false);
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
		return EffectFlag.ROOTED.getMask();
	}
}