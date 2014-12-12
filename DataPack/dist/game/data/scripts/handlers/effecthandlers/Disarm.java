package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */

public class Disarm extends L2Effect
{
	public Disarm(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.DISARM;
	}
	
	@Override
	public boolean onStart()
	{
		if (!getEffected().isPlayer())
		{
			return false;
		}
		
		getEffected().getActingPlayer().disarmWeapons();
		return true;
		
	}
	
	@Override
	public void onExit()
	{
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.DISARMED.getMask();
	}
}