package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class TimakOrcOverlord extends AbstractNpcAI 
{
	private static final int TIMAK_ORC_OVERLORD = 20588;
	
	public TimakOrcOverlord(String name, String descr)
	{
		super(name, descr);
		addAttackId(TIMAK_ORC_OVERLORD);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == TIMAK_ORC_OVERLORD)
		{
			if (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ATTACK)
			{
				if (Rnd.get(100) < 50)
					npc.broadcastNpcSay("Dear ultimate power!!!");
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new TimakOrcOverlord(TimakOrcOverlord.class.getSimpleName(), "ai/group_template");
	}
}