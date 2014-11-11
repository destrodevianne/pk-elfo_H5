package pk.elfo.gameserver.scripts.data;

import pk.elfo.gameserver.instancemanager.SoIManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;

public class AbyssGaze extends Quest
{
	public AbyssGaze(int id, String name, String desc)
	{
		super(id, name, desc);
		
		addStartNpc(32540);
		addFirstTalkId(32540);
		addTalkId(32540);
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
		
		if (event.equalsIgnoreCase("request_permission"))
		{
			if ((SoIManager.getCurrentStage() == 2) || (SoIManager.getCurrentStage() == 5))
			{
				htmltext = "32540-2.htm";
			}
			else if ((SoIManager.getCurrentStage() == 3) && SoIManager.isSeedOpen())
			{
				htmltext = "32540-3.htm";
			}
			else
			{
				htmltext = "32540-1.htm";
			}
		}
		else if (event.equalsIgnoreCase("enter_seed"))
		{
			if (SoIManager.getCurrentStage() == 3)
			{
				SoIManager.teleportInSeed(player);
				return null;
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
		
		if (npc.getNpcId() == 32540)
		{
			return "32540.htm";
		}
		return "";
	}
	
	public static void main(String[] args)
	{
		new AbyssGaze(-1, AbyssGaze.class.getSimpleName(), "Abyss Gaze");
	}
}