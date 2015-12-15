package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */

public class CrystalGradeModify extends L2Effect
{
	public CrystalGradeModify(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BUFF;
	}
	
	@Override
	public boolean onStart()
	{
		final L2PcInstance player = getEffected().getActingPlayer();
		if (player != null)
		{
			player.setExpertisePenaltyBonus((int) calc());
			return true;
		}
		return false;
	}
	
	@Override
	public void onExit()
	{
		final L2PcInstance player = getEffected().getActingPlayer();
		if (player != null)
		{
			player.setExpertisePenaltyBonus(0);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}