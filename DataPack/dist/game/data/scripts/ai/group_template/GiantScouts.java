package ai.group_template;

import java.util.Collection;

import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import ai.npc.AbstractNpcAI;

public class GiantScouts extends AbstractNpcAI
{
	private static final int[] SCOUTS =
	{
		22668,
		22669
	};
	
	private GiantScouts(String name, String descr)
	{
		super(name, descr);
		addAggroRangeEnterId(SCOUTS);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		L2Character target = isSummon ? player.getSummon() : player;
		
		if (GeoData.getInstance().canSeeTarget(npc, target))
		{
			if (!npc.isInCombat() && (npc.getTarget() == null))
			{
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), Say2.NPC_SHOUT, npc.getName(), NpcStringId.OH_GIANTS_AN_INTRUDER_HAS_BEEN_DISCOVERED));
			}
			
			npc.setTarget(target);
			npc.setRunning();
			((L2Attackable) npc).addDamageHate(target, 0, 999);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
			
			// Notify clan
			Collection<L2Object> objs = npc.getKnownList().getKnownObjects().values();
			for (L2Object obj : objs)
			{
				if (obj != null)
				{
					if (obj instanceof L2MonsterInstance)
					{
						L2MonsterInstance monster = (L2MonsterInstance) obj;
						if (((npc.getClan() != null) && (monster.getClan() != null)) && monster.getClan().equals(npc.getClan()) && GeoData.getInstance().canSeeTarget(npc, monster))
						{
							monster.setTarget(target);
							monster.setRunning();
							monster.addDamageHate(target, 0, 999);
							monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
						}
					}
				}
			}
		}
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	public static void main(String[] args)
	{
		new GiantScouts(GiantScouts.class.getSimpleName(), "ai/group_template");
	}
}