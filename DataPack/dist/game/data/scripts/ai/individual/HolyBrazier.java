package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;


public class HolyBrazier extends AbstractNpcAI
{
	private static final int HolyBrazier        = 32027;
	private static final int GuardianOfTheGrail = 22133;

	private L2Npc _guard = null;
	private L2Npc _brazier = null;

	public HolyBrazier(String name, String descr)
	{
		super(name, descr);
		int[] mobs = { HolyBrazier , GuardianOfTheGrail };
		registerMobs(mobs);
	}

	private void spawnGuard(L2Npc npc)
	{
		System.out.println("******* spawnGuard *******");
		System.out.println("_guard   = " + _guard);
		System.out.println("_brazier = " + _brazier);
		if ((_guard == null) && (_brazier != null)) 
		{
			System.out.println("******* addSpawn *******");
			_guard = addSpawn(GuardianOfTheGrail, _brazier.getX(), _brazier.getY(), _brazier.getZ(), 0, false, 0);
			_guard.setIsNoRndWalk(true);
		}
		System.out.println("******* return *******");
		return;
	}

	@Override
	public String onSpawn(L2Npc npc)
	{
		System.out.println("******* onSpawn *******");
		System.out.println("npc = " + npc.getNpcId());
		if (npc.getNpcId() == HolyBrazier)
		{
			System.out.println("******* HolyBrazier *******");
			_brazier = npc;
			_guard = null;
			npc.setIsNoRndWalk(true);
			spawnGuard(npc);
		}
		System.out.println("******* return *******");
		return super.onSpawn(npc);
	}

	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (npc.getNpcId() == GuardianOfTheGrail && !npc.isInCombat() && npc.getTarget() == null)
		{
			npc.setIsNoRndWalk(true);
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}

	@Override
	public String onKill (L2Npc npc, L2PcInstance killer, boolean isPet) 
	{
		if (npc.getNpcId() == GuardianOfTheGrail)
		{
			_guard = null;
			spawnGuard(npc);
		}
		else if (npc.getNpcId() == HolyBrazier)
		{
			if (_guard != null)
			{
				_guard.deleteMe();
				_guard = null;
				
			}
			_brazier = null;
		}
		return super.onKill(npc,killer,isPet);
	}

	public static void main(String[] args)
	{
		// now call the constructor (starts up the ai)
		new HolyBrazier(HolyBrazier.class.getSimpleName(), "ai/individual");
	}
}