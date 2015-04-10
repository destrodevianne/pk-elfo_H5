package ai.group_template;

import java.util.EnumMap;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.ClassId;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.util.Util;

public final class DragonValley extends AbstractNpcAI
{
	// NPC
	private static final int[] SUMMON_NPC =
	{
		22822, // Drakos Warrior
		22824, // Drakos Guardian
		22862, // Drakos Hunter
	};
	private static final int[] SPAWN_ANIMATION =
	{
		22826, // Scorpion Bones
		22823, // Drakos Assassin
		22828, // Parasitic Leech
	};
	private static final int[] SPOIL_REACT_MONSTER =
	{
		22822, // Drakos Warrior
		22823, // Drakos Assassin
		22824, // Drakos Guardian
		22825, // Giant Scorpion Bones
		22826, // Scorpion Bones
		22827, // Batwing Drake
		22828, // Parasitic Leech
		22829, // Emerald Drake
		22830, // Gem Dragon
		22831, // Dragon Tracker of the Valley
		22832, // Dragon Scout of the Valley
		22833, // Sand Drake Tracker
		22834, // Dust Dragon Tracker
		22860, // Hungry Parasitic Leech
		22861, // Hard Scorpion Bones
		22862, // Drakos Hunter
	};
		
	// Skills
	private static final SkillHolder SELF_DESTRUCTION = new SkillHolder(6850, 1);
	private static final SkillHolder MORALE_BOOST1 = new SkillHolder(6885, 1);
	private static final SkillHolder MORALE_BOOST2 = new SkillHolder(6885, 2);
	private static final SkillHolder MORALE_BOOST3 = new SkillHolder(6885, 3);
	
	// Misc
	private static final EnumMap<ClassId, Double> CLASS_POINTS = new EnumMap<>(ClassId.class);
	{
		CLASS_POINTS.put(ClassId.adventurer, 0.2);
		CLASS_POINTS.put(ClassId.arcanaLord, 1.5);
		CLASS_POINTS.put(ClassId.archmage, 0.3);
		CLASS_POINTS.put(ClassId.cardinal, -0.6);
		CLASS_POINTS.put(ClassId.dominator, 0.2);
		CLASS_POINTS.put(ClassId.doombringer, 0.2);
		CLASS_POINTS.put(ClassId.doomcryer, 0.1);
		CLASS_POINTS.put(ClassId.dreadnought, 0.7);
		CLASS_POINTS.put(ClassId.duelist, 0.2);
		CLASS_POINTS.put(ClassId.elementalMaster, 1.4);
		CLASS_POINTS.put(ClassId.evaSaint, -0.6);
		CLASS_POINTS.put(ClassId.evaTemplar, 0.8);
		CLASS_POINTS.put(ClassId.femaleSoulhound, 0.4);
		CLASS_POINTS.put(ClassId.fortuneSeeker, 0.9);
		CLASS_POINTS.put(ClassId.ghostHunter, 0.2);
		CLASS_POINTS.put(ClassId.ghostSentinel, 0.2);
		CLASS_POINTS.put(ClassId.grandKhavatari, 0.2);
		CLASS_POINTS.put(ClassId.hellKnight, 0.6);
		CLASS_POINTS.put(ClassId.hierophant, 0.0);
		CLASS_POINTS.put(ClassId.judicator, 0.1);
		CLASS_POINTS.put(ClassId.moonlightSentinel, 0.2);
		CLASS_POINTS.put(ClassId.maestro, 0.7);
		CLASS_POINTS.put(ClassId.maleSoulhound, 0.4);
		CLASS_POINTS.put(ClassId.mysticMuse, 0.3);
		CLASS_POINTS.put(ClassId.phoenixKnight, 0.6);
		CLASS_POINTS.put(ClassId.sagittarius, 0.2);
		CLASS_POINTS.put(ClassId.shillienSaint, -0.6);
		CLASS_POINTS.put(ClassId.shillienTemplar, 0.8);
		CLASS_POINTS.put(ClassId.soultaker, 0.3);
		CLASS_POINTS.put(ClassId.spectralDancer, 0.4);
		CLASS_POINTS.put(ClassId.spectralMaster, 1.4);
		CLASS_POINTS.put(ClassId.stormScreamer, 0.3);
		CLASS_POINTS.put(ClassId.swordMuse, 0.4);
		CLASS_POINTS.put(ClassId.titan, 0.3);
		CLASS_POINTS.put(ClassId.trickster, 0.5);
		CLASS_POINTS.put(ClassId.windRider, 0.2);
	}
	
