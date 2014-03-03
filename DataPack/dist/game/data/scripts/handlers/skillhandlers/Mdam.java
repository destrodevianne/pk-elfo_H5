package handlers.skillhandlers;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
 
public class Mdam implements ISkillHandler
{
	protected static final Logger _log = Logger.getLogger(Mdam.class.getName());
	private static final Logger _logDamage = Logger.getLogger("damage");
	
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.MDAM,
		L2SkillType.DEATHLINK
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		boolean ss = skill.useSoulShot() && activeChar.isChargedShot(ShotType.SOULSHOTS);
		boolean sps = skill.useSpiritShot() && activeChar.isChargedShot(ShotType.SPIRITSHOTS);
		boolean bss = skill.useSpiritShot() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		for (L2Character target : (L2Character[]) targets)
		{
			if (activeChar.isPlayer() && target.isPlayer() && target.getActingPlayer().isFakeDeath())
			{
				target.stopFakeDeath(true);
			}
			else if (target.isDead())
			{
				continue;
			}
			
			final boolean mcrit = Formulas.calcMCrit(activeChar.getMCriticalHit(target, skill));
			final byte shld = Formulas.calcShldUse(activeChar, target, skill);
			final byte reflect = Formulas.calcSkillReflect(target, skill);
			
			int damage = skill.isStaticDamage() ? (int) skill.getPower() : (int) Formulas.calcMagicDam(activeChar, target, skill, shld, sps, bss, mcrit);
			
			// Curse of Divinity Formula (each buff increase +30%)
			if (!skill.isStaticDamage() && skill.getDependOnTargetBuff())
			{
				damage *= (((target.getBuffCount() * 0.3) + 1.3) / 4);
			}
			
			if (!skill.isStaticDamage() && (skill.getMaxSoulConsumeCount() > 0) && activeChar.isPlayer())
			{
				// Souls Formula (each soul increase +4%)
				int chargedSouls = (activeChar.getActingPlayer().getSouls() <= skill.getMaxSoulConsumeCount()) ? activeChar.getActingPlayer().getSouls() : skill.getMaxSoulConsumeCount();
				damage *= 1 + (chargedSouls * 0.04);
			}
			
			// Possibility of a lethal strike
			Formulas.calcLethalHit(activeChar, target, skill);
			
			if (damage > 0)
			{
				// Manage attack or cast break of the target (calculating rate, sending message...)
				if (!target.isRaid() && Formulas.calcAtkBreak(target, damage))
				{
					target.breakAttack();
					target.breakCast();
				}
				
				// vengeance reflected damage
				// DS: because only skill using vengeanceMdam is Shield Deflect Magic
				// and for this skill no damage should pass to target, just hardcode it for now
				if ((reflect & Formulas.SKILL_REFLECT_VENGEANCE) != 0)
				{
					activeChar.reduceCurrentHp(damage, target, skill);
				}
				else
				{
					activeChar.sendDamageMessage(target, damage, mcrit, false, false);
					target.reduceCurrentHp(damage, activeChar, skill);
				}
				
				if (skill.hasEffects())
				{
					if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0) // reflect skill effects
					{
						activeChar.stopSkillEffects(skill.getId());
						skill.getEffects(target, activeChar);
						SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
						sm.addSkillName(skill);
						activeChar.sendPacket(sm);
					}
					else
					{
						// activate attacked effects, if any
						target.stopSkillEffects(skill.getId());
						if (Formulas.calcSkillSuccess(activeChar, target, skill, shld, ss, sps, bss))
						{
							skill.getEffects(activeChar, target, new Env(shld, ss, sps, bss));
						}
						else
						{
							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_RESISTED_YOUR_S2);
							sm.addCharName(target);
							sm.addSkillName(skill);
							activeChar.sendPacket(sm);
						}
					}
				}
				
				// Logging damage
				if (Config.LOG_GAME_DAMAGE && activeChar.isPlayable() && (damage > Config.LOG_GAME_DAMAGE_THRESHOLD))
				{
					LogRecord record = new LogRecord(Level.INFO, "");
					record.setParameters(new Object[]
					{
						activeChar,
						" did damage ",
						damage,
						skill,
						" to ",
						target
					});
					record.setLoggerName("mdam");
					_logDamage.log(record);
				}
			}
		}
		
		// self Effect :]
		if (skill.hasSelfEffects())
		{
			final L2Effect effect = activeChar.getFirstEffect(skill.getId());
			if ((effect != null) && effect.isSelfEffect())
			{
				// Replace old effect with new one.
				effect.exit();
			}
			skill.getEffectsSelf(activeChar);
		}
		activeChar.setChargedShot(bss ? ShotType.BLESSED_SPIRITSHOTS : ShotType.SPIRITSHOTS, false);
		
		if (skill.isSuicideAttack())
		{
			activeChar.doDie(activeChar);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}