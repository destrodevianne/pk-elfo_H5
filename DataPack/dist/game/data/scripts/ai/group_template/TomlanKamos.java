package ai.group_template;

import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class TomlanKamos extends AbstractNpcAI
{
	private static L2Npc Tomlan;
	private static final int duration = 300000;
	private static final int TOMLAN = 18554;
	private static long _LastAttack = 0;
	private static boolean successDespawn = false;
	
	public TomlanKamos(int id, String name, String descr)
	{
		super(name, descr);
		
		addKillId(TOMLAN);
		addAttackId(TOMLAN);
		addSpawnId(TOMLAN);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("despawn"))
		{
			if (!successDespawn && (Tomlan != null) && ((_LastAttack + 300000) < System.currentTimeMillis()))
			{
				cancelQuestTimer("despawn", npc, null);
				Tomlan.deleteMe();
				if (InstanceManager.getInstance().getInstance(Tomlan.getInstanceId()) != null)
				{
					InstanceManager.getInstance().getInstance(Tomlan.getInstanceId()).setDuration(duration);
				}
				successDespawn = true;
			}
		}
		return null;
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		_LastAttack = System.currentTimeMillis();
		startQuestTimer("despawn", 60000, npc, null, true);
		Tomlan = npc;
		return null;
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		_LastAttack = System.currentTimeMillis();
		return super.onAttack(npc, attacker, damage, isPet);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		cancelQuestTimer("despawn", npc, null);
		return null;
	}
	
	public static void main(String[] args)
	{
		new TomlanKamos(-1, "TomlanKamos", "ai");
	}
}