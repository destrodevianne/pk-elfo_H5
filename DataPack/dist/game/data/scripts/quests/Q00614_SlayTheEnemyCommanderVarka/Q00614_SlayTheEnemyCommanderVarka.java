package quests.Q00614_SlayTheEnemyCommanderVarka;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.util.Util;

/**
 * Slay the Enemy Commander! (Varka) (614)
 */
public class Q00614_SlayTheEnemyCommanderVarka extends Quest
{
	// NPC
	private static final int ASHAS = 31377;
	// Monster
	private static final int TAYR = 25302;
	// Items
	private static final int TAYR_HEAD = 7241;
	private static final int WISDOM_FEATHER = 7230;
	private static final int VARKA_ALLIANCE_FOUR = 7224;
	// Misc
	private static final int MIN_LEVEL = 75;
	
	private Q00614_SlayTheEnemyCommanderVarka(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ASHAS);
		addTalkId(ASHAS);
		addKillId(TAYR);
		registerQuestItems(TAYR_HEAD);
	}
	
	@Override
	public void actionForEachPlayer(L2PcInstance player, L2Npc npc, boolean isSummon)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && st.isCond(1) && Util.checkIfInRange(1500, npc, player, false))
		{
			st.giveItems(TAYR_HEAD, 1);
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
				if (st.hasQuestItems(TAYR_HEAD) && st.isCond(2))
				{
					st.giveItems(WISDOM_FEATHER, 1);
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
				htmltext = (player.getLevel() >= MIN_LEVEL) ? (st.hasQuestItems(VARKA_ALLIANCE_FOUR)) ? "31377-01.htm" : "31377-02.htm" : "31377-03.htm";
				break;
			case State.STARTED:
				htmltext = (st.isCond(2) && st.hasQuestItems(TAYR_HEAD)) ? "31377-05.html" : "31377-06.html";
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00614_SlayTheEnemyCommanderVarka(614, Q00614_SlayTheEnemyCommanderVarka.class.getSimpleName(), "Slay the Enemy Commander! (Varka)");
	}
}