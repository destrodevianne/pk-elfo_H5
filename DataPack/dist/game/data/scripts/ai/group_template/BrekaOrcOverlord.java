package ai.group_template;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class BrekaOrcOverlord extends AbstractNpcAI 
{
	private static final int BREKA = 20270;

	private static boolean _FirstAttacked;

	public BrekaOrcOverlord(String name, String descr)
	{
		super(name, descr);
		int[] mobs = {BREKA};
		registerMobs(mobs, QuestEventType.ON_ATTACK, QuestEventType.ON_KILL);
		_FirstAttacked = false;
	}

	@Override
	public String onAttack (L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.getNpcId() == BREKA)
		{
			if (_FirstAttacked)
			{
				if (Rnd.get(100) == 50)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Ultimate strength!!!"));
				}
				if (Rnd.get(100) == 50)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Now it is truly the beginning of the duel!!"));
				}
				if (Rnd.get(100) == 50)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(),0,npc.getNpcId(),"Did not think could even use it on the callow kid trick!"));
				}
			}
			_FirstAttacked = true;
		}
		return super.onAttack(npc, attacker, damage, isPet);
	}

	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet)
	{
		int npcId = npc.getNpcId();
		if (npcId == BREKA)
		{
			_FirstAttacked = false;
		}
		return super.onKill(npc,killer,isPet);
	}

	public static void main(String[] args)
	{
		new BrekaOrcOverlord(BrekaOrcOverlord.class.getSimpleName(), "ai/group_template");
	}
}