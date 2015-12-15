package ai.group_template;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.util.Rnd;
 
/**
 * Projeto PkElfo
 */

public class OlMahumGeneral extends AbstractNpcAI 
{
	private static final int Ol_Mahum_General = 20438;
	private static boolean _FirstAttacked;

	public OlMahumGeneral(String name, String descr)
	{
		super(name, descr);
		int[] mobs = { Ol_Mahum_General };
		registerMobs(mobs, new Quest.QuestEventType[] { Quest.QuestEventType.ON_ATTACK, Quest.QuestEventType.ON_KILL });
		_FirstAttacked = false;
	}

	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
	{
		if (npc.getNpcId() == Ol_Mahum_General)
		{
			if (_FirstAttacked)
			{
				if (Rnd.get(100) == 50)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), NpcStringId.I_WILL_DEFINITELY_REPAY_THIS_HUMILIATION));
				}
				if (Rnd.get(100) == 50)
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), 0, npc.getNpcId(), NpcStringId.WE_SHALL_SEE_ABOUT_THAT));
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
		if (npcId == Ol_Mahum_General)
		{
			_FirstAttacked = false;
		}
		return super.onKill(npc, killer, isPet);
	}

	public static void main(String[] args)
	{
		new OlMahumGeneral(OlMahumGeneral.class.getSimpleName(), "ai");
	}
}