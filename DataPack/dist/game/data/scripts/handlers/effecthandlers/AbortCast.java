package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */

public class AbortCast extends L2Effect
{
	public AbortCast(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.ABORT_CAST;
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffected() == null) || (getEffected() == getEffector()))
		{
			return false;
		}
		
		if (getEffected().isRaid())
		{
			return false;
		}
		
		getEffected().breakCast();
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}