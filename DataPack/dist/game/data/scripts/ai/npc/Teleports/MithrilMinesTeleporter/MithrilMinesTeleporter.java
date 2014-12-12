package ai.npc.Teleports.MithrilMinesTeleporter;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class MithrilMinesTeleporter extends AbstractNpcAI
{
	// NPC
	private final static int TELEPORT_CRYSTAL = 32652;
	// Location
	private static final Location[] LOCS =
	{
		new Location(171946, -173352, 3440),
		new Location(175499, -181586, -904),
		new Location(173462, -174011, 3480),
		new Location(179299, -182831, -224),
		new Location(178591, -184615, 360),
		new Location(175499, -181586, -904)
	};
	
	private MithrilMinesTeleporter()
	{
		super(MithrilMinesTeleporter.class.getSimpleName(), "ai/npc/Teleports/");
		addStartNpc(TELEPORT_CRYSTAL);
		addFirstTalkId(TELEPORT_CRYSTAL);
		addTalkId(TELEPORT_CRYSTAL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int index = Integer.parseInt(event) - 1;
		if (LOCS.length > index)
		{
			Location loc = LOCS[index];
			player.teleToLocation(loc, false);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (npc.isInsideRadius(173147, -173762, L2Npc.INTERACTION_DISTANCE, true))
		{
			return "32652-01.htm";
		}
		
		if (npc.isInsideRadius(181941, -174614, L2Npc.INTERACTION_DISTANCE, true))
		{
			return "32652-02.htm";
		}
		
		if (npc.isInsideRadius(179560, -182956, L2Npc.INTERACTION_DISTANCE, true))
		{
			return "32652-03.htm";
		}
		return super.onFirstTalk(npc, player);
	}
	
	public static void main(String[] args)
	{
		new MithrilMinesTeleporter();
	}
}
