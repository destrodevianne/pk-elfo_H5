package ai.npc;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.instancemanager.WalkingManager;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class ZealotOfShilen extends AbstractNpcAI
{
	private static final int ZEALOT = 18782;
	
	private static final int GUARD1 = 32628;
	private static final int GUARD2 = 32629;
	
	private static final int[] ROUTE_ID =
	{
		101, // gludio_airport1
		102, // gludio_airport2
		103, // gludio_airport3
		104, // gludio_airport4
	};
	
	private static final L2Npc[] _guards = new L2Npc[4];
	
	private static boolean _killOneAtStart = true; // Lets help the guards :)
	
	public ZealotOfShilen(String name, String descr)
	{
		super(name, descr);
		addSpawnId(GUARD1);
		addSpawnId(GUARD2);
		addSpawnId(ZEALOT);
		
		int i = 0;
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GUARD1))
		{
			L2Npc guard = spawn.getLastSpawn();
			guard.setIsInvul(true);
			((L2Attackable) guard).setCanReturnToSpawnPoint(false);
			startQuestTimer("watching", 10000, guard, null, true);
			WalkingManager.getInstance().startMoving(guard, ROUTE_ID[i]);
			_guards[i] = guard;
			i = (i == 3) ? 0 : i++;
		}
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(GUARD2))
		{
			L2Npc guard = spawn.getLastSpawn();
			guard.setIsInvul(true);
			((L2Attackable) guard).setCanReturnToSpawnPoint(false);
			startQuestTimer("watching", 10000, guard, null, true);
			WalkingManager.getInstance().startMoving(guard, ROUTE_ID[i]);
			_guards[i] = guard;
			i = (i == 3) ? 0 : i++;
		}
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(ZEALOT))
		{
			if (_killOneAtStart)
			{
				spawn.getLastSpawn().doDie(null);
				_killOneAtStart = false;
			}
			spawn.getLastSpawn().setIsNoRndWalk(true);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("watching") && !npc.isAttackingNow())
		{
			for (L2Character character : npc.getKnownList().getKnownCharacters())
			{
				if (character.isMonster() && !character.isDead() && !((L2Attackable) character).isDecayed())
				{
					npc.setRunning();
					((L2Attackable) npc).addDamageHate(character, 0, 999);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, character, null);
				}
			}
		}
		return event;
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		switch (npc.getNpcId())
		{
			case GUARD1:
			case GUARD2:
				for (int i = 0; i <= 3; i++)
				{
					if (npc.equals(_guards[i]))
					{
						WalkingManager.getInstance().startMoving(npc, ROUTE_ID[i]);
					}
				}
				break;
			case ZEALOT:
				npc.setIsNoRndWalk(true);
				break;
		}
		return super.onSpawn(npc);
	}
	
	public static void main(String[] args)
	{
		new ZealotOfShilen(ZealotOfShilen.class.getSimpleName(), "ai");
	}
}