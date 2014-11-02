package ai.individual.kamaloka;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;

public class WhiteAllosce extends AbstractNpcAI
{
	private static final int ALLOSCE = 18577;
	private static final int GUARD   = 18578;
	boolean _isLock = false;

	public WhiteAllosce(String name, String descr)
	{
		super(name, descr);
		addAttackId(ALLOSCE);
		addKillId(ALLOSCE);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int x = player.getX();
		int y = player.getY();

		if (event.equalsIgnoreCase("time_to_spawn"))
		{
			addSpawn(GUARD, x + 100, y + 50, npc.getZ(), 0, false, 0, false, npc.getInstanceId());
			_isLock = false;
		}
		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();

		if (npcId == ALLOSCE)
		{
			if (_isLock == false)
			{
				startQuestTimer("time_to_spawn", 40000, npc, player);
				_isLock = true;
			}
			else
				return "";
		}
		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == ALLOSCE)
			cancelQuestTimer("time_to_spawn", npc, player);
		return "";
	}

	public static void main(String[] args)
	{
		new WhiteAllosce(WhiteAllosce.class.getSimpleName(), "ai/individual");
	}
}