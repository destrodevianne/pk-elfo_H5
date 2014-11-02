package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;

public class Darnel extends AbstractNpcAI
{
	private static final int DARNEL = 25531;

	public Darnel(String name, String descr)
	{
		super(name, descr);
		addKillId(DARNEL);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == DARNEL)
			addSpawn(32279, 152761, 145950, -12588, 0, false, 0, false, player.getInstanceId());

		return "";
	}

	public static void main(String[] args)
	{
		new Darnel(Darnel.class.getSimpleName(), "ai/individual");
	}
}