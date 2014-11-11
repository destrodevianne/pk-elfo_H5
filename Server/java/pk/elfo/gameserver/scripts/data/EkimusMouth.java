package pk.elfo.gameserver.scripts.data;

import pk.elfo.gameserver.instancemanager.SoIManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;

public class EkimusMouth extends Quest
{
	public EkimusMouth(int id, String name, String desc)
	{
		super(id, name, desc);
		
		addStartNpc(32537);
		addFirstTalkId(32537);
		addTalkId(32537);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		if (event.equalsIgnoreCase("hos_enter"))
		{
			if (SoIManager.getCurrentStage() == 1)
			{
				htmltext = "32537-1.htm";
			}
			else if (SoIManager.getCurrentStage() == 4)
			{
				htmltext = "32537-2.htm";
			}
		}
		else if (event.equalsIgnoreCase("hoe_enter"))
		{
			if (SoIManager.getCurrentStage() == 1)
			{
				htmltext = "32537-3.htm";
			}
			else if (SoIManager.getCurrentStage() == 4)
			{
				htmltext = "32537-4.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		if (npc.getNpcId() == 32537)
		{
			return "32537.htm";
		}
		return "";
	}
	
	public static void main(String[] args)
	{
		new EkimusMouth(-1, EkimusMouth.class.getSimpleName(), "custom");
	}
}