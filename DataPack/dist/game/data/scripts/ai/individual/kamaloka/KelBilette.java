package ai.individual.kamaloka;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;


public class KelBilette extends AbstractNpcAI
{
	private static final int KEL   = 18573;
	private static final int GUARD = 18574;

	boolean _isAlreadyStarted = false;
	boolean _isAlreadySpawned = false;

	public KelBilette(String name, String descr)
	{
		super(name, descr);
		addAttackId(KEL);
		addKillId(GUARD);
		addKillId(KEL);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		int x = player.getX();
		int y = player.getY();

		if (event.equalsIgnoreCase("time_to_skill"))
		{
			npc.setTarget(player);
			npc.doCast(SkillTable.getInstance().getInfo(4748, 6));
			_isAlreadyStarted = false;
			startQuestTimer("time_to_skill1", 10000, npc, player);
		}
		else if (event.equalsIgnoreCase("time_to_skill1"))
		{
			npc.setTarget(player);
			npc.doCast(SkillTable.getInstance().getInfo(5203, 6));
		}
		else if (event.equalsIgnoreCase("time_to_spawn"))
			addSpawn(GUARD, x + 100, y + 50, npc.getZ(), 0, false, 0, false, npc.getInstanceId());

		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();

		if (npcId == KEL)
		{
			if (_isAlreadyStarted == false)
			{
				startQuestTimer("time_to_skill", 30000, npc, player);
				_isAlreadyStarted = true;
			}
			if (_isAlreadyStarted == true)
				return "";
			if (_isAlreadySpawned == false)
			{
				startQuestTimer("time_to_spawn", 10000, npc, player);
				_isAlreadySpawned = true;
			}
			if (_isAlreadySpawned == true)
				return "";
		}

		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == GUARD)
			_isAlreadySpawned = true;
		else if (npcId == KEL)
		{
			cancelQuestTimer("time_to_spawn", npc, player);
			cancelQuestTimer("time_to_skill", npc, player);
		}

		return "";
	}

	public static void main(String[] args)
	{
		new KelBilette(KelBilette.class.getSimpleName(), "ai");
	}
}