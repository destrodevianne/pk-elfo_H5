package ai.individual.kamaloka;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.util.Util;
import ai.npc.AbstractNpcAI;

/**
 * ¤õÃÄ±í
 */
public class PowderKeg extends AbstractNpcAI 
{
	private static final int[] NPC_IDS = { 18622 };

	public PowderKeg(String name, String descr)
	{
		super(name, descr);
		this.registerMobs(NPC_IDS);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (Util.contains(NPC_IDS, npcId))
		{
			L2Skill _boom = SkillTable.getInstance().getInfo(5714, 1);
			npc.disableCoreAI(true);
			npc.doCast(_boom);
			npc.broadcastStatusUpdate();
			return "";
		}
		return super.onKill(npc, killer, isPet);
	}

	public static void main(String[] args)
	{
		new PowderKeg(PowderKeg.class.getSimpleName(),"ai");
	}
}