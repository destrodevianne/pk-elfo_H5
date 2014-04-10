package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;


public class FollowerOfMontagnar extends AbstractNpcAI 
{
	private static final int FOFMONTAGNAR = 18569;

	public FollowerOfMontagnar(String name, String descr)
	{
		super(name, descr);
		addAggroRangeEnterId(FOFMONTAGNAR);
	}

	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == FOFMONTAGNAR)
			npc.setIsInvul(true);

		return "";
	}

	public static void main(String[] args)
	{
		new FollowerOfMontagnar(FollowerOfMontagnar.class.getSimpleName(), "ai/individual");
	}
}