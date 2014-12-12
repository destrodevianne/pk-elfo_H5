package handlers.effecthandlers;

import pk.elfo.gameserver.ai.CtrlEvent;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2EffectPointInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class SignetAntiSummon extends L2Effect
{
	private L2EffectPointInstance _actor;
	
	public SignetAntiSummon(Env env, EffectTemplate template)
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
		int mpConsume = getSkill().getMpConsume();
		
		L2PcInstance caster = getEffector().getActingPlayer();
		
		for (L2Character cha : _actor.getKnownList().getKnownCharactersInRadius(getSkill().getSkillRadius()))
		{
			if (cha == null)
			{
				continue;
			}
			
			if (cha.isPlayable())
			{
				if (caster.canAttackCharacter(cha))
				{
					L2PcInstance owner = null;
					if (cha.isSummon())
					{
						owner = ((L2Summon) cha).getOwner();
					}
					else
					{
						owner = cha.getActingPlayer();
					}
					
					if ((owner != null) && owner.hasSummon())
					{
						if (mpConsume > getEffector().getCurrentMp())
						{
							getEffector().sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
							return false;
						}
						
						getEffector().reduceCurrentMp(mpConsume);
						owner.getSummon().unSummon(owner);
						owner.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, getEffector());
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