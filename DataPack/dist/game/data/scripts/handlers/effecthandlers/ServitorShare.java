package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectFlag;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Synchronizing effects on player and servitor if one of them gets removed for some reason the same will happen to another.
 * PkElfo
 */
public class ServitorShare extends L2Effect
{
	public ServitorShare(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public void onExit()
	{
		L2Effect[] effects = null;
		if (getEffected().isPlayer())
		{
			L2Summon summon = getEffector().getSummon();
			if ((summon != null) && summon.isServitor())
			{
				effects = summon.getAllEffects();
			}
		}
		else if (getEffected().isServitor())
		{
			L2PcInstance owner = getEffected().getActingPlayer();
			if (owner != null)
			{
				effects = owner.getAllEffects();
			}
		}
		
		if (effects != null)
		{
			for (L2Effect eff : effects)
			{
				if (eff.getSkill().getId() == getSkill().getId())
				{
					eff.exit();
					break;
				}
			}
		}
		super.onExit();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public boolean canBeStolen()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.SERVITOR_SHARE.getMask();
	}
}