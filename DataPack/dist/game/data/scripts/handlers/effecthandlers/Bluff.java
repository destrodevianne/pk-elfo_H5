package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2NpcInstance;
import pk.elfo.gameserver.model.actor.instance.L2SiegeSummonInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.serverpackets.StartRotation;
import pk.elfo.gameserver.network.serverpackets.StopRotation;

/**
 * Projeto PkElfo
 */

public class Bluff extends L2Effect
{
	public Bluff(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.BLUFF; // test for bluff effect
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof L2NpcInstance)
		{
			return false;
		}
		
		if ((getEffected() instanceof L2Npc) && (((L2Npc) getEffected()).getNpcId() == 35062))
		{
			return false;
		}
		
		if (getEffected() instanceof L2SiegeSummonInstance)
		{
			return false;
		}
		
		getEffected().broadcastPacket(new StartRotation(getEffected().getObjectId(), getEffected().getHeading(), 1, 65535));
		getEffected().broadcastPacket(new StopRotation(getEffected().getObjectId(), getEffector().getHeading(), 65535));
		getEffected().setHeading(getEffector().getHeading());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}