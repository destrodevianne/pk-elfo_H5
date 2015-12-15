package ai.individual;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class FleeNpc extends AbstractNpcAI
{
	private static final int[] MOBS =
	{
		25604, // Mutated Elpy
		20432, // Elpy
		22228, // Grey Elpy
		18150, // Victim
		18151, // Victim
		18152, // Victim
		18153, // Victim
		18154, // Victim
		18155, // Victim
		18156, // Victim
		18157 // Victim
	};
	
	private FleeNpc(String name, String descr)
	{
		super(name, descr);
		addAttackId(MOBS);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if ((npc.getNpcId() >= 18150) && (npc.getNpcId() <= 18157))
		{
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition((npc.getX() + getRandom(-40, 40)), (npc.getY() + getRandom(-40, 40)), npc.getZ(), npc.getHeading()));
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
			return null;
		}
		else if ((npc.getNpcId() == 20432) || (npc.getNpcId() == 22228) || (npc.getNpcId() == 25604))
		{
			if (getRandom(3) == 2)
			{
				npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition((npc.getX() + getRandom(-200, 200)), (npc.getY() + getRandom(-200, 200)), npc.getZ(), npc.getHeading()));
			}
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
			return null;
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	// Register the new Script at the Script System
	public static void main(String[] args)
	{
		new FleeNpc(FleeNpc.class.getSimpleName(), "ai");
	}
}