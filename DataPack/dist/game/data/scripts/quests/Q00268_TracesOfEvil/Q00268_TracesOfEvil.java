package quests.Q00268_TracesOfEvil;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

public final class Q00268_TracesOfEvil extends Quest
{
	// NPC
	private static final int KUNAI = 30559;
	// Item
	private static final int CONTAMINATED_KASHA_SPIDER_VENOM = 10869;
	// Monsters
	private static final int[] MONSTERS = new int[]
	{
		20474, // Kasha Spider
		20476, // Kasha Fang Spider
		20478, // Kasha Blade Spider
	};
	// Misc
	private static final int MIN_LVL = 15;
	
	private Q00268_TracesOfEvil(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(KUNAI);
		addTalkId(KUNAI);
		addKillId(MONSTERS);
		registerQuestItems(CONTAMINATED_KASHA_SPIDER_VENOM);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equalsIgnoreCase("30559-03.htm"))
		{
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState st = killer.getQuestState(getName());
		if ((st != null) && st.isCond(1))
		{
			st.giveItems(CONTAMINATED_KASHA_SPIDER_VENOM, 1);
			if (st.getQuestItemsCount(CONTAMINATED_KASHA_SPIDER_VENOM) >= 30)
			{
				st.setCond(2, true);
			}
			else
			{
				st.playSound(QuestSound.ITEMSOUND_QUEST_ITEMGET);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st != null)
		{
			switch (st.getState())
			{
				case State.CREATED:
				{
					htmltext = (player.getLevel() >= MIN_LVL) ? "30559-02.htm" : "30559-01.htm";
					break;
				}
				case State.STARTED:
				{
					switch (st.getCond())
					{
						case 1:
						{
							htmltext = (!st.hasQuestItems(CONTAMINATED_KASHA_SPIDER_VENOM)) ? "30559-04.html" : "30559-05.html";
							break;
						}
						case 2:
						{
							if (st.getQuestItemsCount(CONTAMINATED_KASHA_SPIDER_VENOM) >= 30)
							{
								st.giveAdena(2474, true);
								st.addExpAndSp(8738, 409);
								st.exitQuest(true, true);
								htmltext = "30559-06.html";
							}
							break;
						}
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00268_TracesOfEvil(268, Q00268_TracesOfEvil.class.getSimpleName(), "Traces of Evil");
	}
}
