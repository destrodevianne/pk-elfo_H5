package ai.group_template;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class NonLethalableNpcs extends AbstractNpcAI
{
	private static final int[] NPCS = new int[]
	{
		35062, // Headquarters
	};
	
	public NonLethalableNpcs(String name, String descr)
	{
		super(name, descr);
		addSpawnId(NPCS);
		
		for (int npcId : NPCS)
		{
			for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
			{
				onSpawn(spawn.getLastSpawn());
			}
		}
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		npc.setLethalable(false);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new NonLethalableNpcs(NonLethalableNpcs.class.getSimpleName(), "ai");
	}
}