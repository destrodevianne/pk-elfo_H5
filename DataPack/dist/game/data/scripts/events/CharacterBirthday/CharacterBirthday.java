package events.CharacterBirthday;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.util.Util;

/**
 * PkElfo
 */

public class CharacterBirthday extends Quest
{
	private static final int ALEGRIA = 32600;
	private static int SPAWNS = 0;
	private final static int[] GK =
	{
		30006,
		30059,
		30080,
		30134,
		30146,
		30177,
		30233,
		30256,
		30320,
		30540,
		30576,
		30836,
		30848,
		30878,
		30899,
		31275,
		31320,
		31964,
		32163
	};
	
	public CharacterBirthday(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ALEGRIA);
		addStartNpc(GK);
		addTalkId(ALEGRIA);
		addTalkId(GK);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (event.equalsIgnoreCase("despawn_npc"))
		{
			npc.doDie(player);
			SPAWNS--;
			htmltext = null;
		}
		else if (event.equalsIgnoreCase("change"))
		{
			// Change Hat
			if (st.hasQuestItems(10250))
			{
				st.takeItems(10250, 1); // Adventurer Hat (Event)
				st.giveItems(21594, 1); // Birthday Hat
				htmltext = null; // FIXME: Probably has html
				// Despawn npc
				npc.doDie(player);
				SPAWNS--;
			}
			else
			{
				htmltext = "32600-nohat.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		if (SPAWNS >= 3)
		{
			return "busy.htm";
		}		
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}		
		if (!Util.checkIfInRange(10, npc, player, true))
		{
			L2Npc spawned = st.addSpawn(32600, player.getX() + 10, player.getY() + 10, player.getZ() + 10, 0, false, 0, true);
			st.setState(State.STARTED);
			st.startQuestTimer("despawn_npc", 180000, spawned);
			SPAWNS++;
		}
		else
		{
			return "tooclose.htm";
		}
		return null;
	}	
	public static void main(String[] args)
	{
		new CharacterBirthday(-1, CharacterBirthday.class.getSimpleName(), "events");
	}
}