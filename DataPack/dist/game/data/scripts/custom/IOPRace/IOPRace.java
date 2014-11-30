package custom.IOPRace;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.skills.L2Skill;

/**
 * IOP Race AI.
 */
public class IOPRace extends Quest
{
	final private static int RIGNOS = 32349;
	final private static int STAMP = 10013;
	final private static int KEY = 9694;
	
	private int _player = -1;
	
	public IOPRace(int id, String name, String descr)
	{
		super(id, name, descr);
		addStartNpc(RIGNOS);
		addTalkId(RIGNOS);
		addFirstTalkId(RIGNOS);
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		if (player.getLevel() < 78)
		{
			return "32349-notavailable.htm";
		}
		else if ((_player != -1) && (_player == player.getObjectId()) && (st.getQuestItemsCount(STAMP) == 4))
		{
			return "32349-return.htm";
		}
		else if (_player != -1)
		{
			return "32349-notavailable.htm";
		}
		npc.showChatWindow(player);
		return null;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		if (_player == -1)
		{
			// clean old data
			player.stopSkillEffects(5239);
			if (player.hasSummon())
			{
				player.getSummon().stopSkillEffects(5239);
			}
			
			st.takeItems(STAMP, -1);
			st.set("1st", "0");
			st.set("2nd", "0");
			st.set("3rd", "0");
			st.set("4th", "0");
			
			L2Skill skill = L2Skill.valueOf(5239, 5);
			if (skill != null)
			{
				skill.getEffects(npc, player);
				if (player.hasSummon())
				{
					skill.getEffects(npc, player.getSummon());
				}
			}
			startQuestTimer("timer", 1800000, null, null); // 30 min
			_player = player.getObjectId();
		}
		return null;
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		
		if (event.equalsIgnoreCase("timer"))
		{
			_player = -1;
			return null;
		}
		else if (event.equalsIgnoreCase("finish"))
		{
			if (_player == player.getObjectId())
			{
				QuestState st = player.getQuestState(getName());
				st.giveItems(KEY, 3);
				st.takeItems(STAMP, -1);
				st.exitQuest(true);
			}
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new IOPRace(-1, "IOPRace", "custom");
	}
}