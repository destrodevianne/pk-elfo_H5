package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */

public class ChangeHairStyle extends L2Effect
{
	public ChangeHairStyle(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onStart()
	{
		if ((getEffector() == null) || (getEffected() == null) || !getEffector().isPlayer() || !getEffected().isPlayer() || getEffected().isAlikeDead())
		{
			return false;
		}
		
		final L2PcInstance player = getEffector().getActingPlayer();
		player.getAppearance().setHairStyle(getSkill().getHairStyleId());
		player.broadcastUserInfo();
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
}