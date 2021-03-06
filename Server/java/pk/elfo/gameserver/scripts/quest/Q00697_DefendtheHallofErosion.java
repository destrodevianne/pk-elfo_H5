package pk.elfo.gameserver.scripts.quest;

import pk.elfo.gameserver.instancemanager.SoIManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.util.Rnd;

public class Q00697_DefendtheHallofErosion extends Quest
{
	private static final int TEPIOS = 32603;
	private static final int VesperNobleEnhanceStone = 14052;
	
	public Q00697_DefendtheHallofErosion(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(TEPIOS);
		addTalkId(TEPIOS);
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equalsIgnoreCase("32603-03.htm"))
		{
			st.set("cond", "1");
			st.setState(State.STARTED);
			st.playSound("ItemSound.quest_accept");
		}
		return htmltext;
	}
	
	@Override
	public final String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		npc.getNpcId();
		int cond = st.getInt("cond");
		
		switch (st.getState())
		{
			case State.CREATED:
				if (player.getLevel() < 75)
				{
					htmltext = "32603-00.htm";
					st.exitQuest(true);
				}
				if (SoIManager.getCurrentStage() != 4)
				{
					htmltext = "32603-00a.htm";
					st.exitQuest(true);
				}
				htmltext = "32603-01.htm";
				break;
			case State.STARTED:
				if ((cond == 1) && (st.getInt("defenceDone") == 0))
				{
					htmltext = "32603-04.htm";
				}
				else if ((cond == 1) && (st.getInt("defenceDone") != 0))
				{
					st.giveItems(VesperNobleEnhanceStone, Rnd.get(12, 20));
					htmltext = "32603-05.htm";
					st.playSound("ItemSound.quest_finish");
					st.unset("defenceDone");
					st.exitQuest(true);
				}
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00697_DefendtheHallofErosion(697, Q00697_DefendtheHallofErosion.class.getSimpleName(), "Defend the Hall of Erosion");
	}
}