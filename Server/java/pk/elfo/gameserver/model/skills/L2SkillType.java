package pk.elfo.gameserver.model.skills;

import java.lang.reflect.Constructor;

import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillChangeWeapon;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillChargeDmg;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillCreateItem;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillDecoy;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillDefault;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillDrain;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillLearnSkill;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillMount;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSiegeFlag;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSignet;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSignetCasttime;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSpawn;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSummon;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillSweeper;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillTeleport;
import pk.elfo.gameserver.model.skills.l2skills.L2SkillTrap;

/**
 * @author nBd
 */
public enum L2SkillType
{
	// Damage
	PDAM,
	MDAM,
	CPDAM,
	MANADAM,
	CPDAMPERCENT,
	DOT,
	MDOT,
	DRAIN_SOUL,
	DRAIN(L2SkillDrain.class),
	DEATHLINK,
	FATAL,
	BLOW,
	BOMB,
	CAPTURE,
	SIGNET(L2SkillSignet.class),
	SIGNET_CASTTIME(L2SkillSignetCasttime.class),
	// Disablers
	BLEED,
	POISON,
	STUN,
	ROOT,
	CONFUSION,
	FEAR,
	SLEEP,
	CONFUSE_MOB_ONLY,
	MUTE,
	PARALYZE,
	DISARM,
	// hp, mp, cp
	HEAL,
	HOT,
	HEAL_PERCENT,
	HEAL_STATIC,
	COMBATPOINTHEAL,
	CPHEAL_PERCENT,
	CPHOT,
	MANAHEAL,
	MANA_BY_LEVEL,
	MANAHEAL_PERCENT,
	MANARECHARGE,
	MPHOT,
	HPMPCPHEAL_PERCENT,
	HPMPHEAL_PERCENT,
	HPCPHEAL_PERCENT,
	CHAIN_HEAL,
	// sp
	GIVE_SP,
	// reco
	GIVE_RECO,
	// vitality
	GIVE_VITALITY,
	// Aggro
	AGGDAMAGE,
	AGGREDUCE,
	AGGREMOVE,
	AGGREDUCE_CHAR,
	AGGDEBUFF,
	// Fishing
	FISHING,
	PUMPING,
	REELING,
	// Misc
	UNLOCK,
	UNLOCK_SPECIAL,
	ENCHANT_ARMOR,
	ENCHANT_WEAPON,
	ENCHANT_ATTRIBUTE,
	SOULSHOT,
	SPIRITSHOT,
	SIEGEFLAG(L2SkillSiegeFlag.class),
	TAKECASTLE,
	TAKEFORT,
	WEAPON_SA,
	DELUXE_KEY_UNLOCK,
	SOW,
	GET_PLAYER,
	MOUNT(L2SkillMount.class),
	INSTANT_JUMP,
	DETECTION,
	DUMMY,
	// Creation
	COMMON_CRAFT,
	DWARVEN_CRAFT,
	CREATE_ITEM(L2SkillCreateItem.class),
	LEARN_SKILL(L2SkillLearnSkill.class),
	// Summons
	SUMMON(L2SkillSummon.class),
	FEED_PET,
	DEATHLINK_PET,
	STRSIEGEASSAULT,
	ERASE,
	BETRAY,
	DECOY(L2SkillDecoy.class),
	SPAWN(L2SkillSpawn.class),
	// Cancel
	CANCEL,
	CANCEL_ALL,
	CANCEL_STATS,
	CANCEL_DEBUFF,
	NEGATE,
	
	BUFF,
	DEBUFF,
	CONT,
	FUSION,
	
	RESURRECT,
	CHARGEDAM(L2SkillChargeDmg.class),
	MHOT,
	DETECT_WEAKNESS,
	RECALL(L2SkillTeleport.class),
	TELEPORT(L2SkillTeleport.class),
	SUMMON_FRIEND,
	SPOIL,
	SWEEP(L2SkillSweeper.class),
	FAKE_DEATH,
	UNDEAD_DEFENSE,
	BEAST_FEED,
	BEAST_RELEASE,
	BEAST_RELEASE_ALL,
	BEAST_SKILL,
	BEAST_ACCOMPANY,
	CHARGESOUL,
	TRANSFORMDISPEL,
	SUMMON_TRAP(L2SkillTrap.class),
	DETECT_TRAP,
	REMOVE_TRAP,
	SHIFT_TARGET,
	// Kamael WeaponChange
	CHANGEWEAPON(L2SkillChangeWeapon.class),
	
	STEAL_BUFF,
	
	// Skill is done within the core.
	COREDONE,
	// Refuel airship
	REFUEL,
	// Nornil's Power (Nornil's Garden instance)
	NORNILS_POWER,
	// unimplemented
	NOTDONE,
	BALLISTA;
	
	private final Class<? extends L2Skill> _class;
	
	public L2Skill makeSkill(StatsSet set)
	{
		try
		{
			Constructor<? extends L2Skill> c = _class.getConstructor(StatsSet.class);
			
			return c.newInstance(set);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private L2SkillType()
	{
		_class = L2SkillDefault.class;
	}
	
	private L2SkillType(Class<? extends L2Skill> classType)
	{
		_class = classType;
	}
}
