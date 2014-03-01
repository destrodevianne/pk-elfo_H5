package handlers.effecthandlers;

import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.items.L2Item;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.model.stats.Stats;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
 
public class Heal extends L2Effect
{
	public Heal(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.HEAL;
	}
	
	@Override
	public boolean onStart()
	{
		L2Character target = getEffected();
		L2Character activeChar = getEffector();
		if ((target == null) || target.isDead() || target.isDoor())
		{
			return false;
		}
		
		double amount = calc();
		double staticShotBonus = 0;
		int mAtkMul = 1;
		boolean sps = getSkill().isMagic() && activeChar.isChargedShot(ShotType.SPIRITSHOTS);
		boolean bss = getSkill().isMagic() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		if (((sps || bss) && (activeChar.isPlayer() && activeChar.getActingPlayer().isMageClass())) || activeChar.isSummon())
		{
			staticShotBonus = getSkill().getMpConsume(); // static bonus for spiritshots
			
			if (bss)
			{
				mAtkMul = 4;
				staticShotBonus *= 2.4; // static bonus for blessed spiritshots
			}
			else
			{
				mAtkMul = 2;
			}
		}
		else if ((sps || bss) && activeChar.isNpc())
		{
			staticShotBonus = 2.4 * getSkill().getMpConsume(); // always blessed spiritshots
			mAtkMul = 4;
		}
		else
		{
			// no static bonus
			// grade dynamic bonus
			final L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
			if (weaponInst != null)
			{
				switch (weaponInst.getItem().getItemGrade())
				{
					case L2Item.CRYSTAL_S84:
						mAtkMul = 4;
						break;
					case L2Item.CRYSTAL_S80:
						mAtkMul = 2;
						break;
				}
			}
			// shot dynamic bonus
			if (bss)
			{
				mAtkMul *= 4; // 16x/8x/4x s84/s80/other
			}
			else
			{
				mAtkMul += 1; // 5x/3x/1x s84/s80/other
			}
		}
		
		if (!getSkill().isStaticHeal())
		{
			amount += staticShotBonus + Math.sqrt(mAtkMul * activeChar.getMAtk(activeChar, null));
			amount *= target.calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100;
			// Healer proficiency (since CT1)
			amount *= activeChar.calcStat(Stats.HEAL_PROFICIENCY, 100, null, null) / 100;
			// Extra bonus (since CT1.5)
			if (!getSkill().isStatic())
			{
				amount += target.calcStat(Stats.HEAL_STATIC_BONUS, 0, null, null);
			}
			
			// Heal critic, since CT2.3 Gracia Final
			if (!getSkill().isStatic() && Formulas.calcMCrit(activeChar.getMCriticalHit(target, getSkill())))
			{
				amount *= 3;
			}
		}
		
		amount = Math.min(amount, target.getMaxRecoverableHp() - target.getCurrentHp());
		
		// Prevent negative amounts
		if (amount < 0)
		{
			amount = 0;
		}
		
		target.setCurrentHp(amount + target.getCurrentHp());
		StatusUpdate su = new StatusUpdate(target);
		su.addAttribute(StatusUpdate.CUR_HP, (int) target.getCurrentHp());
		target.sendPacket(su);
		
		if (target.isPlayer())
		{
			if (getSkill().getId() == 4051)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.REJUVENATING_HP);
				target.sendPacket(sm);
			}
			else
			{
				if (activeChar.isPlayer() && (activeChar != target))
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_C1);
					sm.addString(activeChar.getName());
					sm.addNumber((int) amount);
					target.sendPacket(sm);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
					sm.addNumber((int) amount);
					target.sendPacket(sm);
				}
			}
		}
		activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}