package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;

public class Gargos extends AbstractNpcAI 
{
	private static final int GARGOS = 18607;

	boolean _isStarted = false;

	public Gargos(String name, String descr)
	{
		super(name, descr);
		addAttackId(GARGOS);
		addKillId(GARGOS);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("TimeToFire"))
		{
			_isStarted = false;
			player.sendMessage("Oooo... Ooo...");
			npc.doCast(L2Skill.valueOf(5705, 1));
		}
		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();

		if (npcId == GARGOS)
		{
			if (_isStarted == false)
			{
				startQuestTimer("TimeToFire", 60000, npc, player);
				_isStarted = true;
			}
		}
		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();

		if (npcId == GARGOS)
		{
			cancelQuestTimer("TimeToFire", npc, player);
		}
		return "";
	}

	public static void main(String[] args)
	{
		new Gargos(Gargos.class.getSimpleName(), "ai/individual");
	}
}