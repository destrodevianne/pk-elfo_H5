package ai.individual;

import java.util.Arrays;
import java.util.Map;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.MinionList;
import pk.elfo.util.L2FastMap;
import ai.npc.AbstractNpcAI;

/**
 * Manages minion's spawn, idle despawn and Teleportation Cube spawn.
 */
public class Epidos extends AbstractNpcAI
{
	private static final int[] EPIDOSES =
	{
		25609,
		25610,
		25611,
		25612
	};
	
	private static final int[] MINIONS =
	{
		25605,
		25606,
		25607,
		25608
	};
	
	private static final int[] MINIONS_COUNT =
	{
		3,
		6,
		11
	};
	
	private final Map<Integer, Double> _lastHp = new L2FastMap<>(true);
	
	private Epidos(String name, String descr)
	{
		super(name, descr);
		addKillId(EPIDOSES);
		addSpawnId(EPIDOSES);
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("check_minions"))
		{
			if ((getRandom(1000) > 250) && _lastHp.containsKey(npc.getObjectId()))
			{
				int hpDecreasePercent = (int) (((_lastHp.get(npc.getObjectId()) - npc.getCurrentHp()) * 100) / npc.getMaxHp());
				int minionsCount = 0;
				int spawnedMinions = ((L2MonsterInstance) npc).getMinionList().countSpawnedMinions();
				
				if ((hpDecreasePercent > 5) && (hpDecreasePercent <= 15) && (spawnedMinions <= 9))
				{
					minionsCount = MINIONS_COUNT[0];
				}
				else if ((((hpDecreasePercent > 1) && (hpDecreasePercent <= 5)) || ((hpDecreasePercent > 15) && (hpDecreasePercent <= 30))) && (spawnedMinions <= 6))
				{
					minionsCount = MINIONS_COUNT[1];
				}
				else if (spawnedMinions == 0)
				{
					minionsCount = MINIONS_COUNT[2];
				}
				
				for (int i = 0; i < minionsCount; i++)
				{
					MinionList.spawnMinion((L2MonsterInstance) npc, MINIONS[Arrays.binarySearch(EPIDOSES, npc.getNpcId())]);
				}
				
				_lastHp.put(npc.getObjectId(), npc.getCurrentHp());
			}
			startQuestTimer("check_minions", 10000, npc, null);
		}
		else if (event.equalsIgnoreCase("check_idle"))
		{
			if (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE)
			{
				npc.deleteMe();
			}
			else
			{
				startQuestTimer("check_idle", 600000, npc, null);
			}
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.isInsideRadius(-45474, 247450, -13994, 2000, true, false))
		{
			addSpawn(32376, -45482, 246277, -14184, 0, false, 0, false);
		}
		
		_lastHp.remove(npc.getObjectId());
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		startQuestTimer("check_minions", 10000, npc, null);
		startQuestTimer("check_idle", 600000, npc, null);
		_lastHp.put(npc.getObjectId(), (double) npc.getMaxHp());
		
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Epidos(Epidos.class.getSimpleName(), "ai/individual");
	}
}