package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class ProtectionBlessing extends L2Effect
{
	public ProtectionBlessing(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PROTECTION_BLESSING;
	}
	
	/** Notify started */
	@Override
	public boolean onStart()
	{
		return false;
	}
	
	/** Notify exited */
	@Override
	public void onExit()
	{
		((L2Playable) getEffected()).stopProtectionBlessing(this);
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
		return EffectFlag.PROTECTION_BLESSING.getMask();
	}
}