package ai.individual;

import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.util.Rnd;

import ai.npc.AbstractNpcAI;

public class BaylorChest extends AbstractNpcAI
{
	public BaylorChest(String name, String descr)
	{
		super(name, descr);
		addKillId(29116);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == 29116)
		{
			int chance = Rnd.get(100);
			if (chance <= 1)
			{	
				dropItem(npc,9470,1);
			}
			else if ((chance >= 2) && (chance <= 32))
			{
				dropItem(npc,6578,2);
			}
			else
			{
				dropItem(npc,6704,10);
			}
		}
		return super.onKill(npc, player, isPet);
	}

	private void dropItem(L2Npc npc, int itemId, int count) 
	{
		L2ItemInstance ditem = ItemTable.getInstance().createItem("Loot", itemId, count, null);
		ditem.dropMe(npc, npc.getX(), npc.getY(), npc.getZ());		
	}

	public static void main(String[] args)
	{
		new BaylorChest(BaylorChest.class.getSimpleName(), "ai");
	}
}