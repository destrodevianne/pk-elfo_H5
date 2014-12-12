package handlers.effecthandlers;

import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */
 
public class RebalanceHP extends L2Effect
{
	public RebalanceHP(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.REBALANCE_HP;
	}
	
	@Override
	public boolean onStart()
	{
		if (!getEffector().isPlayer() || !getEffector().isInParty())
		{
			return false;
		}
		
		double fullHP = 0;
		double currentHPs = 0;
		final L2Party party = getEffector().getParty();
		for (L2PcInstance member : party.getMembers())
		{
			if (member.isDead() || !Util.checkIfInRange(getSkill().getSkillRadius(), getEffector(), member, true))
			{
				continue;
			}
			
			fullHP += member.getMaxHp();
			currentHPs += member.getCurrentHp();
		}
		
		double percentHP = currentHPs / fullHP;
		for (L2PcInstance member : party.getMembers())
		{
			if (member.isDead() || !Util.checkIfInRange(getSkill().getSkillRadius(), getEffector(), member, true))
			{
				continue;
			}
			
			double newHP = member.getMaxHp() * percentHP;
			if (newHP > member.getCurrentHp()) // The target gets healed
			{
				// The heal will be blocked if the current hp passes the limit
				if (member.getCurrentHp() > member.getMaxRecoverableHp())
				{
					newHP = member.getCurrentHp();
				}
				else if (newHP > member.getMaxRecoverableHp())
				{
					newHP = member.getMaxRecoverableHp();
				}
			}
			
			member.setCurrentHp(newHP);
			StatusUpdate su = new StatusUpdate(member);
			su.addAttribute(StatusUpdate.CUR_HP, (int) member.getCurrentHp());
			member.sendPacket(su);
		}
		return true;
	}
}