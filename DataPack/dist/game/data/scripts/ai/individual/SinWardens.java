package ai.individual;

import java.util.Map;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import javolution.util.FastMap;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class SinWardens extends AbstractNpcAI
{
	private static final int[] SIN_WARDEN_MINIONS =
	{
		22424,
		22425,
		22426,
		22427,
		22428,
		22429,
		22430,
		22432,
		22433,
		22434,
		22435,
		22436,
		22437,
		22438
	};
	
	private final Map<Integer, Integer> killedMinionsCount = new FastMap<>();
	
	private SinWardens(String name, String descr)
	{
		super(name, descr);
		addKillId(SIN_WARDEN_MINIONS);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (npc.isMinion())
		{
			L2MonsterInstance master = ((L2MonsterInstance) npc).getLeader();
			if ((master != null) && !master.isDead())
			{
				int killedCount = killedMinionsCount.containsKey(master.getObjectId()) ? killedMinionsCount.get(master.getObjectId()) : 0;
				killedCount++;
				
				if ((killedCount) == 5)
				{
					master.broadcastPacket(new NpcSay(master.getObjectId(), Say2.NPC_ALL, master.getNpcId(), NpcStringId.WE_MIGHT_NEED_NEW_SLAVES_ILL_BE_BACK_SOON_SO_WAIT));
					master.doDie(killer);
					killedMinionsCount.remove(master.getObjectId());
				}
				else
				{
					killedMinionsCount.put(master.getObjectId(), killedCount);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new SinWardens(SinWardens.class.getSimpleName(), "ai");
	}
}