package ai.npc.Tunatun;

import quests.Q00020_BringUpWithLove.Q00020_BringUpWithLove;
import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.QuestState;

/**
 * Beast Herder Tunatun AI.
 */
public class Tunatun extends AbstractNpcAI
{
	// NPC
	private static final int TUNATUN = 31537;
	// Item
	private static final int BEAST_HANDLERS_WHIP = 15473;
	// Misc
	private static final int MIN_LEVEL = 82;
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ("Whip".equals(event))
		{
			if (hasQuestItems(player, BEAST_HANDLERS_WHIP))
			{
				return "31537-01.html";
			}
			
			QuestState st = player.getQuestState(Q00020_BringUpWithLove.class.getSimpleName());
			if ((st == null) && (player.getLevel() < MIN_LEVEL))
			{
				return "31537-02.html";
			}
			else if ((st != null) || (player.getLevel() >= MIN_LEVEL))
			{
				giveItems(player, BEAST_HANDLERS_WHIP, 1);
				return "31537-03.html";
			}
		}
		return event;
	}
	
	private Tunatun(String name, String descr)
	{
		super(name, descr);
		addStartNpc(TUNATUN);
		addFirstTalkId(TUNATUN);
		addTalkId(TUNATUN);
	}
	
	public static void main(String[] args)
	{
		new Tunatun(Tunatun.class.getSimpleName(), "ai/npc");
	}
}