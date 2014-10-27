package quests.Q00020_BringUpWithLove;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Bring Up With Love (20)
 */
public class Q00020_BringUpWithLove extends Quest
{
	// NPC
	private static final int TUNATUN = 31537;
	// Items
	private static final int BEAST_HANDLERS_WHIP = 15473;
	private static final int WATER_CRYSTAL = 9553;
	private static final int JEWEL_OF_INNOCENCE = 7185;
	
	public Q00020_BringUpWithLove(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(TUNATUN);
		addTalkId(TUNATUN);
		addFirstTalkId(TUNATUN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		
		switch (event)
		{
			case "31537-12.htm":
				st.startQuest();
				break;
			case "31537-03.htm":
				if (hasQuestItems(player, BEAST_HANDLERS_WHIP))
				{
					return "31537-03a.htm";
				}
				giveItems(player, BEAST_HANDLERS_WHIP, 1);
				break;
			
			case "31537-15.htm":
				takeItems(player, JEWEL_OF_INNOCENCE, -1);
				giveItems(player, WATER_CRYSTAL, 1);
				st.exitQuest(false, true);
				break;
			case "31537-21.html":
				if (player.getLevel() < 82)
				{
					return "31537-23.html";
				}
				if (hasQuestItems(player, BEAST_HANDLERS_WHIP))
				{
					return "31537-22.html";
				}
				giveItems(player, BEAST_HANDLERS_WHIP, 1);
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			newQuestState(player);
		}
		return "31537-20.html";
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = ((player.getLevel() < 82) ? "31537-00.htm" : "31537-01.htm");
				break;
			case State.STARTED:
				switch (st.getCond())
				{
					case 1:
						htmltext = "31537-13.htm";
						break;
					case 2:
						htmltext = "31537-14.htm";
						break;
				}
				break;
		}
		return htmltext;
	}
	
	public static void checkJewelOfInnocence(L2PcInstance player)
	{
		final QuestState st = player.getQuestState(Q00020_BringUpWithLove.class.getSimpleName());
		if ((st != null) && st.isCond(1) && !st.hasQuestItems(JEWEL_OF_INNOCENCE) && (getRandom(20) == 0))
		{
			st.giveItems(JEWEL_OF_INNOCENCE, 1);
			st.setCond(2, true);
		}
	}
	
	public static void main(String[] args)
	{
		new Q00020_BringUpWithLove(20, Q00020_BringUpWithLove.class.getSimpleName(), "Bring Up With Love");
	}
}