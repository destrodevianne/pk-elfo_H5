package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class SearchingMaster extends AbstractNpcAI
{
	private static final int[] MOBS =
	{
		20965,
		20966,
		20967,
		20968,
		20969,
		20970,
		20971,
		20972,
		20973
	};
	
	private SearchingMaster(String name, String descr)
	{
		super(name, descr);
		addAttackId(MOBS);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isSummon)
	{
		if (player == null)
		{
			return null;
		}
		
		npc.setIsRunning(true);
		((L2Attackable) npc).addDamageHate(player, 0, 999);
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
		
		return super.onAttack(npc, player, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SearchingMaster(SearchingMaster.class.getSimpleName(), "ai");
	}
}