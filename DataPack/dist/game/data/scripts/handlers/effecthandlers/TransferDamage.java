package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class TransferDamage extends L2Effect
{
	public TransferDamage(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	public TransferDamage(Env env, L2Effect effect)
	{
		super(env, effect);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.DAMAGE_TRANSFER;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isPlayable() && getEffector().isPlayer())
		{
			((L2Playable) getEffected()).setTransferDamageTo(getEffector().getActingPlayer());
		}
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected().isPlayable() && getEffector().isPlayer())
		{
			((L2Playable) getEffected()).setTransferDamageTo(null);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}