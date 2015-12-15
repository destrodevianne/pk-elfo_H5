package ai.group_template;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class EvasGiftBoxes extends AbstractNpcAI
{
	private static final int GIFTBOX = 32342;
	
	private static final int KISSOFEVA = 1073;
	
	// index 0: without kiss of eva
	// index 1: with kiss of eva
	// chance,itemId,...
	private static final int[][] CHANCES =
	{
		{
			2,
			9692,
			1,
			9693
		},
		{
			100,
			9692,
			50,
			9693
		}
	};
	
	private EvasGiftBoxes(String name, String descr)
	{
		super(name, descr);
		addKillId(GIFTBOX);
		addSpawnId(GIFTBOX);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		npc.setIsNoRndWalk(true);
		return super.onSpawn(npc);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.getNpcId() == GIFTBOX)
		{
			int isKissOfEvaBuffed = 0;
			if (killer.getFirstEffect(KISSOFEVA) != null)
			{
				isKissOfEvaBuffed = 1;
			}
			for (int i = 0; i < CHANCES[isKissOfEvaBuffed].length; i += 2)
			{
				if (getRandom(100) < CHANCES[isKissOfEvaBuffed][i])
				{
					giveItems(killer, CHANCES[isKissOfEvaBuffed][i + 1], 1);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new EvasGiftBoxes(EvasGiftBoxes.class.getSimpleName(), "ai");
	}
}