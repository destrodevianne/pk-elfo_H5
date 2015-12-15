package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2EffectPointInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class SignetNoise extends L2Effect
{
	private L2EffectPointInstance _actor;
	
	public SignetNoise(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SIGNET_GROUND;
	}
	
	@Override
	public boolean onStart()
	{
		_actor = (L2EffectPointInstance) getEffected();
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getCount() == (getTotalCount() - 1))
		{
			return true; // do nothing first time
		}
		
		L2PcInstance caster = getEffector().getActingPlayer();
		
		for (L2Character target : _actor.getKnownList().getKnownCharactersInRadius(getSkill().getSkillRadius()))
		{
			if ((target == null) || (target == caster))
			{
				continue;
			}
			
			if (caster.canAttackCharacter(target))
			{
				L2Effect[] effects = target.getAllEffects();
				if (effects != null)
				{
					for (L2Effect effect : effects)
					{
						if (effect.getSkill().isDance())
						{
							effect.exit();
						}
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (_actor != null)
		{
			_actor.deleteMe();
		}
	}
}