package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class TurekOrcFootman extends AbstractNpcAI 
{
	private static final int TUREK_ORC_FOOTMAN = 20499;
	
	public TurekOrcFootman(String name, String descr)
	{
		super(name, descr);
		addAttackId(TUREK_ORC_FOOTMAN);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == TUREK_ORC_FOOTMAN)
		{
			if (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
			{
				if (Rnd.get(100) < 70)
				{
					npc.broadcastNpcSay("We shall see about that!");
				}
			}
			else if (Rnd.get(100) < 10)
			{
				npc.broadcastNpcSay("There is no reason for you to kill me! I have nothing you need!");
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new TurekOrcFootman(TurekOrcFootman.class.getSimpleName(), "ai/group_template");
	}
}