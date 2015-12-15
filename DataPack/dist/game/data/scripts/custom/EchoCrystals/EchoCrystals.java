package custom.EchoCrystals;

import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastMap;

/**
 * Echo Crystals AI.<br>
 * Original Jython script by DrLecter, formerly based on Elektra's script.
 * @author Plim
 */
public class EchoCrystals extends Quest
{
	private final static int[] NPCs =
	{
		31042,
		31043
	};
	
	private static final int ADENA = 57;
	private static final int COST = 200;
	
	private static final Map<Integer, ScoreData> SCORES = new FastMap<>();
	
	private class ScoreData
	{
		private final int crystalId;
		private final String okMsg;
		private final String noAdenaMsg;
		private final String noScoreMsg;
		
		public ScoreData(int crystalId, String okMsg, String noAdenaMsg, String noScoreMsg)
		{
			super();
			this.crystalId = crystalId;
			this.okMsg = okMsg;
			this.noAdenaMsg = noAdenaMsg;
			this.noScoreMsg = noScoreMsg;
		}
		
		public int getCrystalId()
		{
			return crystalId;
		}
		
		public String getOkMsg()
		{
			return okMsg;
		}
		
		public String getNoAdenaMsg()
		{
			return noAdenaMsg;
		}
		
		public String getNoScoreMsg()
		{
			return noScoreMsg;
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = "";
		QuestState st = player.getQuestState(EchoCrystals.class.getSimpleName());
		
		if ((st != null) && Util.isDigit(event))
		{
			int score = Integer.parseInt(event);
			if (SCORES.containsKey(score))
			{
				int crystal = SCORES.get(score).getCrystalId();
				String ok = SCORES.get(score).getOkMsg();
				String noadena = SCORES.get(score).getNoAdenaMsg();
				String noscore = SCORES.get(score).getNoScoreMsg();
				
				if (!st.hasQuestItems(score))
				{
					htmltext = npc.getNpcId() + "-" + noscore + ".htm";
				}
				else if (st.getQuestItemsCount(ADENA) < COST)
				{
					htmltext = npc.getNpcId() + "-" + noadena + ".htm";
				}
				else
				{
					st.takeItems(ADENA, COST);
					st.giveItems(crystal, 1);
					htmltext = npc.getNpcId() + "-" + ok + ".htm";
				}
			}
		}
		else
		{
			return htmltext;
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		return "1.htm";
	}
	
	public EchoCrystals(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		// Initialize Map
		SCORES.put(4410, new ScoreData(4411, "01", "02", "03"));
		SCORES.put(4409, new ScoreData(4412, "04", "05", "06"));
		SCORES.put(4408, new ScoreData(4413, "07", "08", "09"));
		SCORES.put(4420, new ScoreData(4414, "10", "11", "12"));
		SCORES.put(4421, new ScoreData(4415, "13", "14", "15"));
		SCORES.put(4419, new ScoreData(4417, "16", "05", "06"));
		SCORES.put(4418, new ScoreData(4416, "17", "05", "06"));
		
		for (int npc : NPCs)
		{
			addStartNpc(npc);
			addTalkId(npc);
		}
	}
	
	public static void main(String[] args)
	{
		new EchoCrystals(-1, EchoCrystals.class.getSimpleName(), "custom");
	}
}