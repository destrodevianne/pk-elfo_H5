package ai.individual;

import java.util.Map;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.skills.L2Skill;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class DemonPrince extends AbstractNpcAI
{
	private static final int DEMON_PRINCE = 25540;
	private static final int FIEND = 25541;
	
	private static final SkillHolder UD = new SkillHolder(5044, 2);
	private static final SkillHolder[] AOE =
	{
		new SkillHolder(5376, 4),
		new SkillHolder(5376, 5),
		new SkillHolder(5376, 6)
	};
	
	private static final Map<Integer, Boolean> ATTACK_STATE = new FastMap<>();
	
	private DemonPrince(String name, String descr)
	{
		super(name, descr);
		addAttackId(DEMON_PRINCE);
		addKillId(DEMON_PRINCE);
		addSpawnId(FIEND);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("cast") && (npc != null) && (npc.getNpcId() == FIEND) && !npc.isDead())
		{
			npc.doCast(AOE[getRandom(AOE.length)].getSkill());
		}
		return null;
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon, L2Skill skill)
	{
		if (!npc.isDead())
		{
			if (!ATTACK_STATE.containsKey(npc.getObjectId()) && (npc.getCurrentHp() < (npc.getMaxHp() * 0.5)))
			{
				npc.doCast(UD.getSkill());
				spawnMinions(npc);
				ATTACK_STATE.put(npc.getObjectId(), false);
			}
			else if ((npc.getCurrentHp() < (npc.getMaxHp() * 0.1)) && ATTACK_STATE.containsKey(npc.getObjectId()) && (ATTACK_STATE.get(npc.getObjectId()) == false))
			{
				npc.doCast(UD.getSkill());
				spawnMinions(npc);
				ATTACK_STATE.put(npc.getObjectId(), true);
			}
			
			if (getRandom(1000) < 10)
			{
				spawnMinions(npc);
			}
		}
		return super.onAttack(npc, attacker, damage, isSummon, skill);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		ATTACK_STATE.remove(npc.getObjectId());
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		if (npc.getNpcId() == FIEND)
		{
			startQuestTimer("cast", 15000, npc, null);
		}
		return super.onSpawn(npc);
	}
	
	private void spawnMinions(L2Npc master)
	{
		if ((master != null) && !master.isDead())
		{
			int instanceId = master.getInstanceId();
			int x = master.getX();
			int y = master.getY();
			int z = master.getZ();
			addSpawn(FIEND, x + 200, y, z, 0, false, 0, false, instanceId);
			addSpawn(FIEND, x - 200, y, z, 0, false, 0, false, instanceId);
			addSpawn(FIEND, x - 100, y - 140, z, 0, false, 0, false, instanceId);
			addSpawn(FIEND, x - 100, y + 140, z, 0, false, 0, false, instanceId);
			addSpawn(FIEND, x + 100, y - 140, z, 0, false, 0, false, instanceId);
			addSpawn(FIEND, x + 100, y + 140, z, 0, false, 0, false, instanceId);
		}
	}
	
	public static void main(String[] args)
	{
		new DemonPrince(DemonPrince.class.getSimpleName(), "ai");
	}
}