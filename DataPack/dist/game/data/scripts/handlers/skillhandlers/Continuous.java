package handlers.skillhandlers;

import pk.elfo.gameserver.ai.CtrlEvent;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.instancemanager.DuelManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2ClanHallManagerInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class Continuous implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.BUFF,
		L2SkillType.DEBUFF,
		L2SkillType.DOT,
		L2SkillType.MDOT,
		L2SkillType.POISON,
		L2SkillType.BLEED,
		L2SkillType.HOT,
		L2SkillType.CPHOT,
		L2SkillType.MPHOT,
		L2SkillType.FEAR,
		L2SkillType.CONT,
		L2SkillType.UNDEAD_DEFENSE,
		L2SkillType.AGGDEBUFF,
		L2SkillType.FUSION
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		boolean acted = true;
		
		L2PcInstance player = null;
		if (activeChar.isPlayer())
		{
			player = activeChar.getActingPlayer();
		}
		
		if (skill.getEffectId() != 0)
		{
			L2Skill sk = SkillTable.getInstance().getInfo(skill.getEffectId(), skill.getEffectLvl() == 0 ? 1 : skill.getEffectLvl());
			
			if (sk != null)
			{
				skill = sk;
			}
		}
		
		boolean ss = skill.useSoulShot() && activeChar.isChargedShot(ShotType.SOULSHOTS);
		boolean sps = skill.useSpiritShot() && activeChar.isChargedShot(ShotType.SPIRITSHOTS);
		boolean bss = skill.useSpiritShot() && activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS);
		
		for (L2Character target : (L2Character[]) targets)
		{
			byte shld = 0;
			
			if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED)
			{
				target = activeChar;
			}
			
			// Player holding a cursed weapon can't be buffed and can't buff
			if ((skill.getSkillType() == L2SkillType.BUFF) && !(activeChar instanceof L2ClanHallManagerInstance))
			{
				if (target != activeChar)
				{
					if (target.isPlayer())
					{
						L2PcInstance trg = target.getActingPlayer();
						if (trg.isCursedWeaponEquipped())
						{
							continue;
						}
						else if (trg.getBlockCheckerArena() != -1)
						{
							continue;
						}
					}
					else if ((player != null) && player.isCursedWeaponEquipped())
					{
						continue;
					}
				}
			}
			
			switch (skill.getSkillType())
			{
				case HOT:
				case CPHOT:
				case MPHOT:
					if (activeChar.isInvul())
					{
						continue;
					}
					break;
			}
			
			if (skill.isOffensive() || skill.isDebuff())
			{
				shld = Formulas.calcShldUse(activeChar, target, skill);
				acted = Formulas.calcSkillSuccess(activeChar, target, skill, shld, ss, sps, bss);
			}
			
			if (acted)
			{
				if (skill.isToggle())
				{
					L2Effect[] effects = target.getAllEffects();
					if (effects != null)
					{
						for (L2Effect e : effects)
						{
							if (e != null)
							{
								if (e.getSkill().getId() == skill.getId())
								{
									e.exit();
									return;
								}
							}
						}
					}
				}
				
				// if this is a debuff let the duel manager know about it
				// so the debuff can be removed after the duel
				// (player & target must be in the same duel)
				if (target.isPlayer() && target.getActingPlayer().isInDuel() && ((skill.getSkillType() == L2SkillType.DEBUFF) || (skill.getSkillType() == L2SkillType.BUFF)) && (player != null) && (player.getDuelId() == target.getActingPlayer().getDuelId()))
				{
					DuelManager dm = DuelManager.getInstance();
					for (L2Effect buff : skill.getEffects(activeChar, target, new Env(shld, ss, sps, bss)))
					{
						if (buff != null)
						{
							dm.onBuff(target.getActingPlayer(), buff);
						}
					}
				}
				else
				{
					L2Effect[] effects = skill.getEffects(activeChar, target, new Env(shld, ss, sps, bss));
					L2Summon summon = target.getSummon();
					if ((summon != null) && (summon != activeChar) && summon.isServitor() && (effects.length > 0))
					{
						if (effects[0].canBeStolen() || skill.isHeroSkill() || skill.isStatic())
						{
							skill.getEffects(activeChar, target.getSummon(), new Env(shld, ss, sps, bss));
						}
					}
				}
				
				if (skill.getSkillType() == L2SkillType.AGGDEBUFF)
				{
					if (target.isL2Attackable())
					{
						target.getAI().notifyEvent(CtrlEvent.EVT_AGGRESSION, activeChar, (int) skill.getPower());
					}
					else if (target.isPlayable())
					{
						if (target.getTarget() == activeChar)
						{
							target.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, activeChar);
						}
						else
						{
							target.setTarget(activeChar);
						}
					}
				}
			}
			else
			{
				activeChar.sendPacket(SystemMessageId.ATTACK_FAILED);
			}
			
			// Possibility of a lethal strike
			Formulas.calcLethalHit(activeChar, target, skill);
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
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}