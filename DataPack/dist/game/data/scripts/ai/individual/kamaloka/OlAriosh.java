package ai.individual.kamaloka;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import ai.npc.AbstractNpcAI;
import javolution.util.FastSet;
 
/**
 * Projeto PkElfo
 */

public class OlAriosh extends AbstractNpcAI
{
	private static final int ARIOSH = 18555;
	private static final int GUARD = 18556;
	private static L2Npc _guard = null;
	private FastSet<Integer> _lockedSpawns = new FastSet<>();
	private Map<Integer,Integer> _spawnedGuards = new HashMap<>();

	public OlAriosh(String name, String descr)
	{
		super(name, descr);

		addAttackId(ARIOSH);
		addKillId(ARIOSH);
		addKillId(GUARD);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_to_spawn"))
		{
			int objId = npc.getObjectId();
			if (!this._spawnedGuards.containsKey(objId))
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), NpcStringId.WHAT_ARE_YOU_DOING_HURRY_UP_AND_HELP_ME));
				_guard = addSpawn(GUARD, npc.getX() + 100, npc.getY() + 100, npc.getZ(), 0, false, 0L, false, npc.getInstanceId());
				this._lockedSpawns.remove(Integer.valueOf(objId));
				this._spawnedGuards.put(_guard.getObjectId(), Integer.valueOf(objId));
			}
		}
		return super.onAdvEvent(event, npc, player);
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == ARIOSH)
		{
			int objId = npc.getObjectId();
			if (!this._spawnedGuards.containsKey(objId))
			{
				if (!this._lockedSpawns.contains(Integer.valueOf(objId)))
				{
					startQuestTimer("time_to_spawn", 60000L, npc, player);
					this._lockedSpawns.add(Integer.valueOf(objId));
				}
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		switch (npc.getNpcId())
		{
			case GUARD:
				this._spawnedGuards.remove(npc.getObjectId());
				break;
			case ARIOSH:
				this._spawnedGuards.remove(_guard.getObjectId());
				_guard.decayMe();
				cancelQuestTimer("time_to_spawn", npc, killer);
		}
		return super.onKill(npc, killer, isPet);
	}

	public static void main(String[] args)
	{
		new OlAriosh(OlAriosh.class.getSimpleName(), "ai/individual");
	}
}