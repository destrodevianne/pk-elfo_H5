package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class TurekOrcSupplier extends AbstractNpcAI 
{
	private static final int TUREK_ORC_SUPPLIER = 20498;
	
	public TurekOrcSupplier(String name, String descr)
	{
		super(name, descr);
		addAttackId(TUREK_ORC_SUPPLIER);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == TUREK_ORC_SUPPLIER)
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
				npc.broadcastNpcSay("You wont take me down easily.");
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new TurekOrcSupplier(TurekOrcSupplier.class.getSimpleName(), "ai");
	}
}