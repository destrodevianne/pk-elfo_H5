package ai.individual.kamaloka;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class VenomousStorace extends AbstractNpcAI
{
	private static final int VENOMOUS = 18571;
	private static final int GUARD    = 18572;
	boolean _isAlreadySpawned = false;
	int _isLockSpawned = 0;

	public VenomousStorace(String name, String descr)
	{
		super(name, descr);
		addAttackId(VENOMOUS);
		addKillId(GUARD);
		addKillId(VENOMOUS);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int x = player.getX();
		int y = player.getY();
		if (event.equalsIgnoreCase("time_to_spawn"))
		{
			addSpawn(GUARD, x + 100, y + 50, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
			addSpawn(GUARD, x - 100, y - 50, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
			_isAlreadySpawned = false;
			_isLockSpawned = 2;
		}
		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();
		if (npcId == VENOMOUS)
		{
			if (_isAlreadySpawned == false)
			{
				if (_isLockSpawned == 0)
				{
					startQuestTimer("time_to_spawn", 20000, npc, player);
					_isAlreadySpawned = true;
				}
			}
			if (_isLockSpawned == 2)
			{
				return "";
			}
			return "";
		}
		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == GUARD)
		{
			_isLockSpawned = 1;
		}
		else if (npcId == VENOMOUS)
		{
			cancelQuestTimer("time_to_spawn", npc, player);
		}
		return "";
	}

	public static void main(String[] args)
	{
		new VenomousStorace(VenomousStorace.class.getSimpleName(), "ai/individual");
	}
}