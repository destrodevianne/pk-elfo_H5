package ai.npc.Teleports.DelusionTeleport;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
 
/**
 * Projeto PkElfo
 */

public class DelusionTeleport extends Quest
{
	private final static int REWARDER_ONE   = 32658;
	private final static int REWARDER_TWO   = 32659;
	private final static int REWARDER_THREE = 32660;
	private final static int REWARDER_FOUR  = 32661;
	private final static int REWARDER_FIVE  = 32663;
	private final static int REWARDER_SIX   = 32662;
	private final static int START_NPC      = 32484;
	private int x;
	private int y;
	private int z;
	
	/**
	 * Delusion Teleport 
	 * @author d0S
	 * @param questId 
	 * @param name 
	 * @param descr 
	 */
	
	public DelusionTeleport(int questId, String name, String descr)
	{
		super(questId, name, descr);
			addStartNpc(START_NPC);
			addTalkId(START_NPC);
			addTalkId(REWARDER_ONE);
			addTalkId(REWARDER_TWO);
			addTalkId(REWARDER_THREE);
			addTalkId(REWARDER_FOUR);
			addTalkId(REWARDER_FIVE);
			addTalkId(REWARDER_SIX);
	}

	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(getName());
		int npcId = npc.getNpcId();
		if (npcId == START_NPC)
		{
			x = player.getX();
			y = player.getY();
			z = player.getZ();
			player.teleToLocation(-114592,-152509,-6723);
			if (player.getSummon() != null)
			player.getSummon().teleToLocation(-114592,-152509,-6723);
		}
		else if (npcId == REWARDER_ONE || npcId == REWARDER_TWO || npcId == REWARDER_THREE || npcId == REWARDER_FOUR || npcId == REWARDER_FIVE || npcId == REWARDER_SIX)
		{	
			player.teleToLocation(x,y,z);
			if (player.getSummon() != null)
			player.getSummon().teleToLocation(x,y,z);
			st.exitQuest(true);
		} 
		return "";
	}
	public static void main(String[] args)
	{
		new DelusionTeleport(-1, "DelusionTeleport", "teleports");
	}
}