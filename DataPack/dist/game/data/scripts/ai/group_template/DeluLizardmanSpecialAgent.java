package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class DeluLizardmanSpecialAgent extends AbstractNpcAI
{
	private static final int DELU_LIZARDMAN_SPECIAL_AGENT = 21105;
	
	public DeluLizardmanSpecialAgent(String name, String descr)
	{
		super(name, descr);
		addAttackId(DELU_LIZARDMAN_SPECIAL_AGENT);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == DELU_LIZARDMAN_SPECIAL_AGENT)
		{
			if (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
			{
				if (Rnd.get(100) < 70)
					npc.broadcastNpcSay("How dare you interrupt our fight! Hey guys, help!");
			}
			else if (Rnd.get(100) < 10)
				npc.broadcastNpcSay("Hey! Were having a duel here!");
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new DeluLizardmanSpecialAgent(DeluLizardmanSpecialAgent.class.getSimpleName(), "ai/group_template");
	}
}