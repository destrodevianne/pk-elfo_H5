package quests.Q00160_NerupasRequest;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.base.Race;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Projeto PkElfo
 */
 
public final class Q00160_NerupasRequest extends Quest
{
	// NPCs
	private static final int NERUPA = 30370;
	private static final int UNOREN = 30147;
	private static final int CREAMEES = 30149;
	private static final int JULIA = 30152;
	// Items
	private static final int SILVERY_SPIDERSILK = 1026;
	private static final int UNOS_RECEIPT = 1027;
	private static final int CELS_TICKET = 1028;
	private static final int NIGHTSHADE_LEAF = 1029;
	private static final int LESSER_HEALING_POTION = 1060;
	// Misc
	private static final int MIN_LEVEL = 3;
	
	public Q00160_NerupasRequest(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(NERUPA);
		addTalkId(NERUPA, UNOREN, CREAMEES, JULIA);
		registerQuestItems(SILVERY_SPIDERSILK, UNOS_RECEIPT, CELS_TICKET, NIGHTSHADE_LEAF);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equals("30370-04.htm"))
		{
			st.startQuest();
			giveItems(player, SILVERY_SPIDERSILK, 1);
			return event;
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		final QuestState st = talker.getQuestState(getName());
		String htmltext = getNoQuestMsg(talker);
		switch (st.getState())
		{
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(talker);
				break;
			}
			case State.CREATED:
			{
				if (npc.getNpcId() == NERUPA)
				{
					if (talker.getRace() != Race.Elf)
					{
						htmltext = "30370-01.htm";
					}
					else if (talker.getLevel() < MIN_LEVEL)
					{
						htmltext = "30370-02.htm";
					}
					else
					{
						htmltext = "30370-03.htm";
					}
				}
				break;
			}
			case State.STARTED:
			{
				switch (npc.getNpcId())
				{
					case NERUPA:
					{
						if (hasQuestItems(talker, NIGHTSHADE_LEAF))
						{
							takeItems(talker, NIGHTSHADE_LEAF, -1);
							rewardItems(talker, LESSER_HEALING_POTION, 5);
							addExpAndSp(talker, 1000, 0);
							st.exitQuest(false, true);
							htmltext = "30370-06.html";
						}
						else
						{
							htmltext = "30370-05.html";
						}
						break;
					}
					case UNOREN:
					{
						if (hasQuestItems(talker, SILVERY_SPIDERSILK))
						{
							takeItems(talker, SILVERY_SPIDERSILK, -1);
							giveItems(talker, UNOS_RECEIPT, 1);
							st.setCond(2, true);
							htmltext = "30147-01.html";
						}
						else if (hasQuestItems(talker, UNOS_RECEIPT))
						{
							htmltext = "30147-02.html";
						}
						else if (hasQuestItems(talker, NIGHTSHADE_LEAF))
						{
							htmltext = "30147-03.html";
						}
						break;
					}
					case CREAMEES:
					{
						if (hasQuestItems(talker, UNOS_RECEIPT))
						{
							takeItems(talker, UNOS_RECEIPT, -1);
							giveItems(talker, CELS_TICKET, 1);
							st.setCond(3, true);
							htmltext = "30149-01.html";
						}
						else if (hasQuestItems(talker, CELS_TICKET))
						{
							htmltext = "30149-02.html";
						}
						else if (hasQuestItems(talker, NIGHTSHADE_LEAF))
						{
							htmltext = "30149-03.html";
						}
						break;
					}
					case JULIA:
					{
						if (hasQuestItems(talker, CELS_TICKET))
						{
							takeItems(talker, CELS_TICKET, -1);
							giveItems(talker, NIGHTSHADE_LEAF, 1);
							st.setCond(4, true);
							htmltext = "30152-01.html";
							
						}
						else if (hasQuestItems(talker, NIGHTSHADE_LEAF))
						{
							htmltext = "30152-02.html";
						}
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00160_NerupasRequest(160, Q00160_NerupasRequest.class.getSimpleName(), "Nerupa's Request");
	}
}