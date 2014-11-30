package ai.individual.kamaloka;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;


public class FollowerOfAllosce extends AbstractNpcAI
{
	private static final int FOFALLOSCE = 18568;

	public FollowerOfAllosce(String name, String descr)
	{
		super(name, descr);
		addAggroRangeEnterId(FOFALLOSCE);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_to_skill"))
		{
			npc.setTarget(player);
			npc.doCast(L2Skill.valueOf(5624, 1));
			startQuestTimer("time_to_skill", 30000, npc, player);
		}
		return "";
	}

	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == FOFALLOSCE)
		{
			npc.setIsInvul(true);
			startQuestTimer("time_to_skill", 30000, npc, player);
			npc.setTarget(player);
			npc.doCast(L2Skill.valueOf(5624, 1));
		}
		return "";
	}

	public static void main(String[] args)
	{
		new FollowerOfAllosce(FollowerOfAllosce.class.getSimpleName(), "ai/individual");
	}
}