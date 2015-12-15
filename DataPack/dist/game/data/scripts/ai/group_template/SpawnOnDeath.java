package ai.group_template;

import java.util.Map;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
import javolution.util.FastMap;
 
/**
 * Projeto PkElfo
 */

public class SpawnOnDeath extends AbstractNpcAI 
{
	private static final Map<Integer, Integer> MOBSPAWNS5 = new FastMap<>();
	private static final Map<Integer, Integer> MOBSPAWNS15 = new FastMap<>();
	private static final Map<Integer, Integer> MOBSPAWNS100 = new FastMap<>();

	public SpawnOnDeath(String name, String descr)
	{
		super(name, descr);
		int[] temp = { 22703, 22704, 18812, 18813, 18814, 22705, 22707 };
		registerMobs(temp, new QuestEventType[] { QuestEventType.ON_KILL });
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		L2Attackable newNpc = null;
		if (MOBSPAWNS15.containsKey(Integer.valueOf(npcId)))
		{
			if (Rnd.get(100) < 15)
			{
				newNpc = (L2Attackable)addSpawn(MOBSPAWNS15.get(Integer.valueOf(npcId)).intValue(), npc);
				newNpc.setRunning();
				newNpc.addDamageHate(killer, 0, 999);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, killer);
			}
		}
		else if (MOBSPAWNS100.containsKey(Integer.valueOf(npcId)))
		{
			npc.deleteMe();
			newNpc = (L2Attackable)addSpawn(MOBSPAWNS100.get(Integer.valueOf(npcId)).intValue(), npc);
		}
		else if ((MOBSPAWNS5.containsKey(Integer.valueOf(npcId))) && (Rnd.get(100) < 5))
		{
			newNpc = (L2Attackable)addSpawn(MOBSPAWNS5.get(Integer.valueOf(npcId)).intValue(), npc);
			newNpc.setRunning();
			newNpc.addDamageHate(killer, 0, 999);
			newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, killer);
		}
		return super.onKill(npc, killer, isPet);
	}

	static
	{
		MOBSPAWNS5.put(Integer.valueOf(22705), Integer.valueOf(22707));
		MOBSPAWNS15.put(Integer.valueOf(22703), Integer.valueOf(22703));
		MOBSPAWNS15.put(Integer.valueOf(22704), Integer.valueOf(22704));
		MOBSPAWNS100.put(Integer.valueOf(18812), Integer.valueOf(18813));
		MOBSPAWNS100.put(Integer.valueOf(18813), Integer.valueOf(18814));
		MOBSPAWNS100.put(Integer.valueOf(18814), Integer.valueOf(18812));
	}
	public static void main(String[] args)
	{
		new SpawnOnDeath(SpawnOnDeath.class.getSimpleName(), "ai");
	}
}