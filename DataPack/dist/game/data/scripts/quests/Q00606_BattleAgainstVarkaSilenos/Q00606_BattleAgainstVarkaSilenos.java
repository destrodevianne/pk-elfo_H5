package quests.Q00606_BattleAgainstVarkaSilenos;

import java.util.HashMap;
import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;

/**
 * Battle against Varka Silenos (606)
 * @author malyelfik
 */
public class Q00606_BattleAgainstVarkaSilenos extends Quest
{
	// NPC
	private static final int KADUN = 31370;
	// Monsters
	private static final Map<Integer, Integer> MOBS = new HashMap<>();
	static
	{
		MOBS.put(21350, 500); // Varka Silenos Recruit
		MOBS.put(21353, 510); // Varka Silenos Scout
		MOBS.put(21354, 522); // Varka Silenos Hunter
		MOBS.put(21355, 519); // Varka Silenos Shaman
		MOBS.put(21357, 529); // Varka Silenos Priest
		MOBS.put(21358, 529); // Varka Silenos Warrior
		MOBS.put(21360, 539); // Varka Silenos Medium
		MOBS.put(21362, 539); // Varka Silenos Officer
		MOBS.put(21364, 558); // Varka Silenos Seer
		MOBS.put(21365, 568); // Varka Silenos Great Magus
		MOBS.put(21366, 568); // Varka Silenos General
		MOBS.put(21368, 568); // Varka Silenos Great Seer
		MOBS.put(21369, 664); // Varka's Commander
		MOBS.put(21371, 713); // Varka's Head Magus
		MOBS.put(21373, 738); // Varka's Prophet
	}
	// Items
	private static final int HORN = 7186;
	private static final int MANE = 7233;
	// Misc
	private static final int MIN_LEVEL = 74;
	private static final int MANE_COUNT = 100;
	
	private Q00606_BattleAgainstVarkaSilenos(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(KADUN);
		addTalkId(KADUN);
		addKillId(MOBS.keySet());
		registerQuestItems(MANE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return null;
		}
		
		String htmltext = event;
		switch (event)
		{
			case "31370-03.htm":
				st.startQuest();
				break;
			case "31370-06.html":
				break;
			case "31370-07.html":
				if (st.getQuestItemsCount(MANE) < MANE_COUNT)
				{
					return "31370-08.html";
				}
				st.takeItems(MANE, MANE_COUNT);
				st.giveItems(HORN, 20);
				break;
			case "31370-09.html":
				st.exitQuest(true, true);
				break;
			default:
				htmltext = null;
				break;
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final L2PcInstance member = getRandomPartyMember(killer, 1);
		if ((member != null) && (getRandom(1000) < MOBS.get(npc.getNpcId())))
		{
			final QuestState st = member.getQuestState(getName());
			st.giveItems(MANE, 1);
			st.playSound(QuestSound.ITEMSOUND_QUEST_ITEMGET);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = player.getQuestState(getName());
		if (st == null)
		{
			return htmltext;
		}
		
		switch (st.getState())
		{
			case State.CREATED:
				htmltext = (player.getLevel() >= MIN_LEVEL) ? "31370-01.htm" : "31370-02.htm";
				break;
			case State.STARTED:
				htmltext = (st.hasQuestItems(MANE)) ? "31370-04.html" : "31370-05.html";
				break;
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Q00606_BattleAgainstVarkaSilenos(606, Q00606_BattleAgainstVarkaSilenos.class.getSimpleName(), "Battle against Varka Silenos");
	}
}