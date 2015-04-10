package quests.Q00179_IntoTheLargeCavern;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

public class Q00179_IntoTheLargeCavern extends Quest
{
	// NPCs
	private static final int KEKROPUS = 32138;
	private static final int MENACING_MACHINE = 32258;
	
	public Q00179_IntoTheLargeCavern(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(KEKROPUS);
		addTalkId(KEKROPUS, MENACING_MACHINE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		final QuestState st = player.getQuestState(getName());
		if (npc.getNpcId() == KEKROPUS)
		{
			if (event.equals("32138-03.html"))
			{
				st.startQuest();
			}
		}
		else if (npc.getNpcId() == MENACING_MACHINE)
		{
			if (event.equals("32258-08.html"))
			{
				st.giveItems(391, 1);
				st.giveItems(413, 1);
				st.exitQuest(false, true);
			}
			else if (event.equals("32258-09.html"))
			{
				st.giveItems(847, 2);
				st.giveItems(890, 2);
				st.giveItems(910, 1);
				st.exitQuest(false, true);
			}
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
		
		if (npc.getNpcId() == KEKROPUS)
		{
			switch (st.getState())
			{
				case State.CREATED:
					if (player.getRace() != Race.Kamael)
					{
						htmltext = "32138-00b.html";
					}
					else
					{
						final QuestState prev = player.getQuestState("178_IconicTrinity");
						final int level = player.getLevel();
						if ((prev != null) && prev.isCompleted() && (level >= 17) && (level <= 21) && (player.getClassId().level() == 0))
						{
							htmltext = "32138-01.htm";
						}
						else if (level < 17)
						{
							htmltext = "32138-00.html";
						}
						else
						{
							htmltext = "32138-00c.html";
						}
					}
					break;
				case State.STARTED:
					if (st.isCond(1))
					{
						htmltext = "32138-03.html";
					}
					break;
				case State.COMPLETED:
					htmltext = getAlreadyCompletedMsg(player);
					break;
			}
		}
		else if ((npc.getNpcId() == MENACING_MACHINE) && (st.getState() == State.STARTED))
		{
			htmltext = "32258-01.html";
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00179_IntoTheLargeCavern(179, Q00179_IntoTheLargeCavern.class.getSimpleName(), "Into The Large Cavern");
	}
}