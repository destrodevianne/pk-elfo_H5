package ai.individual;

import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import ai.npc.AbstractNpcAI;

public class BodyDestroyer extends AbstractNpcAI
{
	private static final int BDESTROYER = 40055;

	boolean _isLocked = false;

	public BodyDestroyer(String name, String descr)
	{
		super(name, descr);
		addAttackId(BDESTROYER);
		addKillId(BDESTROYER);
	}

	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equalsIgnoreCase("time_to_destroy"))
		
		player.setCurrentHp(0);
		
		return "";
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		int npcId = npc.getNpcId();

		if (npcId == BDESTROYER)
		{
			if (_isLocked == false)
			{
				((L2Attackable) npc).addDamageHate(player, 0, 9999);
				_isLocked = true;
				npc.setTarget(player);
			    npc.doCast(SkillTable.getInstance().getInfo(5256, 1));
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(),player.getName() + " u will Die."));
				startQuestTimer("time_to_destroy", 30000, npc, player);
			}
		}
		return "";
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == BDESTROYER)
		{
			cancelQuestTimer("time_to_destroy", npc, player);
			player.stopSkillEffects(5256);
			_isLocked = false;
		}
		return "";
	}

	public static void main(String[] args)
	{
		new BodyDestroyer(BodyDestroyer.class.getSimpleName(), "ai/individual");
	}
}