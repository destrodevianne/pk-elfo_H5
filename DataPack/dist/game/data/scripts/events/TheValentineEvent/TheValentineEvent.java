package events.TheValentineEvent;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.event.LongTimeEvent;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

public class TheValentineEvent extends LongTimeEvent
{
	// NPC
	private static final int NPC = 4301;
	// Item
	private static final int RECIPE = 20191;
	
	public TheValentineEvent(String name, String descr)
	{
		super(name, descr);
		addStartNpc(NPC);
		addFirstTalkId(NPC);
		addTalkId(NPC);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return getNoQuestMsg(player);
		}
		
		String htmltext = event;
		if (event.equalsIgnoreCase("4301-3.htm"))
		{
			if (st.isCompleted())
			{
				htmltext = "4301-4.htm";
			}
			else
			{
				st.giveItems(RECIPE, 1);
				st.playSound(QuestSound.ITEMSOUND_QUEST_ITEMGET);
				st.setState(State.COMPLETED);
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (player.getQuestState(getName()) == null)
		{
			newQuestState(player);
		}
		return npc.getNpcId() + ".htm";
	}
	
	public static void main(String[] args)
	{
		new TheValentineEvent(TheValentineEvent.class.getSimpleName(), "events");
	}
}