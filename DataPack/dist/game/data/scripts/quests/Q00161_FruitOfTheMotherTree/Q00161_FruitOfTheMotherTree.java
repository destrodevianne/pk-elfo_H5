package quests.Q00161_FruitOfTheMotherTree;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Projeto PkElfo
 */
 
public class Q00161_FruitOfTheMotherTree extends Quest
{
	// NPCs
	private static final int ANDELLIA = 30362;
	private static final int THALIA = 30371;
	// Items
	private static final int ANDELLRIAS_LETTER = 1036;
	private static final int MOTHERTREE_FRUIT = 1037;
	// Misc
	private static final int MIN_LEVEL = 3;
	
	public Q00161_FruitOfTheMotherTree(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ANDELLIA);
		addTalkId(ANDELLIA, THALIA);
		registerQuestItems(ANDELLRIAS_LETTER, MOTHERTREE_FRUIT);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		String htmltext = event;
		switch (event)
		{
			case "30362-04.htm":
				st.startQuest();
				st.giveItems(ANDELLRIAS_LETTER, 1);
				break;
			case "30371-03.html":
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (npc.getNpcId())
		{
			case ANDELLIA:
				switch (st.getState())
				{
					case State.CREATED:
						htmltext = (player.getRace() == Race.Elf) ? (player.getLevel() >= MIN_LEVEL) ? "30362-03.htm" : "30362-02.htm" : "30362-01.htm";
						break;
					case State.STARTED:
						if (st.isCond(1))
						{
							htmltext = "30362-05.html";
						}
						else if (st.isCond(2) && st.hasQuestItems(MOTHERTREE_FRUIT))
						{
							st.giveAdena(1000, true);
							st.addExpAndSp(1000, 0);
							st.exitQuest(false, true);
							htmltext = "30362-06.html";
						}
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(player);
						break;
				}
				break;
			case THALIA:
				if (st.isStarted())
				{
					if (st.isCond(1) && st.hasQuestItems(ANDELLRIAS_LETTER))
					{
						st.takeItems(ANDELLRIAS_LETTER, -1);
						st.giveItems(MOTHERTREE_FRUIT, 1);
						st.setCond(2, true);
						htmltext = "30371-01.html";
					}
					else if (st.isCond(2) && st.hasQuestItems(MOTHERTREE_FRUIT))
					{
						htmltext = "30371-02.html";
					}
				}
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00161_FruitOfTheMotherTree(161, Q00161_FruitOfTheMotherTree.class.getSimpleName(), "Fruit of the Mother Tree");
	}
}