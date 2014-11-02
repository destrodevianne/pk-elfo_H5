package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;

/**
 * Naia Lock AI.<br>
 * Removes minions after master's death.
 */
public class NaiaLock extends AbstractNpcAI
{
	private static final int LOCK = 18491;
	
	public NaiaLock(String name, String descr)
	{
		super(name, descr);
		addKillId(LOCK);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		((L2MonsterInstance) npc).getMinionList().onMasterDie(true);
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new NaiaLock(NaiaLock.class.getSimpleName(), "ai/individual");
	}
}