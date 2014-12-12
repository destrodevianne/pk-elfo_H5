package ai.npc.Teleports.TeleportToFantasy;

import java.util.Map;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class TeleportToFantasy extends AbstractNpcAI
{
	// NPC
	private static final int PADDIES = 32378;
	// Misc
	private static final Map<Integer, Integer> TELEPORTERS = new FastMap<>();
	// Locations
	private static final Location[] RETURN_LOCS =
	{
		new Location(-80826, 149775, -3043),
		new Location(-12672, 122776, -3116),
		new Location(15670, 142983, -2705),
		new Location(83400, 147943, -3404),
		new Location(111409, 219364, -3545),
		new Location(82956, 53162, -1495),
		new Location(146331, 25762, -2018),
		new Location(116819, 76994, -2714),
		new Location(43835, -47749, -792),
		new Location(147930, -55281, -2728),
		new Location(87386, -143246, -1293),
		new Location(12882, 181053, -3560)
	};
	
	private static final Location[] ISLE_LOCS =
	{
		new Location(-58752, -56898, -2032),
		new Location(-59716, -57868, -2032),
		new Location(-60691, -56893, -2032),
		new Location(-59720, -55921, -2032)
	};
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return null;
		}
		
		if (TELEPORTERS.containsKey(npc.getNpcId()))
		{
			int random_id = getRandom(ISLE_LOCS.length);
			
			player.teleToLocation(ISLE_LOCS[random_id], false);
			st.setState(State.STARTED);
			st.set("id", String.valueOf(TELEPORTERS.get(npc.getNpcId())));
		}
		else if (npc.getNpcId() == PADDIES)
		{
			if ((st.getState() == State.STARTED) && (st.getInt("id") > 0))
			{
				int return_id = st.getInt("id") - 1;
				player.teleToLocation(RETURN_LOCS[return_id], false);
				st.unset("id");
			}
			
			else
			{
				player.sendPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.IF_YOUR_MEANS_OF_ARRIVAL_WAS_A_BIT_UNCONVENTIONAL_THEN_ILL_BE_SENDING_YOU_BACK_TO_RUNE_TOWNSHIP_WHICH_IS_THE_NEAREST_TOWN));
				player.teleToLocation(43835, -47749, -792);
			}
			st.exitQuest(true);
		}
		return htmltext;
	}
	
	private TeleportToFantasy(String name, String descr)
	{
		super(name, descr);
		TELEPORTERS.put(30059, 3); // TRISHA
		TELEPORTERS.put(30080, 4); // CLARISSA
		TELEPORTERS.put(30177, 6); // VALENTIA
		TELEPORTERS.put(30233, 8); // ESMERALDA
		TELEPORTERS.put(30256, 2); // BELLA
		TELEPORTERS.put(30320, 1); // RICHLIN
		TELEPORTERS.put(30848, 7); // ELISA
		TELEPORTERS.put(30899, 5); // FLAUEN
		TELEPORTERS.put(31320, 9); // ILYANA
		TELEPORTERS.put(31275, 10); // TATIANA
		TELEPORTERS.put(31964, 11); // BILIA
		
		for (int npcId : TELEPORTERS.keySet())
		{
			addStartNpc(npcId);
			addTalkId(npcId);
		}
		
		addStartNpc(PADDIES);
		addTalkId(PADDIES);
	}
	
	public static void main(String[] args)
	{
		new TeleportToFantasy(TeleportToFantasy.class.getSimpleName(), "ai/npc/Teleports/");
	}
}