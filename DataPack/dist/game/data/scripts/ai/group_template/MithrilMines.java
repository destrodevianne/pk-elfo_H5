package ai.group_template;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import ai.npc.AbstractNpcAI;

public final class MithrilMines extends AbstractNpcAI
{
	// NPCs
	private static final int GRAVE_ROBBER_SUMMONER = 22678; // Grave Robber Summoner (Lunatic)
	private static final int GRAVE_ROBBER_MAGICIAN = 22679; // Grave Robber Magician (Lunatic)
	private static final int[] SUMMONER_MINIONS =
	{
		22683, // Servitor of Darkness
		22684, // Servitor of Darkness
	};
	private static final int[] MAGICIAN_MINIONS =
	{
		22685, // Servitor of Darkness
		22686, // Servitor of Darkness
	};
	
	private MithrilMines(String name, String descr)
	{
		super(name, descr);
		addSpawnId(GRAVE_ROBBER_SUMMONER, GRAVE_ROBBER_MAGICIAN);
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GRAVE_ROBBER_SUMMONER))
		{
			onSpawn(spawn.getLastSpawn());
		}
		
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GRAVE_ROBBER_MAGICIAN))
		{
			onSpawn(spawn.getLastSpawn());
		}
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		int[] minions = MAGICIAN_MINIONS;
		if (npc.getNpcId() == GRAVE_ROBBER_SUMMONER)
		{
			minions = SUMMONER_MINIONS;
		}
		addMinion((L2MonsterInstance) npc, minions[getRandom(minions.length)]);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new MithrilMines(MithrilMines.class.getSimpleName(), "ai/group_template");
	}
}