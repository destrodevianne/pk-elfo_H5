package quests.Q456_DontKnowDontCare;

import pk.elfo.gameserver.model.L2CommandChannel;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.model.quest.QuestState.QuestType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.Rnd;

public class Q456_DontKnowDontCare extends Quest
{
	private static final int[] SEPARATED_SOUL =
	{
		32864,
		32865,
		32866,
		32867,
		32868,
		32869,
		32870,
		32891
	};
	
	// Item's
	private static final int DRAKE_LORD_ESSENCE = 17251;
	private static final int BEHEMOTH_LEADER_ESSENCE = 17252;
	private static final int DRAGON_BEAST_ESSENCE = 17253;
	
	// Raidboss
	private static final int DRAKE_LEADER = 25725;
	private static final int BEHEMOTH_LEADER = 25726;
	private static final int DRAGON_BEAST = 25727;
	
	// Raid Npcs
	private static final int DRAKE_LEADER_NPC = 32884;
	private static final int BEHEMOTH_LEADER_NPC = 32885;
	private static final int DRAGON_BEAST_NPC = 32886;
	private static final int MIN_PLAYERS = 18;
	// Reward
	private static final int[][] REWARD =
	{
		{
			15558,
			15559,
			15560,
			15561,
			15562,
			15563,
			15564,
			15565,
			15566,
			15567,
			15567,
			15569,
			15570,
			15571
		},
		{
			15750,
			15753,
			15756,
			15745,
			15748,
			15751,
			15754,
			15757,
			15759,
			15743,
			15746,
			15749,
			15752,
			15755,
			15758,
			15744,
			15747
		},
		{
			15765,
			15764,
			15763
		},
		{
			9552,
			9553,
			9554,
			9555,
			9557,
			9556,
			6577,
			6578,
			959,
			2134
		}
	};
	
	private boolean checkConditions(L2PcInstance player)
	{
		final L2Party party = player.getParty();
		if (party == null)
		{
			player.sendPacket(SystemMessageId.NOT_IN_PARTY_CANT_ENTER);
			return false;
		}
		final L2CommandChannel channel = player.getParty().getCommandChannel();
		if (channel == null)
		{
			player.sendPacket(SystemMessageId.NOT_IN_COMMAND_CHANNEL_CANT_ENTER);
			return false;
		}
		else if (channel.getLeader() != player)
		{
			player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
			return false;
		}
		else if (channel.getMemberCount() < MIN_PLAYERS)
		{
			player.sendPacket(SystemMessageId.PARTY_EXCEEDED_THE_LIMIT_CANT_ENTER);
			return false;
		}
		return true;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return htmltext;
		}
		
		if (event.equalsIgnoreCase("accept"))
		{
			st.setState(State.STARTED);
			st.set("cond", "1");
			st.playSound("ItemSound.quest_accept");
			htmltext = "DontKnowDontCare-07.htm";
		}
		else if (event.equalsIgnoreCase("reward"))
		{
			switch (npc.getNpcId())
			{
				case DRAKE_LEADER_NPC:
					if (st.hasQuestItems(DRAKE_LORD_ESSENCE))
					{
						player.sendMessage("You already have this Essence");
					}
					else if (!checkConditions(player))
					{
						player.sendMessage("You can't take the reward. You need to be in a party and command channel with min 18 players.");
					}
					else
					{
						st.playSound("ItemSound.quest_itemget");
						st.giveItems(DRAKE_LORD_ESSENCE, 1);
					}
					break;
				case BEHEMOTH_LEADER_NPC:
					if (st.hasQuestItems(BEHEMOTH_LEADER_ESSENCE))
					{
						player.sendMessage("You already have this Essence");
					}
					else if (!checkConditions(player))
					{
						player.sendMessage("You can't take the reward. You need to be in a party and command channel with min 18 players.");
					}
					else
					{
						st.playSound("ItemSound.quest_itemget");
						st.giveItems(BEHEMOTH_LEADER_ESSENCE, 1);
					}
					break;
				case DRAGON_BEAST_NPC:
					if (st.hasQuestItems(DRAGON_BEAST_ESSENCE))
					{
						player.sendMessage("You already have this Essence");
					}
					else if (!checkConditions(player))
					{
						player.sendMessage("You can't take the reward. You need to be in a party and command channel with min 18 players.");
					}
					else
					{
						st.playSound("ItemSound.quest_itemget");
						st.giveItems(DRAGON_BEAST_ESSENCE, 1);
					}
					break;
			}
			
			if (st.hasQuestItems(BEHEMOTH_LEADER_ESSENCE) && st.hasQuestItems(DRAGON_BEAST_ESSENCE) && st.hasQuestItems(DRAKE_LORD_ESSENCE))
			{
				st.playSound("ItemSound.quest_middle");
				st.set("cond", "2");
			}
			htmltext = null;
		}
		return htmltext;
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
		
