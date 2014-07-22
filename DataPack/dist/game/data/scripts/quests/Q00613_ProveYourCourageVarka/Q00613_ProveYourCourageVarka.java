package quests.Q00613_ProveYourCourageVarka;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.util.Util;

/**
 * Prove Your Courage! (Varka) (613)
 */
public class Q00613_ProveYourCourageVarka extends Quest
{
	// NPC
	private static final int ASHAS = 31377;
	// Monster
	private static final int HEKATON = 25299;
	// Items
	private static final int HEKATON_HEAD = 7240;
	private static final int VALOR_FEATHER = 7229;
	private static final int VARKA_ALLIANCE_THREE = 7223;
	// Misc
	private static final int MIN_LEVEL = 75;
	
	private Q00613_ProveYourCourageVarka(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ASHAS);
		addTalkId(ASHAS);
		addKillId(HEKATON);
		registerQuestItems(HEKATON_HEAD);
	}
	
	@Override
	public void actionForEachPlayer(L2PcInstance player, L2Npc npc, boolean isSummon)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && st.isCond(1) && Util.checkIfInRange(1500, npc, player, false))
		{
			st.giveItems(HEKATON_HEAD, 1);
			st.setCond(2, true);
		}
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
			case "31377-04.htm":
				st.startQuest();
				break;
			case "31377-07.html":
				if (st.hasQuestItems(HEKATON_HEAD) && st.isCond(2))
				{
					st.giveItems(VALOR_FEATHER, 1);
					st.addExpAndSp(10000, 0);
					st.exitQuest(true, true);
				}
				else
				{
					htmltext = getNoQuestMsg(player);
				}
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		executeForEachPlayer(killer, npc, isSummon, true, false);
		return super.onKill(npc, killer, isSummon);
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
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = (player.getLevel() >= MIN_LEVEL) ? (st.hasQuestItems(VARKA_ALLIANCE_THREE)) ? "31377-01.htm" : "31377-02.htm" : "31377-03.htm";
				break;
			case State.STARTED:
				htmltext = (st.isCond(2) && st.hasQuestItems(HEKATON_HEAD)) ? "31377-05.html" : "31377-06.html";
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00613_ProveYourCourageVarka(613, Q00613_ProveYourCourageVarka.class.getSimpleName(), "Prove Your Courage! (Varka)");
	}
}