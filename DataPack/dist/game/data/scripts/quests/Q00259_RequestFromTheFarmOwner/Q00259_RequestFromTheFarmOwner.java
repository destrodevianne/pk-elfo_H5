package quests.Q00259_RequestFromTheFarmOwner;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

public final class Q00259_RequestFromTheFarmOwner extends Quest
{
	// Npcs
	private static final int EDMOND = 30497;
	private static final int MARIUS = 30405;
	// Monsters
	private static final int[] MONSTERS = new int[]
	{
		20103, // Giant Spider
		20106, // Talon Spider
		20108, // Blade Spider
	};
	// Items
	private static final int SPIDER_SKIN = 1495;
	// Misc
	private static final int MIN_LVL = 15;
	private static final int SKIN_COUNT = 10;
	private static final int SKIN_REWARD = 25;
	private static final int SKIN_BONUS = 250;
	private static final Map<String, ItemHolder> CONSUMABLES = new HashMap<>();
	static
	{
		CONSUMABLES.put("30405-04.html", new ItemHolder(1061, 2)); // Greater Healing Potion
		CONSUMABLES.put("30405-05.html", new ItemHolder(17, 250)); // Wooden Arrow
		CONSUMABLES.put("30405-05a.html", new ItemHolder(1835, 60)); // Soulshot: No Grade
		CONSUMABLES.put("30405-05c.html", new ItemHolder(2509, 30)); // Spiritshot: No Grade
	}
	
	private Q00259_RequestFromTheFarmOwner(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(EDMOND);
		addTalkId(EDMOND, MARIUS);
		addKillId(MONSTERS);
		registerQuestItems(SPIDER_SKIN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = null;
		if (st == null)
		{
			return htmltext;
		}
		
		switch (event)
		{
			case "30405-03.html":
			case "30405-05b.html":
			case "30405-05d.html":
			case "30497-07.html":
			{
				htmltext = event;
				break;
			}
			case "30405-04.html":
			case "30405-05.html":
			case "30405-05a.html":
			case "30405-05c.html":
			{
				if (st.getQuestItemsCount(SPIDER_SKIN) >= SKIN_COUNT)
				{
					st.giveItems(CONSUMABLES.get(event));
					st.takeItems(SPIDER_SKIN, SKIN_COUNT);
					htmltext = event;
				}
				break;
			}
			case "30405-06.html":
			{
				htmltext = (st.getQuestItemsCount(SPIDER_SKIN) >= SKIN_COUNT) ? event : "30405-07.html";
				break;
			}
			case "30497-03.html":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
			case "30497-06.html":
			{
				st.exitQuest(true, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState st = killer.getQuestState(getName());
		if (st != null)
		{
			st.giveItems(SPIDER_SKIN, 1);
			st.playSound(QuestSound.ITEMSOUND_QUEST_ITEMGET);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (npc.getNpcId())
		{
			case EDMOND:
			{
				switch (st.getState())
				{
					case State.CREATED:
					{
						htmltext = (player.getLevel() >= MIN_LVL) ? "30497-02.htm" : "30497-01.html";
						break;
					}
					case State.STARTED:
					{
						if (st.hasQuestItems(SPIDER_SKIN))
						{
							final long skins = st.getQuestItemsCount(SPIDER_SKIN);
							st.giveAdena((skins * SKIN_REWARD) + ((skins >= 10) ? SKIN_BONUS : 0), true);
							st.takeItems(SPIDER_SKIN, -1);
							htmltext = "30497-05.html";
						}
						else
						{
							htmltext = "30497-04.html";
						}
						break;
					}
				}
				break;
			}
			case MARIUS:
			{
				htmltext = (st.getQuestItemsCount(SPIDER_SKIN) >= SKIN_COUNT) ? "30405-02.html" : "30405-01.html";
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00259_RequestFromTheFarmOwner(259, Q00259_RequestFromTheFarmOwner.class.getSimpleName(), "Request from the Farm Owner");
	}
}
