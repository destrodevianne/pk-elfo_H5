package events.ChristmasIsHere;

import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.QuestState;

import events.EventsConfig;

public class ChristmasIsHere extends Quest
{
	private static final String qn = "ChristmasIsHere";
	
	/**
	 * List of Event Npc
	 */
	private static final int SANTA_CLAUS = 13285;
	/**
	 * List of Event Monsters
	 */
	private static final int[] EventMonsters =
	{
		7000,
		7001,
		7002,
		7003,
		7004,
		7005,
		7006,
		7007,
		7008,
		7009,
		7010,
		7011,
		7012,
		7013,
		7014,
		7015,
		7016,
		7017,
		7018,
		7019,
		7020,
		7021,
		7022,
		7023
	};
	
	/**
	 * On First Talk Script
	 */
	@Override
	public final String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
		{
			st = newQuestState(player);
		}
		String htmltext = "";
		if (EventsConfig.CH_STARTED)
		{
			htmltext = "<html><body>Santa:<br>Ho ho ho !! Feliz Natal! Voce quer ter coisas boas ou mas?<br>Voce deve ver o que eu posso te dar.<br>";
			htmltext += "<a action=\"bypass -h Quest ChristmasIsHere getprizes\">Procurar presente para mim</a><br>";
			htmltext += "<a action=\"bypass -h Quest ChristmasIsHere info\">Como posso obter premios.</a><br></body></html>";
		}
		else
		{
			htmltext = EventsConfig.EVENT_DISABLED;
		}
		return htmltext;
	}
	
	/**
	 * On Advanced Event Script
	 */
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
		{
			st = newQuestState(player);
		}
		String htmltext = "";
		if (event.equalsIgnoreCase("getprizes"))
		{
			htmltext = "prizes.htm";
		}
		else if (event.equalsIgnoreCase("info"))
		{
			htmltext = "<html><body>Santa:<br>Oh nao, eu nao tenho as minhas meias. Voce deve ir para o calaboucxo escuro (Mission Master) e obter as minhas meias, entao voce pode obter um dos meus premios.<br>";
			htmltext += "<br></body></html>";
		}
		return htmltext;
	}
	
	/**
	 * On Kill Monster Script
	 */
	@Override
	public final String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
		{
			st = newQuestState(player);
		}
		int npcId = npc.getNpcId();
		if (EventsConfig.CH_ACTIVE_DROP)
		{
			for (int ID : EventMonsters)
			{
				if (npcId == ID)
				{
					st.giveItems(EventsConfig.CH_CHRISTMAS_SOCK, 1);
				}
			}
		}
		return super.onKill(npc, player, isPet);
	}
	
	public ChristmasIsHere(int questId, String name, String descr)
	{
		super(questId, name, descr);
		addStartNpc(SANTA_CLAUS);
		addFirstTalkId(SANTA_CLAUS);
		addTalkId(SANTA_CLAUS);
		for (int MONSTER : EventMonsters)
		{
			addKillId(MONSTER);
		}
	}
	
	public static void main(String[] args)
	{
		new ChristmasIsHere(-1, qn, "events");
		if (EventsConfig.CH_STARTED)
		{
			_log.warning("Event System: Christmas Evento LIDO ...");
		}
	}
}