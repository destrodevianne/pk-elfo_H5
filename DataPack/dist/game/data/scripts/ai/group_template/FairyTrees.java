package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.util.Util;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class FairyTrees extends AbstractNpcAI
{
	private static final int[] MOBS =
	{
		27185,
		27186,
		27187,
		27188
	};
	
	private FairyTrees(String name, String descr)
	{
		super(name, descr);
		registerMobs(MOBS, QuestEventType.ON_KILL);
		addSpawnId(27189);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		int npcId = npc.getNpcId();
		if (Util.contains(MOBS, npcId))
		{
			for (int i = 0; i < 20; i++)
			{
				L2Attackable newNpc = (L2Attackable) addSpawn(27189, npc.getX(), npc.getY(), npc.getZ(), 0, false, 30000);
				L2Character originalKiller = isSummon ? killer.getSummon() : killer;
				newNpc.setRunning();
				newNpc.addDamageHate(originalKiller, 0, 999);
				newNpc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, originalKiller);
				if (Rnd.nextBoolean())
				{
					L2Skill skill = SkillTable.getInstance().getInfo(4243, 1);
					if ((skill != null) && (originalKiller != null))
					{
						skill.getEffects(newNpc, originalKiller);
					}
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new FairyTrees(FairyTrees.class.getSimpleName(), "ai/group_template");
	}
}