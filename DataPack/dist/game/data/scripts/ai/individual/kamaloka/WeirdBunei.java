package ai.individual.kamaloka;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;

public class WeirdBunei extends AbstractNpcAI
{
	private static final int WEIRD = 18564;
	boolean _isAlreadyStarted = false;

	public WeirdBunei(String name, String descr)
	{
		super(name, descr);
		addAttackId(WEIRD);
		addKillId(WEIRD);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_to_skill"))
		{
			if (_isAlreadyStarted == true)
			{
				_isAlreadyStarted = false;
				npc.setTarget(player);
				npc.doCast(SkillTable.getInstance().getInfo(5625, 1));
			}
			else
				return "";
		}
		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();
		if (npcId == WEIRD)
		{
			if (_isAlreadyStarted == false)
			{
				startQuestTimer("time_to_skill", 30000, npc, player);
				_isAlreadyStarted = true;
			}
			else if (_isAlreadyStarted == true)
				return "";
		}
		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == WEIRD)
			cancelQuestTimer("time_to_skill", npc, player);
		return "";
	}

	public static void main(String[] args)
	{
		new WeirdBunei(WeirdBunei.class.getSimpleName(), "ai/individual");
	}
}