package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */

public class CpHealPercent extends L2Effect
{
	public CpHealPercent(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.CPHEAL_PERCENT;
	}
	
	@Override
	public boolean onStart()
	{
		L2Character target = getEffected();
		if ((target == null) || target.isDead() || target.isDoor())
		{
			return false;
		}
		StatusUpdate su = new StatusUpdate(target);
		double amount = 0;
		double power = calc();
		boolean full = (power == 100.0);
		
		if (full)
		{
			amount = target.getMaxCp();
		}
		else
		{
			amount = (target.getMaxCp() * power) / 100.0;
		}
		
		amount = Math.min(amount, target.getMaxRecoverableCp() - target.getCurrentCp());
		
		// Prevent negative amounts
		if (amount < 0)
		{
			amount = 0;
		}
		
		// To prevent -value heals, set the value only if current Cp is less than max recoverable.
		if (target.getCurrentCp() < target.getMaxRecoverableCp())
		{
			target.setCurrentCp(amount + target.getCurrentCp());
		}
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CP_WILL_BE_RESTORED);
		sm.addNumber((int) amount);
		target.sendPacket(sm);
		su.addAttribute(StatusUpdate.CUR_CP, (int) target.getCurrentCp());
		target.sendPacket(su);
		
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}