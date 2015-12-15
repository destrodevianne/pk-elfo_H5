package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class WarriorMonk extends AbstractNpcAI
{
	private boolean FirstAttacked = false;
	
	public WarriorMonk(int questId, String name, String descr)
	{
		super(name, descr);
		registerMobs(new int[]
		{
			22129
		});
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet, L2Skill skill)
	{
		if (FirstAttacked)
		{
			if (Rnd.get(100) > 50)
			{
				npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Brother " + player.getName() + ", move your weapon away!!"));
			}
		}
		else
		{
			FirstAttacked = true;
		}
		return super.onAttack(npc, player, damage, isPet, skill);
	}
	
	@Override
	public String onSpawn(L2Npc npc)
	{
		for (L2PcInstance player : npc.getKnownList().getKnownPlayers().values())
		{
			if (player.isInsideRadius(npc, 500, false, false))
			{
				if (player.getActiveWeaponItem() != null)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Brother " + player.getName() + ", move your weapon away!!"));
					((L2Attackable) npc).addDamageHate(player, 0, 999);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
				}
				else
				{
					((L2Attackable) npc).getAggroList().remove(player);
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
				}
			}
		}
		return super.onSpawn(npc);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		if (player.getActiveWeaponItem() != null)
		{
			npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), "Brother " + player.getName() + ", move your weapon away!!"));
			((L2Attackable) npc).addDamageHate(player, 0, 999);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
		}
		else
		{
			((L2Attackable) npc).getAggroList().remove(player);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE, null, null);
		}
		return super.onAggroRangeEnter(npc, player, isPet);
	}
	
	public static void main(String[] args)
	{
		new WarriorMonk(-1, "WarriorMonk", "ai");
	}
}