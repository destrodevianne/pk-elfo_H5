package handlers.effecthandlers;

import pk.elfo.gameserver.ai.CtrlEvent;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class Spoil extends L2Effect
{
	public Spoil(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.SPOIL;
	}
	
	@Override
	public boolean onStart()
	{
		
		if (!getEffector().isPlayer())
		{
			return false;
		}
		
		if (!getEffected().isMonster())
		{
			return false;
		}
		
		L2MonsterInstance target = (L2MonsterInstance) getEffected();
		
		if (target == null)
		{
			return false;
		}
		
		if (target.isSpoil())
		{
			getEffector().sendPacket(SystemMessageId.ALREADY_SPOILED);
			return false;
		}
		
		boolean spoil = false;
		if (target.isDead() == false)
		{
			spoil = Formulas.calcMagicSuccess(getEffector(), target, getSkill());
			
			if (spoil)
			{
				target.setSpoil(true);
				target.setIsSpoiledBy(getEffector().getObjectId());
				getEffector().sendPacket(SystemMessageId.SPOIL_SUCCESS);
			}
			target.getAI().notifyEvent(CtrlEvent.EVT_ATTACKED, getEffector());
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}