package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Mutation extends AbstractNpcAI 
{
	private static final int CHANCE = 1; // 1% chance
	private static final String[] NPCS_MESSAGES =
	{
		"Eu nao posso desprezar o companheiro! Vejo sua sinceridade no duelo.",
		"Agora vamos realmente comecar!",
		"Tolo! Agora e so pratica!",
        "De uma olhada em minha verdadeira forca.",
        "Desta vez, no ultimo! o fim!"
	};
	
	private static final int[][] MUTATION_IDS =
	{
	//  old ID, new ID
		{20835, 21608},
        {21608, 21609},
		{20832, 21602},
		{21602, 21603},
		{20833, 21605},
		{21605, 21606},
		{21625, 21623},
		{21623, 21624},
		{20842, 21620},
		{21620, 21621}
	}; 

	/**
	 * @param name
	 * @param descr
	 */
	public Mutation(String name, String descr)
	{
		super(name, descr);
		for (int npcId : MUTATION_IDS[0])
		{
			addAttackId(npcId);
		}
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		final int npcId = npc.getNpcId();
		if (Rnd.get(100) <= CHANCE)
		{
			int arrId = getContainsId(MUTATION_IDS[0], npcId);
			if (!(arrId < 0))
			{
				npc.broadcastNpcSay(NPCS_MESSAGES[4]);
				npc.deleteMe();
				L2Npc newNpc = addSpawn(MUTATION_IDS[1][arrId], npc);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				
				return super.onAttack(newNpc, player, damage, isPet);
			}
		}
		return super.onAttack(npc, player, damage, isPet);
	}

	public static void main(String[] args)
	{
		new Mutation(Mutation.class.getSimpleName(), "ai");
	}
}