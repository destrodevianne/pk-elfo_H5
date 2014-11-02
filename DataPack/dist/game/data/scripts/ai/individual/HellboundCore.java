package ai.individual;

import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import ai.npc.AbstractNpcAI;

/**
 * Manages Naia's cast on the Hellbound Core
 */
public class HellboundCore extends AbstractNpcAI
{
	private static final int NAIA = 18484;
	private static final int HELLBOUND_CORE = 32331;
	
	private static SkillHolder BEAM = new SkillHolder(5493, 1);
	
	private HellboundCore(String name, String descr)
	{
		super(name, descr);
		addSpawnId(HELLBOUND_CORE);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("cast") && (HellboundManager.getInstance().getLevel() <= 6))
		{
			for (L2Character naia : npc.getKnownList().getKnownCharactersInRadius(900))
			{
				if ((naia != null) && naia.isMonster() && (((L2MonsterInstance) naia).getNpcId() == NAIA) && !naia.isDead())
				{
					naia.setTarget(npc);
					naia.doSimultaneousCast(BEAM.getSkill());
				}
			}
			startQuestTimer("cast", 10000, npc, null);
		}
		return null;
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		startQuestTimer("cast", 10000, npc, null);
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new HellboundCore(HellboundCore.class.getSimpleName(), "ai/individual");
	}
}