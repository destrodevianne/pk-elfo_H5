package ai.group_template;

import static pk.elfo.gameserver.datatables.SkillTable.*;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.util.Util;
import ai.npc.AbstractNpcAI;

public final class MonasteryOfSilence extends AbstractNpcAI
{
	// NPCs
	private static final int CAPTAIN = 18910; // Solina Knight Captain
	private static final int KNIGHT = 18909; // Solina Knights
	private static final int SCARECROW = 18912; // Scarecrow
	private static final int GUIDE = 22789; // Guide Solina
	private static final int SEEKER = 22790; // Seeker Solina
	private static final int SAVIOR = 22791; // Savior Solina
	private static final int ASCETIC = 22793; // Ascetic Solina
	// ScriptValue
	private static final int START_TIMER_SCARECROW_TRAINING = 1;
	private static final int START_TIMER_ASCETIC_DO_CAST = 1;
	private static final int TRAINING_INTERVAL = 30000; //[JOJO]
	private static final int MINIMAL_SHOUT_INTERVAL = 90; //[JOJO]
	
	private static final int[] DIVINITY_CLAN =
		{
			22794, // Divinity Judge
			22795, // Divinity Manager
		};
	// Skills
	private static final int ORDEAL_STRIKE = getSkillHashCode(6303, 1); // Trial of the Coup
	private static final int LEADER_STRIKE = getSkillHashCode(6304, 1); // Shock
	private static final int SAVER_STRIKE = getSkillHashCode(6305, 1); // Sacred Gnosis
	private static final int SAVER_BLEED = getSkillHashCode(6306, 1); // Solina Strike
	private static final int LEARNING_MAGIC = getSkillHashCode(6308, 1); // Opus of the Wave
	private static final int STUDENT_CANCEL = getSkillHashCode(6310, 1); // Loss of Quest
	private static final int WARRIOR_THRUSTING = getSkillHashCode(6311, 1); // Solina Thrust
	private static final int KNIGHT_BLESS = getSkillHashCode(6313, 1); // Solina Bless
	// Misc
	private static final NpcStringId[] DIVINITY_MSG =
		{
			NpcStringId.S1_WHY_WOULD_YOU_CHOOSE_THE_PATH_OF_DARKNESS,
			NpcStringId.S1_HOW_DARE_YOU_DEFY_THE_WILL_OF_EINHASAD
		};
	private static final NpcStringId[] SOLINA_KNIGHTS_MSG =
		{
			NpcStringId.PUNISH_ALL_THOSE_WHO_TREAD_FOOTSTEPS_IN_THIS_PLACE,
			NpcStringId.WE_ARE_THE_SWORD_OF_TRUTH_THE_SWORD_OF_SOLINA,
			NpcStringId.WE_RAISE_OUR_BLADES_FOR_THE_GLORY_OF_SOLINA
		};
	private MonasteryOfSilence()
	{
		super(MonasteryOfSilence.class.getSimpleName(), "ai/group_template");
		addSeeCreatureId(SCARECROW);
		addSkillSeeId(DIVINITY_CLAN);
		addAttackId(KNIGHT, CAPTAIN, GUIDE, SEEKER, ASCETIC);
		addNpcHateId(GUIDE, SEEKER, SAVIOR, ASCETIC);
		addAggroRangeEnterId(GUIDE, SEEKER, SAVIOR, ASCETIC);
		addSpawnId(KNIGHT);
		addSpawnId(SCARECROW);
		 if (!pk.elfo.Config.FIX_onSpawn_for_SpawnTable) 
		 {
			 {
				 for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(SCARECROW))
				 {
					 onSpawn(spawn.getLastSpawn());
				 }
			 }
		 }
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
		case "SCARECROW": // SCARECROW
			startQuestTimer("TRAINING", TRAINING_INTERVAL, npc, null, true);
			break;
		case "TRAINING": // SCARECROW
		{
			final L2MonsterInstance scarecrow = (L2MonsterInstance) npc;
			if (!scarecrow.hasAI() || !scarecrow.isVisible())
			{
				cancelQuestTimer(event, scarecrow, player);
				scarecrow.setScriptValue(0);
				return null;
			}
			if (scarecrow.getKnownList().getKnownPlayers().size() == 0)
			{
				return null;
			}
			for (L2Object obj : scarecrow.getKnownList().getKnownObjects().values())
			{
				if (obj instanceof L2MonsterInstance && com.l2jserver.gameserver.util.Util.checkIfInRange(400, scarecrow, obj, true))
									
				{
					L2MonsterInstance monster = (L2MonsterInstance) obj;
					if (monster.hasAI() && getRandom(100) < 30 && monster.isVisible() && !monster.isDead() && !monster.isInCombat())
					{
						if (monster.getId() == CAPTAIN)
							{
								final L2MonsterInstance captain = monster;
								final int now;
								if (getRandom(100) < 10 && captain.getScriptValue() < (now = currentTime()))
								{
									captain.setScriptValue(now + MINIMAL_SHOUT_INTERVAL);
									captain.broadcastPacket(new NpcSay(captain.getObjectId(), Say2.NPC_ALL, captain.getId(), SOLINA_KNIGHTS_MSG[getRandom(SOLINA_KNIGHTS_MSG.length)]));
								}
							}
					}
					
					else if (character.getId() == KNIGHT)
					{
						character.setRunning();
						((L2Attackable) character).addDamageHate(npc, 0, 100);
						character.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, npc, null);
					}
				}
			}
			break;
		}
		case "DO_CAST": // ASCETIC
		{
			if (npc.isVisible() && !npc.isDead() && player.isVisible() && !player.isDead())
			{
				final L2Skill skill;
				if (npc.checkDoCastConditions(skill = getSkill(STUDENT_CANCEL)))
				{
					npc.setTarget(player);
					npc.doCast(skill);
				}
				npc.setScriptValue(0);
			}
			break;
		}

		}
		return super.onAdvEvent(event, npc, player);
	}
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isSummon)
	{
		final L2Attackable mob = (L2Attackable) npc;
		switch (npc.getId())
		{
		case KNIGHT:
		{
			final L2Skill skill;
			if ((getRandom(100) < 10) && (mob.getMostHated() == player) && mob.checkDoCastConditions(skill = getSkill(WARRIOR_THRUSTING)))
			{
				npc.setTarget(player);
				npc.doCast(skill);
			}
			break;
		}
		case CAPTAIN:
		{
			final int now;
			if ((getRandom(100) < 20) && (npc.getCurrentHp() < (npc.getMaxHp() * 0.5)) && npc.getScriptValue() < (now = currentTime()))
			{
				npc.setScriptValue(now + MINIMAL_SHOUT_INTERVAL);
				final L2Skill skill;
				if (npc.checkDoCastConditions(skill = getSkill(KNIGHT_BLESS)))
				{
					npc.setTarget(npc);
					npc.doCast(skill);
				}
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), NpcStringId.FOR_THE_GLORY_OF_SOLINA));
				final L2Attackable knight = (L2Attackable) addSpawn(KNIGHT, npc);
				attackPlayer(knight, player);
			}
			break;
		}
		case GUIDE:
		{
			final L2Skill skill;
			if ((getRandom(100) < 3) && (mob.getMostHated() == player) && npc.checkDoCastConditions(skill = getSkill(ORDEAL_STRIKE)))
			{
				npc.setTarget(player);
				npc.doCast(skill);
			}
			break;
		}
		case SEEKER:
		{
			final L2Skill skill;
			if ((getRandom(100) < 33) && (mob.getMostHated() == player) && npc.checkDoCastConditions(skill = getSkill(SAVER_STRIKE)))
			{
				npc.setTarget(npc);
				npc.doCast(skill);
			}
			break;
		}
		case ASCETIC:
		{
			if (getRandom(100) < 3 && (mob.getMostHated() == player) && npc.compareAndSetScriptValue(0, START_TIMER_ASCETIC_DO_CAST))
			{
				startQuestTimer("DO_CAST", 20000, npc, player);
			}
			break;
		}
	}
		return super.onAttack(npc, player, damage, isSummon);
	}
	@Override
	public boolean onNpcHate(L2Attackable mob, L2Playable playable)
	{
		// GUIDE, SEEKER, SAVIOR, ASCETIC
		return playable.getActiveWeaponInstance() != null;
	}
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		if (player.getActiveWeaponInstance() != null)
		{
			int skillHashCode = 0;
			switch (npc.getId())
			{
			case GUIDE:
			{
				if (getRandom(100) < 3)
				{
					skillHashCode = LEADER_STRIKE;
				}
				break;
			}
			case SEEKER:
			{
				skillHashCode = SAVER_BLEED;
				break;
			}
			case SAVIOR:
			{
				skillHashCode = LEARNING_MAGIC;
				break;
			}
			case ASCETIC:
			{
				if (getRandom(100) < 3)
				{
					skillHashCode = STUDENT_CANCEL;
				}
				if (getRandom(100) < 3 && npc.compareAndSetScriptValue(0, START_TIMER_ASCETIC_DO_CAST))
				{
					startQuestTimer("DO_CAST", 20000, npc, player);
				}
				break;
			}
				}
			final L2Skill skill;
			if (skillHashCode != 0 && npc.checkDoCastConditions(skill = getSkill(skillHashCode)))
				{
						npc.setTarget(player);
						npc.doCast(skill);
				}
				if (!npc.isInCombat())
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), NpcStringId.YOU_CANNOT_CARRY_A_WEAPON_WITHOUT_AUTHORIZATION));
				}
					attackPlayer((L2Attackable) npc, player);
		}
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	@Override
	public String onSeeCreature(L2Npc npc, L2Character creature, boolean isSummon)
		{
			// SCARECROW
			if (creature.isPlayer() && npc.compareAndSetScriptValue(0, START_TIMER_SCARECROW_TRAINING))
			{
				final long randomDelay = 1 + TRAINING_INTERVAL * (npc.getObjectId() % 4) / 4;
				startQuestTimer("SCARECROW", randomDelay, npc, null, false);
			}
			return super.onSeeCreature(npc, creature, isSummon);
		}

	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isSummon)
	{
		// DIVINITY_CLAN[]
		if (skill.hasEffectType(L2EffectType.AGGRESSION) && com.l2jserver.gameserver.util.Util.contains(targets, npc))
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), DIVINITY_MSG[getRandom(DIVINITY_MSG.length)]).addPcName(caster));
			attackPlayer((L2Attackable) npc, caster);
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
		{
			if (npc.isTeleporting())
				return null;

			switch (npc.getId())
			{
			case KNIGHT:
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getId(), NpcStringId.FOR_THE_GLORY_OF_SOLINA));
				break;
			case SCARECROW:
				npc.setIsInvul(true);
				npc.disableCoreAI(true);
				break;
			}
			return super.onSpawn(npc);
		}
	
	public static void main(String[] args)
	{
		new MonasteryOfSilence();
	}
}