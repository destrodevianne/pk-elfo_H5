package quests.Q00607_ProveYourCourageKetra;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.util.Util;

/**
 * Prove Your Courage! (Ketra) (607)
 */
public class Q00607_ProveYourCourageKetra extends Quest
{
	// NPC
	private static final int KADUN = 31370;
	// Monster
	private static final int SHADITH = 25309;
	// Items
	private static final int SHADITH_HEAD = 7235;
	private static final int VALOR_TOTEM = 7219;
	private static final int KETRA_ALLIANCE_THREE = 7213;
	// Misc
	private static final int MIN_LEVEL = 75;
	
	private Q00607_ProveYourCourageKetra(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(KADUN);
		addTalkId(KADUN);
		addKillId(SHADITH);
		registerQuestItems(SHADITH_HEAD);
	}
	
	@Override
	public void actionForEachPlayer(L2PcInstance player, L2Npc npc, boolean isSummon)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && st.isCond(1) && Util.checkIfInRange(1500, npc, player, false))
		{
			st.giveItems(SHADITH_HEAD, 1);
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
			case "31370-04.htm":
				st.startQuest();
				break;
			case "31370-07.html":
				if (st.hasQuestItems(SHADITH_HEAD) && st.isCond(2))
				{
					st.giveItems(VALOR_TOTEM, 1);
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
				htmltext = (player.getLevel() >= MIN_LEVEL) ? (st.hasQuestItems(KETRA_ALLIANCE_THREE)) ? "31370-01.htm" : "31370-02.htm" : "31370-03.htm";
				break;
			case State.STARTED:
				htmltext = (st.isCond(2) && st.hasQuestItems(SHADITH_HEAD)) ? "31370-05.html" : "31370-06.html";
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00607_ProveYourCourageKetra(607, Q00607_ProveYourCourageKetra.class.getSimpleName(), "Prove Your Courage! (Ketra)");
	}
}