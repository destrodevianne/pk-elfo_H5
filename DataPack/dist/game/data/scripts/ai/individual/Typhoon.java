package ai.individual;

import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2RaidBossInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import ai.npc.AbstractNpcAI;

public class Typhoon extends AbstractNpcAI
{
	private static final int TYPHOON = 25539;
	private static SkillHolder STORM = new SkillHolder(5434, 1);
	private Typhoon(String name, String descr)
	{
		super(name, descr);
		addAggroRangeEnterId(TYPHOON);
		addSpawnId(TYPHOON);
		final L2RaidBossInstance boss = RaidBossSpawnManager.getInstance().getBosses().get(TYPHOON);
		if (boss != null)
		{
			onSpawn(boss);
		}
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("cast") && (npc != null) && !npc.isDead())
		{
			npc.doSimultaneousCast(STORM.getSkill());
			startQuestTimer("cast", 5000, npc, null);
		}
		return null;
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		npc.doSimultaneousCast(STORM.getSkill());
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (!npc.isTeleporting())
		{
			startQuestTimer("cast", 5000, npc, null);
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new Typhoon(Typhoon.class.getSimpleName(), "ai/individual");
	}
}