package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.effects.AbnormalEffect;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */

public class Grow extends L2Effect
{
	public Grow(Env env, EffectTemplate template)
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
		if (getEffected().isNpc())
		{
			L2Npc npc = (L2Npc) getEffected();
			// TODO: Uncomment line when fix for mobs falling underground is found
			// npc.setCollisionHeight((int) (npc.getCollisionHeight() * 1.24));
			npc.setCollisionRadius((npc.getCollisionRadius() * 1.19));
			
			getEffected().startAbnormalEffect(AbnormalEffect.GROW);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected().isNpc())
		{
			L2Npc npc = (L2Npc) getEffected();
			// TODO: Uncomment line when fix for mobs falling underground is found
			// npc.setCollisionHeight(npc.getTemplate().collisionHeight);
			npc.setCollisionRadius(npc.getTemplate().getfCollisionRadius());
			
			getEffected().stopAbnormalEffect(AbnormalEffect.GROW);
		}
	}
}