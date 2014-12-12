package ai.npc.Teleports.StakatoNestTeleporter;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.QuestState;
import quests.Q00240_ImTheOnlyOneYouCanTrust.Q00240_ImTheOnlyOneYouCanTrust;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class StakatoNestTeleporter extends AbstractNpcAI
{
	// Locations
	private final static Location[] LOCS =
	{
		new Location(80456, -52322, -5640),
		new Location(88718, -46214, -4640),
		new Location(87464, -54221, -5120),
		new Location(80848, -49426, -5128),
		new Location(87682, -43291, -4128)
	};
	// NPC
	private final static int KINTAIJIN = 32640;
	
	private StakatoNestTeleporter(String name, String descr)
	{
		super(name, descr);
		addStartNpc(KINTAIJIN);
		addTalkId(KINTAIJIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int index = Integer.parseInt(event) - 1;
		
		if (LOCS.length > index)
		{
			Location loc = LOCS[index];
			
			if (player.getParty() != null)
			{
				for (L2PcInstance partyMember : player.getParty().getMembers())
				{
					if (partyMember.isInsideRadius(player, 1000, true, true))
					{
						partyMember.teleToLocation(loc, true);
					}
				}
			}
			player.teleToLocation(loc, false);
		}
		return super.onAdvEvent(event, npc, player);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState accessQuest = player.getQuestState(Q00240_ImTheOnlyOneYouCanTrust.class.getSimpleName());
		return ((accessQuest != null) && accessQuest.isCompleted()) ? "32640.htm" : "32640-no.htm";
	}
	
	public static void main(String[] args)
	{
		new StakatoNestTeleporter(StakatoNestTeleporter.class.getSimpleName(), "ai/npc/Teleports/");
	}
}