package instances.FreyaInstanceMobs;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage2;
import pk.elfo.gameserver.util.Broadcast;
import pk.elfo.util.Rnd;

public class FreyaInstanceMobs extends AbstractNpcAI
{
	private static final int freyaThrone = 29177;
	private static final int freyaSpelling = 29178;
	private static final int freyaStand = 29179;
	private static final int glacier = 18853;
	private static final int archer_knight = 18855;
	// private static final int archer_breathe = 18854;
	private static final int jinia = 18850;
	private static final int kegor = 18851;
	
	@SuppressWarnings("unused")
	private final String[] sounds =
	{
		"Freya.freya_voice_03",
		"Freya.freya_voice_04",
		"Freya.freya_voice_05",
		"Freya.freya_voice_06",
		"Freya.freya_voice_10",
		"Freya.freya_voice_11"
	};
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("setDisplayEffect2"))
		{
			if (!npc.isDead())
			{
				npc.setDisplayEffect(2);
			}
		}
		else if (event.equalsIgnoreCase("cast"))
		{
			if ((npc != null) && !npc.isDead())
			{
				L2Skill skill = SkillTable.getInstance().getInfo(6437, Rnd.get(1, 3));
				for (L2PcInstance plr : npc.getKnownList().getKnownPlayersInRadius(skill.getSkillRadius()))
				{
					if (!hasBuff(6437, plr) && !plr.isDead() && !plr.isAlikeDead())
					{
						skill.getEffects(npc, plr);
					}
				}
				startQuestTimer("cast", 10000, npc, null);
			}
		}
		else if (event.equalsIgnoreCase("show_string"))
		{
			if ((npc != null) && !npc.isDead())
			{
				broadcastString(npc.getInstanceId());
			}
		}
		return null;
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if ((npcId == jinia) || (npcId == kegor))
		{
			((L2Attackable) npc).reduceHate(attacker, 999999999);
			npc.setCurrentHp(npc.getMaxHp());
		}
		else if (npcId == archer_knight)
		{
			if (npc.getDisplayEffect() == 1)
			{
				npc.setDisplayEffect(2);
				npc.setIsImmobilized(false);
			}
		}
		else if (npcId == freyaSpelling)
		{
			npc.setCurrentHp(npc.getMaxHp());
			((L2Attackable) npc).clearAggroList();
		}
		else if ((npcId == freyaStand) || (npcId == freyaThrone))
		{
			if (!npc.isCastingNow())
			{
				callSkillAI(npc);
			}
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onSpellFinished(L2Npc npc, L2PcInstance player, L2Skill skill)
	{
		if ((npc.getNpcId() == freyaStand) || (npc.getNpcId() == freyaThrone))
		{
			callSkillAI(npc);
		}
		return super.onSpellFinished(npc, player, skill);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		int npcId = npc.getNpcId();
		if (npcId == glacier)
		{
			npc.setDisplayEffect(1);
			npc.setIsImmobilized(true);
			((L2Attackable) npc).setOnKillDelay(0);
			startQuestTimer("setDisplayEffect2", 1000, npc, null);
			startQuestTimer("cast", 10000, npc, null);
		}
		else if (npc.getNpcId() == freyaSpelling)
		{
			npc.setIsImmobilized(true);
		}
		else if ((npcId == jinia) || (npcId == kegor))
		{
			npc.setIsNoRndWalk(true);
			((L2Attackable) npc).setisReturningToSpawnPoint(false);
			L2Npc target = findFreyaTarget(npc.getInstanceId());
			if (target != null)
			{
				npc.setTarget(target);
				((L2Attackable) npc).addDamageHate(target, 0, 999999);
			}
		}
		if (npc instanceof L2Attackable)
		{
			((L2Attackable) npc).setisReturningToSpawnPoint(false);
		}
		return super.onSpawn(npc);
	}
	
	private void callSkillAI(L2Npc mob)
	{
		int[][] freya_skills =
		{
			// id, lvl, time to string, chance
			{
				6274,
				1,
				4000,
				10
			},
			{
				6276,
				1,
				-1,
				100
			},
			{
				6277,
				1,
				-1,
				100
			},
			{
				6278,
				1,
				-1,
				100
			},
			{
				6279,
				1,
				2000,
				100
			},
			{
				6282,
				1,
				-1,
				100
			}
		};
		
		int iter = Rnd.get(0, 2);
		
		if ((freya_skills[iter][3] < 100) && (Rnd.get(100) > freya_skills[iter][3]))
		{
			iter = 3;
		}
		
		mob.doCast(SkillTable.getInstance().getInfo(freya_skills[iter][0], freya_skills[iter][1]));
		if (freya_skills[iter][2] > 0)
		{
			startQuestTimer("show_string", freya_skills[iter][2], mob, null);
		}
		
		if (freya_skills[iter][0] == 6277)
		{
			startQuestTimer("summon_breathe", 1500, mob, null);
		}
	}
	
	private boolean hasBuff(int id, L2PcInstance player)
	{
		for (L2Effect e : player.getAllEffects())
		{
			if (e.getSkill().getId() == id)
			{
				return true;
			}
		}
		return false;
	}
	
	private L2Npc findFreyaTarget(int instanceId)
	{
		for (L2Npc mob : InstanceManager.getInstance().getInstance(instanceId).getNpcs())
		{
			if ((mob.getNpcId() == freyaStand) && !mob.isDead())
			{
				return mob;
			}
		}
		return null;
	}
	
	private void broadcastString(int instanceId)
	{
		ExShowScreenMessage2 sm = new ExShowScreenMessage2(1801111, 3000, ExShowScreenMessage2.ScreenMessageAlign.MIDDLE_CENTER, true, false, -1, false);
		Broadcast.toPlayersInInstance(sm, instanceId);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (npc.getNpcId() == archer_knight)
		{
			if (npc.isImmobilized())
			{
				npc.abortAttack();
				npc.abortCast();
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
			}
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	public FreyaInstanceMobs(String name, String descr)
	{
		super(name, descr);
		int[] mobs =
		{
			archer_knight,
			freyaSpelling,
			freyaStand,
			freyaThrone,
			glacier,
			jinia,
			kegor
		};
		this.registerMobs(mobs);
	}
	
	public static void main(String[] args)
	{
		new FreyaInstanceMobs(FreyaInstanceMobs.class.getSimpleName(), "instances");
	}
}