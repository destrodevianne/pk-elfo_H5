package quests.Q00154_SacrificeToTheSea;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;

/**
 * Projeto PkElfo
 */
 
public final class Q00154_SacrificeToTheSea extends Quest
{
	// NPCs
	private static final int ROCKSWELL = 30312;
	private static final int CRISTEL = 30051;
	private static final int ROLLFNAN = 30055;
	// Items
	private static final int FOX_FUR = 1032;
	private static final int FOX_FUR_YAM = 1033;
	private static final int MAIDEN_DOLL = 1034;
	// Monsters
	private static final int ELDER_KELTIR = 20544;
	private static final int YOUNG_KELTIR = 20545;
	private static final int KELTIR = 20481;
	// Reward
	private static final int MAGE_EARING = 113;
	// Misc
	private static final int MIN_LVL = 2;
	
	public Q00154_SacrificeToTheSea(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(ROCKSWELL);
		addTalkId(ROCKSWELL, CRISTEL, ROLLFNAN);
		addKillId(ELDER_KELTIR, YOUNG_KELTIR, KELTIR);
		registerQuestItems(FOX_FUR, FOX_FUR_YAM, MAIDEN_DOLL);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if ((st != null) && event.equals("30312-03.htm"))
		{
			st.startQuest();
			return event;
		}
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		final QuestState st = talker.getQuestState(getName());
		String htmltext = getNoQuestMsg(talker);
		switch (npc.getNpcId())
		{
			case ROCKSWELL:
			{
				if (st.isCreated())
				{
					htmltext = ((talker.getLevel() >= MIN_LVL) ? "30312-01.htm" : "30312-02.htm");
				}
				else if (st.isStarted())
				{
					switch (st.getCond())
					{
						case 1:
						{
							htmltext = "30312-04.html";
							break;
						}
						case 2:
						{
							htmltext = "30312-07.html";
							break;
						}
						case 3:
						{
							htmltext = "30312-05.html";
							break;
						}
						case 4:
						{
							takeItems(talker, MAIDEN_DOLL, -1);
							rewardItems(talker, MAGE_EARING, 1);
							addExpAndSp(talker, 0, 1000);
							st.exitQuest(false, true);
							htmltext = "30312-06.html";
							break;
						}
					}
				}
				else
				{
					htmltext = getAlreadyCompletedMsg(talker);
				}
				break;
			}
			case CRISTEL:
			{
				switch (st.getCond())
				{
					case 1:
					{
						htmltext = "30051-02.html";
						break;
					}
					case 2:
					{
						takeItems(talker, FOX_FUR, -1);
						giveItems(talker, FOX_FUR_YAM, 1);
						st.setCond(3, true);
						htmltext = "30051-01.html";
						break;
					}
					case 3:
					{
						htmltext = "30051-03.html";
						break;
					}
					case 4:
					{
						htmltext = "30051-04.html";
						break;
					}
				}
				break;
			}
			case ROLLFNAN:
			{
				switch (st.getCond())
				{
					case 1:
					case 2:
					{
						htmltext = "30055-03.html";
						break;
					}
					case 3:
					{
						takeItems(talker, FOX_FUR_YAM, -1);
						giveItems(talker, MAIDEN_DOLL, 1);
						st.setCond(4, true);
						htmltext = "30055-01.html";
						break;
					}
					case 4:
					{
						htmltext = "30055-02.html";
						break;
					}
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		final QuestState st;
		if ((npc.getNpcId() == ELDER_KELTIR) || (npc.getNpcId() == YOUNG_KELTIR) ||(npc.getNpcId() == KELTIR))
		{
			st = player.getQuestState(getName());
			st.giveItems(FOX_FUR, 1);
			st.playSound("ItemSound.quest_itemget");

			if (st.getQuestItemsCount(FOX_FUR) == 10)
			{
				st.setCond(2, true);
			}
		}
		return super.onKill(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Q00154_SacrificeToTheSea(154, Q00154_SacrificeToTheSea.class.getSimpleName(), "Sacrifice to the Sea");
	}
}