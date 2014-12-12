package ai.group_template;

import java.util.List;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.taskmanager.DecayTaskManager;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Slaves extends AbstractNpcAI
{
	private static final int[] MASTERS =
	{
		22320,
		22321
	};
	
	private static final L2CharPosition MOVE_TO = new L2CharPosition(-25451, 252291, -3252, 3500);
	
	private static final int TRUST_REWARD = 10;
	
	private Slaves(String name, String descr)
	{
		super(name, descr);
		addSpawnId(MASTERS);
		addKillId(MASTERS);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		((L2MonsterInstance) npc).enableMinions(HellboundManager.getInstance().getLevel() < 5);
		((L2MonsterInstance) npc).setOnKillDelay(1000);
		
		return super.onSpawn(npc);
	}
	
	// Let's count trust points for killing in Engine
	@Override
	public final String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (((L2MonsterInstance) npc).getMinionList() != null)
		{
			final List<L2MonsterInstance> slaves = ((L2MonsterInstance) npc).getMinionList().getSpawnedMinions();
			if ((slaves != null) && !slaves.isEmpty())
			{
				for (L2MonsterInstance slave : slaves)
				{
					if ((slave == null) || slave.isDead())
					{
						continue;
					}
					
					slave.clearAggroList();
					slave.abortAttack();
					slave.abortCast();
					slave.broadcastPacket(new NpcSay(slave.getObjectId(), Say2.NPC_ALL, slave.getNpcId(), NpcStringId.THANK_YOU_FOR_SAVING_ME_FROM_THE_CLUTCHES_OF_EVIL));
					
					if ((HellboundManager.getInstance().getLevel() >= 1) && (HellboundManager.getInstance().getLevel() <= 2))
					{
						HellboundManager.getInstance().updateTrust(TRUST_REWARD, false);
					}
					
					slave.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, MOVE_TO);
					DecayTaskManager.getInstance().addDecayTask(slave);
				}
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Slaves(Slaves.class.getSimpleName(), "ai");
	}
}