		if (Util.contains(new int[]
		{
			BEHEMOTH_LEADER_NPC,
			DRAGON_BEAST_NPC,
			DRAKE_LEADER_NPC
		}, npc.getNpcId()))
		{
			if (st.getInt("cond") == 1)
			{
				htmltext = "takereward.htm";
			}
			else
			{
				htmltext = "notakereward.htm";
			}
		}
		else if (Util.contains(SEPARATED_SOUL, npc.getNpcId()))
		{
			switch (st.getState())
			{
				case State.CREATED:
					if (player.getLevel() >= 80)
					{
						htmltext = "DontKnowDontCare-01.htm";
					}
					else
					{
						htmltext = "DontKnowDontCare-03.htm";
					}
					break;
				case State.STARTED:
					if (st.getInt("cond") == 1)
					{
						htmltext = "DontKnowDontCare-08.htm";
					}
					else if (st.getInt("cond") == 2)
					{
						st.playSound("ItemSound.quest_finish");
						st.takeItems(DRAKE_LORD_ESSENCE, 1);
						st.takeItems(BEHEMOTH_LEADER_ESSENCE, 1);
						st.takeItems(DRAGON_BEAST_ESSENCE, 1);
						rewardPlayer(player);
						htmltext = "DontKnowDontCare-10.htm";
						st.unset("cond");
						st.exitQuest(QuestType.DAILY);
					}
					else
					{
						htmltext = "DontKnowDontCare-09.htm";
					}
					break;
				case State.COMPLETED:					
					{
						if (!st.isNowAvailable())
						{
							htmltext = "DontKnowDontCare-02.htm";
						}
						else
						{
							st.setState(State.CREATED);
							if (player.getLevel() >= 80)
							{
								htmltext = "DontKnowDontCare-01.htm";
							}
						}
					break;
					}
			}
			return htmltext;
		}
	}
	private void rewardPlayer(L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		
		int itemId = 0, count = 1, random = Rnd.get(100);
		
		if (random < 10)
		{
			itemId = REWARD[0][Rnd.get(REWARD[0].length)];
		}
		else if (random < 30)
		{
			itemId = REWARD[1][Rnd.get(REWARD[1].length)];
		}
		else if (random < 50)
		{
			itemId = REWARD[2][Rnd.get(REWARD[2].length)];
		}
		else
		{
			itemId = REWARD[3][Rnd.get(REWARD[3].length)];
			count = Rnd.get(1, 2);
		}
		st.giveItems(itemId, count);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		QuestState st = player.getQuestState(getName());
		
		if (st == null)
		{
			return null;
		}
		
		if (st.getInt("cond") == 1)
		{
			Location loc = npc.getLocation();
			
			switch (npc.getNpcId())
			{
				case DRAKE_LEADER:
					addSpawn(DRAKE_LEADER_NPC, loc, false, 60000, true);
					break;
				case BEHEMOTH_LEADER:
					addSpawn(BEHEMOTH_LEADER_NPC, loc, false, 60000, true);
					break;
				case DRAGON_BEAST:
					addSpawn(DRAGON_BEAST_NPC, loc, false, 60000, true);
					break;
			}
		}
		return null;
	}
	
	public Q456_DontKnowDontCare(int questId, String name, String descr)
	{
		super(questId, name, descr);
		for (int npc : SEPARATED_SOUL)
		{
			addStartNpc(npc);
			addTalkId(npc);
		}
		addTalkId(DRAKE_LEADER_NPC, BEHEMOTH_LEADER_NPC, DRAGON_BEAST_NPC);
		addKillId(DRAKE_LEADER, BEHEMOTH_LEADER, DRAGON_BEAST);
	}
	
	public static void main(String[] args)
	{
		new Q456_DontKnowDontCare(456, Q456_DontKnowDontCare.class.getSimpleName(), "Dont Know, Dont Care");
	}
}
