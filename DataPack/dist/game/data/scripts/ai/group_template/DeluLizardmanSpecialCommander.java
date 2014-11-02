package ai.group_template;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;

public class DeluLizardmanSpecialCommander extends AbstractNpcAI
{
	private static final int DELU_LIZARDMAN_SPECIAL_COMMANDER = 21107;
	
	public DeluLizardmanSpecialCommander(String name, String descr)
	{
		super(name, descr);
		addAttackId(DELU_LIZARDMAN_SPECIAL_COMMANDER);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
	{
		if (npc.getNpcId() == DELU_LIZARDMAN_SPECIAL_COMMANDER)
		{
			if (npc.getAI().getIntention() != CtrlIntention.AI_INTENTION_ATTACK)
			{
				if (Rnd.get(100) < 70)
				{
					npc.broadcastNpcSay("How dare you interrupt a sacred duel! You must be taught a lesson!");
				}
			}
			else if (Rnd.get(100) < 10)
			{
				npc.broadcastNpcSay("Come on, I'll take you on!");
			}
		}
		
		return super.onAttack(npc, player, damage, isPet);
	}
	
	public static void main(String[] args)
	{
		new DeluLizardmanSpecialCommander(DeluLizardmanSpecialCommander.class.getSimpleName(), "ai/group_template");
	}
}