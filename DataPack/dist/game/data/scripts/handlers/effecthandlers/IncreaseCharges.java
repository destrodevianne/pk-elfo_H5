package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Effect will generate charges for L2PcInstance targets.<br>
 * Number of charges in "value", maximum number in "count" effect variables.
 * PkElfo
 */
public class IncreaseCharges extends L2Effect
{
	public IncreaseCharges(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.INCREASE_CHARGES;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() == null)
		{
			return false;
		}
		
		if (!getEffected().isPlayer())
		{
			return false;
		}
		
		getEffected().getActingPlayer().increaseCharges((int) calc(), getCount());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false; // abort effect even if count > 1
	}
}