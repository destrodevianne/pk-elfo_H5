package quests.Q00165_ShilensHunt;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Projeto PkElfo
 */
 
public class Q00165_ShilensHunt extends Quest
{
	// NPC
	private static final int NELSYA = 30348;
	// Monsters
	private static final Map<Integer, Integer> MONSTERS = new HashMap<>();
	static
	{
		MONSTERS.put(20456, 3); // Ashen Wolf
		MONSTERS.put(20529, 1); // Young Brown Keltir
		MONSTERS.put(20532, 1); // Brown Keltir
		MONSTERS.put(20536, 2); // Elder Brown Keltir
	}
	// Items
	private static final int LESSER_HEALING_POTION = 1060;
	private static final int DARK_BEZOAR = 1160;
	// Misc
	private static final int MIN_LVL = 3;
	private static final int REQUIRED_COUNT = 13;
	
	public Q00165_ShilensHunt(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(NELSYA);
		addTalkId(NELSYA);
		addKillId(MONSTERS.keySet());
		registerQuestItems(DARK_BEZOAR);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equalsIgnoreCase("30348-03.htm"))
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
		if ((st != null) && st.isCond(1) && (getRandom(3) < MONSTERS.get(npc.getNpcId())))
		{
			st.giveItems(DARK_BEZOAR, 1);
			if (st.getQuestItemsCount(DARK_BEZOAR) < REQUIRED_COUNT)
			{
				st.playSound("ItemSound.quest_itemget");
			}
			else
			{
				st.setCond(2, true);
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
					htmltext = (player.getRace() == Race.DarkElf) ? (player.getLevel() >= MIN_LVL) ? "30348-02.htm" : "30348-01.htm" : "30348-00.htm";
					break;
				}
				case State.STARTED:
				{
					if (st.isCond(2) && (st.getQuestItemsCount(DARK_BEZOAR) >= REQUIRED_COUNT))
					{
						st.giveItems(LESSER_HEALING_POTION, 5);
						st.addExpAndSp(1000, 0);
						st.exitQuest(false, true);
						htmltext = "30348-05.html";
					}
					else
					{
						htmltext = "30348-04.html";
					}
					break;
				}
				case State.COMPLETED:
				{
					htmltext = getAlreadyCompletedMsg(player);
					break;
				}
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00165_ShilensHunt(165, Q00165_ShilensHunt.class.getSimpleName(), "Shilen's Hunt");
	}
}