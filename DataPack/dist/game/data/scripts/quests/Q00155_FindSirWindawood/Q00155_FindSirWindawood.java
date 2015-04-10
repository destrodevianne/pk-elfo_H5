package quests.Q00155_FindSirWindawood;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Projeto PkElfo
 */
 
public class Q00155_FindSirWindawood extends Quest
{
	// NPCs
	private static final int ABELLOS = 30042;
	private static final int SIR_COLLIN_WINDAWOOD = 30311;
	// Items
	private static final int OFFICIAL_LETTER = 1019;
	private static final int HASTE_POTION = 734;
	// Misc
	private static final int MIN_LEVEL = 3;
	
	public Q00155_FindSirWindawood(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ABELLOS);
		addTalkId(ABELLOS, SIR_COLLIN_WINDAWOOD);
		registerQuestItems(OFFICIAL_LETTER);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equalsIgnoreCase("30042-03.htm"))
		{
			st.startQuest();
			st.giveItems(OFFICIAL_LETTER, 1);
			return event;
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		final QuestState st = talker.getQuestState(getName());
		String htmltext = getNoQuestMsg(talker);
		if (st == null)
		{
			return htmltext;
		}
		
		switch (npc.getNpcId())
		{
			case ABELLOS:
				switch (st.getState())
				{
					case State.CREATED:
						htmltext = (talker.getLevel() >= MIN_LEVEL) ? "30042-02.htm" : "30042-01.htm";
						break;
					case State.STARTED:
						htmltext = "30042-04.html";
						break;
					case State.COMPLETED:
						htmltext = getAlreadyCompletedMsg(talker);
						break;
				}
				break;
			case SIR_COLLIN_WINDAWOOD:
				if (st.isStarted() && st.hasQuestItems(OFFICIAL_LETTER))
				{
					st.giveItems(HASTE_POTION, 1);
					st.exitQuest(false, true);
					htmltext = "30311-01.html";
				}
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00155_FindSirWindawood(155, Q00155_FindSirWindawood.class.getSimpleName(), "Find Sir Windawood");
	}
}