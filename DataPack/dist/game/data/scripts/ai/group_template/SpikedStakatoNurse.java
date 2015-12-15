package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class SpikedStakatoNurse extends AbstractNpcAI
{
	private static final int SPIKED_STAKATO_BABY = 22632;
	private static final int SPIKED_STAKATO_NURSE_2ND_FORM = 22631;
	
	public SpikedStakatoNurse(int questId, String name, String descr)
	{
		super(name, descr);
		
		addKillId(SPIKED_STAKATO_BABY);
		addKillId(SPIKED_STAKATO_NURSE_2ND_FORM);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		final L2Npc nurse = getNurse(npc);
		if ((nurse != null) && !nurse.isDead())
		{
			getNurse(npc).doDie(getNurse(npc));
			final L2Npc newForm = addSpawn(SPIKED_STAKATO_NURSE_2ND_FORM, npc.getX() + Rnd.get(10, 50), npc.getY() + Rnd.get(10, 50), npc.getZ(), 0, false, 0, true);
			newForm.setRunning();
			((L2Attackable) newForm).addDamageHate(killer, 1, 99999);
			newForm.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, killer);
		}
		return super.onKill(npc, killer, isPet);
	}
	
	public L2Npc getNurse(L2Npc couple)
	{
		return ((L2MonsterInstance) couple).getLeader();
	}
	
	public static void main(String[] args)
	{
		new SpikedStakatoNurse(-1, "SpikedStakatoNurse", "ai");
	}
}