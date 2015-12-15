package ai.npc.BlackMarketeerOfMammon;

import java.util.Calendar;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.model.quest.QuestState.QuestType;
import ai.npc.AbstractNpcAI;

/**
 * Black Marketeer of Mammon - Exchange Adena for AA.
 */
public class BlackMarketeerOfMammon extends AbstractNpcAI
{
	// NPC
	private static final int BLACK_MARKETEER = 31092;
	// Misc
	private static final int MIN_LEVEL = 60;
	
	private BlackMarketeerOfMammon(String name, String descr)
	{
		super(name, descr);
		
		addStartNpc(BLACK_MARKETEER);
		addTalkId(BLACK_MARKETEER);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance talker)
	{
		return exchangeAvailable() ? "31092-01.html" : "31092-02.html";
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState qs = player.getQuestState(getName());
		if ("exchange".equals(event))
		{
			if (exchangeAvailable())
			{
				if (player.getLevel() >= MIN_LEVEL)
				{
					if (!qs.isNowAvailable())
					{
						htmltext = "31092-03.html";
					}
					else
					{
						if (player.getAdena() >= 2000000)
						{
							qs.setState(State.STARTED);
							takeItems(player, PcInventory.ADENA_ID, 2000000);
							giveItems(player, PcInventory.ANCIENT_ADENA_ID, 500000);
							htmltext = "31092-04.html";
							qs.exitQuest(QuestType.DAILY, false);
						}
						else
						{
							htmltext = "31092-05.html";
						}
					}
				}
				else
				{
					htmltext = "31092-06.html";
				}
			}
			else
			{
				htmltext = "31092-02.html";
			}
		}
		return htmltext;
	}
	
	private boolean exchangeAvailable()
	{
		Calendar currentTime = Calendar.getInstance();
		Calendar minTime = Calendar.getInstance();
		minTime.set(Calendar.HOUR_OF_DAY, 20);
		minTime.set(Calendar.MINUTE, 0);
		minTime.set(Calendar.SECOND, 0);
		Calendar maxtTime = Calendar.getInstance();
		maxtTime.set(Calendar.HOUR_OF_DAY, 23);
		maxtTime.set(Calendar.MINUTE, 59);
		maxtTime.set(Calendar.SECOND, 59);
		
		return (currentTime.compareTo(minTime) >= 0) && (currentTime.compareTo(maxtTime) <= 0);
	}
	
	public static void main(String[] args)
	{
		new BlackMarketeerOfMammon(BlackMarketeerOfMammon.class.getSimpleName(), "ai/npc");
	}
}