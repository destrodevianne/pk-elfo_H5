package ai.npc.Teleports.Survivor;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Survivor extends AbstractNpcAI
{
	// NPC
	private static final int SURVIVOR = 32632;
	// Misc
	private static final int MIN_LEVEL = 75;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ("32632-2.htm".equals(event))
		{
			if (player.getLevel() < MIN_LEVEL)
			{
				event = "32632-3.htm";
			}
			else if (player.getAdena() < 150000)
			{
				return event;
			}
			else
			{
				takeItems(player, PcInventory.ADENA_ID, 150000);
				player.teleToLocation(-149406, 255247, -80);
				return null;
			}
		}
		return event;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		return "32632-1.htm";
	}
	
	private Survivor(String name, String descr)
	{
		super(name, descr);
		addStartNpc(SURVIVOR);
		addTalkId(SURVIVOR);
	}
	
	public static void main(String[] args)
	{
		new Survivor(Survivor.class.getSimpleName(), "ai/npc/Teleports/");
	}
}