package quests.Q00152_ShardsOfGolem;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Projeto PkElfo
 */
 
public class Q00152_ShardsOfGolem extends Quest
{
	// NPCs
	private static final int HARRYS = 30035;
	private static final int ALTRAN = 30283;
	// Monster
	private static final int STONE_GOLEM = 20016;
	// Items
	private static final int WOODEN_BREASTPLATE = 23;
	private static final int HARRYS_1ST_RECIEPT = 1008;
	private static final int HARRYS_2ND_RECIEPT = 1009;
	private static final int GOLEM_SHARD = 1010;
	private static final int TOOL_BOX = 1011;
	// Misc
	private static final int MIN_LVL = 10;
	
	public Q00152_ShardsOfGolem(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(HARRYS);
		addTalkId(HARRYS, ALTRAN);
		addKillId(STONE_GOLEM);
		registerQuestItems(HARRYS_1ST_RECIEPT, HARRYS_2ND_RECIEPT, GOLEM_SHARD, TOOL_BOX);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = null;
		if (st != null)
		{
			switch (event)
			{
				case "30035-03.htm":
				{
					st.startQuest();
					st.giveItems(HARRYS_1ST_RECIEPT, 1);
					htmltext = event;
					break;
				}
				case "30283-02.html":
				{
					if (st.isCond(1) && st.hasQuestItems(HARRYS_1ST_RECIEPT))
					{
						st.takeItems(HARRYS_1ST_RECIEPT, -1);
						st.giveItems(HARRYS_2ND_RECIEPT, 1);
						st.setCond(2, true);
						htmltext = event;
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState st = killer.getQuestState(getName());
		if ((st != null) && st.isCond(2) && (getRandom(100) < 30) && (st.getQuestItemsCount(GOLEM_SHARD) < 5))
		{
			st.giveItems(GOLEM_SHARD, 1);
			if (st.getQuestItemsCount(GOLEM_SHARD) >= 5)
			{
				st.setCond(3, true);
			}
			else
			{
				st.playSound("ItemSound.quest_itemget");
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		String htmltext = getNoQuestMsg(player);
		if (st != null)
		{
			switch (npc.getNpcId())
			{
				case HARRYS:
				{
					switch (st.getState())
					{
						case State.CREATED:
						{
							htmltext = player.getLevel() >= MIN_LVL ? "30035-02.htm" : "30035-01.htm";
							break;
						}
						case State.STARTED:
						{
							switch (st.getCond())
							{
								case 1:
								{
									if (st.hasQuestItems(HARRYS_1ST_RECIEPT))
									{
										htmltext = "30035-04a.html";
									}
									break;
								}
								case 2:
								case 3:
								{
									if (st.hasQuestItems(HARRYS_2ND_RECIEPT))
									{
										htmltext = "30035-04.html";
									}
									break;
								}
								case 4:
								{
									if (st.hasQuestItems(HARRYS_2ND_RECIEPT, TOOL_BOX))
									{
										st.giveItems(WOODEN_BREASTPLATE, 1);
										st.addExpAndSp(5000, 0);
										st.exitQuest(false, true);
										htmltext = "30035-05.html";
									}
									break;
								}
							}
							break;
						}
						case State.COMPLETED:
						{
							htmltext = getAlreadyCompletedMsg(player);
							break;
						}
					}
					break;
				}
				case ALTRAN:
				{
					switch (st.getCond())
					{
						case 1:
						{
							if (st.hasQuestItems(HARRYS_1ST_RECIEPT))
							{
								htmltext = "30283-01.html";
							}
							break;
						}
						case 2:
						{
							if (st.hasQuestItems(HARRYS_2ND_RECIEPT) && (st.getQuestItemsCount(GOLEM_SHARD) < 5))
							{
								htmltext = "30283-03.html";
							}
							break;
						}
						case 3:
						{
							if (st.hasQuestItems(HARRYS_2ND_RECIEPT) && (st.getQuestItemsCount(GOLEM_SHARD) >= 5))
							{
								st.takeItems(GOLEM_SHARD, -1);
								st.giveItems(TOOL_BOX, 1);
								st.setCond(4, true);
								htmltext = "30283-04.html";
							}
							break;
						}
						case 4:
						{
							if (st.hasQuestItems(HARRYS_2ND_RECIEPT, TOOL_BOX))
							{
								htmltext = "30283-05.html";
							}
							break;
						}
					}
					break;
				}
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00152_ShardsOfGolem(152, Q00152_ShardsOfGolem.class.getSimpleName(), "Shards of Golem");
	}
}