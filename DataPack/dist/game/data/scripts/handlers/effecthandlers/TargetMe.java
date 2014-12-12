package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2SiegeSummonInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.serverpackets.MyTargetSelected;

/**
 * Projeto PkElfo
 */
 
public class TargetMe extends L2Effect
{
	public TargetMe(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.TARGET_ME;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isPlayable())
		{
			if (getEffected() instanceof L2SiegeSummonInstance)
			{
				return false;
			}
			
			if (getEffected().getTarget() != getEffector())
			{
				L2PcInstance effector = getEffector().getActingPlayer();
				// If effector is null, then its not a player, but NPC. If its not null, then it should check if the skill is pvp skill.
				if ((effector == null) || effector.checkPvpSkill(getEffected(), getSkill()))
				{
					// Target is different
					getEffected().setTarget(getEffector());
					if (getEffected().isPlayer())
					{
						getEffected().sendPacket(new MyTargetSelected(getEffector().getObjectId(), 0));
					}
				}
			}
			((L2Playable) getEffected()).setLockedTarget(getEffector());
			return true;
		}
		else if (getEffected().isL2Attackable() && !getEffected().isRaid())
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected().isPlayable())
		{
			((L2Playable) getEffected()).setLockedTarget(null);
		}
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}