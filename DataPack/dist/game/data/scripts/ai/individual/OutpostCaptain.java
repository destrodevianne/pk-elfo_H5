package ai.individual;

import pk.elfo.gameserver.datatables.DoorTable;
import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class OutpostCaptain extends AbstractNpcAI
{
	private static final int CAPTAIN = 18466;
	
	private static final int[] DEFENDERS =
	{
		22357,
		22358
	};
	
	private static final int DOORKEEPER = 32351;
	
	private OutpostCaptain(String name, String descr)
	{
		super(name, descr);
		addKillId(CAPTAIN);
		addSpawnId(CAPTAIN, DOORKEEPER);
		addSpawnId(DEFENDERS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("level_up"))
		{
			npc.deleteMe();
			HellboundManager.getInstance().setLevel(9);
		}
		return null;
	}
	
	@Override
	public final String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (HellboundManager.getInstance().getLevel() == 8)
		{
			addSpawn(DOORKEEPER, npc.getSpawn().getSpawnLocation(), false, 0, false);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		npc.setIsNoRndWalk(true);
		
		if (npc.getNpcId() == CAPTAIN)
		{
			L2DoorInstance door = DoorTable.getInstance().getDoor(20250001);
			if (door != null)
			{
				door.closeMe();
			}
		}
		else if (npc.getNpcId() == DOORKEEPER)
		{
			startQuestTimer("level_up", 3000, npc, null);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new OutpostCaptain(OutpostCaptain.class.getSimpleName(), "ai");
	}
}