	public DragonValley()
	{
		super(DragonValley.class.getSimpleName(), "ai/group_template");
		addAttackId(22858);
		addAttackId(SUMMON_NPC);
		addKillId(22858);
		addKillId(SPOIL_REACT_MONSTER);
		addSpawnId(22818);
		addSpawnId(SPOIL_REACT_MONSTER);
		
		for (int npcId : SPOIL_REACT_MONSTER)
		{
			for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
			{
				onSpawn(spawn.getLastSpawn());
			}
		}
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(22858))
		{
			onSpawn(spawn.getLastSpawn());
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equals("SelfDestruction") && (npc != null) && !npc.isDead())
		{
			npc.abortAttack();
			npc.disableCoreAI(true);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			npc.doCast(SELF_DESTRUCTION.getSkill());
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (npc.getNpcId() == 22858)	//Necromancer of the Valley
		{
			spawnGhost(npc, attacker, isSummon, 1);
		}
		else
		{
			if ((npc.getCurrentHp() < (npc.getMaxHp() / 2)) && (getRandom(100) < 5) && npc.isScriptValue(0))
			{
				npc.setScriptValue(1);
				final int rnd = getRandom(3, 5);
				for (int i = 0; i < rnd; i++)
				{
					final L2Playable playable = isSummon ? attacker.getSummon() : attacker;
					final L2Attackable minion = (L2Attackable) addSpawn(22823, npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), true, 0, true);
					attackPlayer(minion, playable);
				}
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.getNpcId() == 22858)
		{
			spawnGhost(npc, killer, isSummon, 20);
		}
		else if (((L2Attackable) npc).isSweepActive())
		{
			((L2Attackable) npc).dropItem(killer, getRandom(8604, 8605), 1);	//Greater Herb Of Mana = 8604, Superior Herb Of Mana = 8605
			manageMoraleBoost(killer, npc);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		((L2Attackable) npc).setOnKillDelay(0);
		if (npc.getNpcId() == 22818)	//Exploding Orc Ghost
		{
			startQuestTimer("SelfDestruction", 3000, npc, null);
		}
		else if (Util.contains(SPAWN_ANIMATION, npc.getNpcId()))
		{
			npc.setShowSummonAnimation(true);
		}
		return super.onSpawn(npc);
	}
	
	private void manageMoraleBoost(L2PcInstance player, L2Npc npc)
	{
		double points = 0;
		int moraleBoostLv = 0;
		
		if (player.isInParty() && (player.getParty().getMemberCount() >= 3) && (npc != null))	//Min Members = 3
		{
			for (L2PcInstance member : player.getParty().getMembers())
			{
				if ((member.getLevel() >= 80) && (member.getClassId().level() >= 3) && (Util.calculateDistance(npc, member, true) < 1500))	// Min Level = 80; Clan Level = 3; Min Distance = 1500;
				{
					points += CLASS_POINTS.get(member.getClassId());
				}
			}
			
			if (points >= 3)
			{
				moraleBoostLv = 3;
			}
			else if (points >= 2)
			{
				moraleBoostLv = 2;
			}
			else if (points >= 1)
			{
				moraleBoostLv = 1;
			}
			
			for (L2PcInstance member : player.getParty().getMembers())
			{
				if (Util.calculateDistance(npc, member, true) < 1500)
				{
					switch (moraleBoostLv)
					{
						case 1:
							MORALE_BOOST1.getSkill().getEffects(member, member);
							break;
						case 2:
							MORALE_BOOST2.getSkill().getEffects(member, member);
							break;
						case 3:
							MORALE_BOOST3.getSkill().getEffects(member, member);
							break;
					}
				}
			}
		}
	}
	
	private void spawnGhost(L2Npc npc, L2PcInstance player, boolean isSummon, int chance)
	{
		if ((npc.getScriptValue() < 2) && (getRandom(100) < chance))
		{
			int val = npc.getScriptValue();
			final L2Playable attacker = isSummon ? player.getSummon() : player;
			final L2Attackable Ghost1 = (L2Attackable) addSpawn(getRandom(22818, 22819), npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, true);
			attackPlayer(Ghost1, attacker);
			val++;
			if ((val < 2) && (getRandom(100) < 10))
			{
				final L2Attackable Ghost2 = (L2Attackable) addSpawn(getRandom(22818, 22819), npc.getX(), npc.getY(), npc.getZ() + 20, npc.getHeading(), false, 0, false);
				attackPlayer(Ghost2, attacker);
				val++;
			}
			npc.setScriptValue(val);
		}
	}
	
	public static void main(String[] args)
	{
		new DragonValley();
	}
	
	public static DragonValley getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final DragonValley _instance = new DragonValley();
	}
}