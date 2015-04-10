package quests.Q00166_MassOfDarkness;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.network.NpcStringId;

/**
 * Projeto PkElfo
 */
 
public class Q00166_MassOfDarkness extends Quest
{
	// NPCs
	private static final int UNDRIAS = 30130;
	private static final int IRIA = 30135;
	private static final int DORANKUS = 30139;
	private static final int TRUDY = 30143;
	// Items
	private static final int UNDRIAS_LETTER = 1088;
	private static final int CEREMONIAL_DAGGER = 1089;
	private static final int DREVIANT_WINE = 1090;
	private static final int GARMIELS_SCRIPTURE = 1091;
	// Misc
	private static final int MIN_LVL = 2;
	private static final Map<Integer, Integer> NPCs = new HashMap<>();
	static
	{
		NPCs.put(IRIA, CEREMONIAL_DAGGER);
		NPCs.put(DORANKUS, DREVIANT_WINE);
		NPCs.put(TRUDY, GARMIELS_SCRIPTURE);
	}
	
	public Q00166_MassOfDarkness(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(UNDRIAS);
		addTalkId(UNDRIAS, IRIA, DORANKUS, TRUDY);
		registerQuestItems(UNDRIAS_LETTER, CEREMONIAL_DAGGER, DREVIANT_WINE, GARMIELS_SCRIPTURE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equals("30130-03.htm"))
		{
			st.startQuest();
			st.giveItems(UNDRIAS_LETTER, 1);
			return event;
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st != null)
		{
			switch (npc.getNpcId())
			{
				case UNDRIAS:
				{
					switch (st.getState())
					{
						case State.CREATED:
						{
							htmltext = (player.getRace() == Race.DarkElf) ? (player.getLevel() >= MIN_LVL) ? "30130-02.htm" : "30130-01.htm" : "30130-00.htm";
							break;
						}
						case State.STARTED:
						{
							if (st.isCond(2) && st.hasQuestItems(UNDRIAS_LETTER, CEREMONIAL_DAGGER, DREVIANT_WINE, GARMIELS_SCRIPTURE))
							{
								showOnScreenMsg(player, NpcStringId.DELIVERY_DUTY_COMPLETE_N_GO_FIND_THE_NEWBIE_GUIDE, 2, 5000); // TODO: Newbie Guide
								st.addExpAndSp(5672, 466);
								st.giveAdena(2966, true);
								st.exitQuest(false, true);
								htmltext = "30130-05.html";
							}
							else
							{
								htmltext = "30130-04.html";
							}
							break;
						}
						case State.COMPLETED:
						{
							htmltext = getAlreadyCompletedMsg(player);
							break;
						}
					}
					break;
				}
				case IRIA:
				case DORANKUS:
				case TRUDY:
				{
					if (st.isStarted())
					{
						final int npcId = npc.getNpcId();
						final int itemId = NPCs.get(npcId);
						if (st.isCond(1) && !st.hasQuestItems(itemId))
						{
							st.giveItems(itemId, 1);
							if (st.hasQuestItems(CEREMONIAL_DAGGER, DREVIANT_WINE, GARMIELS_SCRIPTURE))
							{
								st.setCond(2, true);
							}
							htmltext = npcId + "-01.html";
						}
						else
						{
							htmltext = npcId + "-02.html";
						}
						break;
					}
				}
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00166_MassOfDarkness(166, Q00166_MassOfDarkness.class.getSimpleName(), "Mass of Darkness");
	}
}