package ai.group_template;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class SeeThroughSilentMove extends AbstractNpcAI
{
	//@formatter:off
	private static final int[] MOBIDS =
	{
		18001, 18002, 22199,
		22215, 22216, 22217,
		22327, 22746, 22747,
		22748, 22749, 22750,
		22751, 22752, 22753,
		22754, 22755, 22756,
		22757, 22758, 22759,
		22760, 22761, 22762,
		22763, 22764, 22765,
		22794, 22795, 22796,
		22797, 22798, 22799,
		22800, 22843, 22857,
		25725, 25726, 25727,
		29009, 29010, 29011,
		29012, 29013
	};
	//@formatter:on
	
	private SeeThroughSilentMove(String name, String descr)
	{
		super(name, descr);
		for (int npcId : MOBIDS)
		{
			for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(npcId))
			{
				final L2Npc npc = spawn.getLastSpawn();
				if ((npc != null) && npc.isL2Attackable())
				{
					((L2Attackable) npc).setSeeThroughSilentMove(true);
				}
			}
		}
		addSpawnId(MOBIDS);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		if (npc.isL2Attackable())
		{
			((L2Attackable) npc).setSeeThroughSilentMove(true);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new SeeThroughSilentMove(SeeThroughSilentMove.class.getSimpleName(), "ai");
	}
}