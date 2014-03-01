package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */
 
public class UnsummonAgathion extends SummonAgathion
{
	public UnsummonAgathion(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	protected void setAgathionId(L2PcInstance player)
	{
		player.setAgathionId(0);
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.UNSUMMON_AGATHION;
	}
}