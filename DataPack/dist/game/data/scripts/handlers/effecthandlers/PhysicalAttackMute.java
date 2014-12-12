package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class PhysicalAttackMute extends L2Effect
{
	public PhysicalAttackMute(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PHYSICAL_ATTACK_MUTE;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startPhysicalAttackMuted();
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		// Simply stop the effect
		return false;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopPhysicalAttackMuted(this);
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.PSYCHICAL_ATTACK_MUTED.getMask();
	}
}