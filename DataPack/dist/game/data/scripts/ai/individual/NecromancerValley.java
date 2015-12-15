package ai.individual;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class NecromancerValley extends AbstractNpcAI
{
	private static final int NECROMANCER = 22858;
	private static final int EXPLODING_ORC_GHOST = 22818;
	private static final int WRATHFUL_ORC_GHOST = 22819;
	
	private NecromancerValley(String name, String descr)
	{
		super(name, descr);
		addAttackId(NECROMANCER);
		addKillId(NECROMANCER);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		if (Rnd.get(100) < 20)
		{
			L2Character attacker = isSummon ? killer.getSummon() : killer;
			L2Attackable Orc = (L2Attackable) addSpawn(EXPLODING_ORC_GHOST, npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, true);
			Orc.setRunning();
			Orc.addDamageHate(attacker, 0, 600);
			Orc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
			L2Attackable Ork2 = (L2Attackable) addSpawn(WRATHFUL_ORC_GHOST, npc.getX(), npc.getY(), npc.getZ() + 20, npc.getHeading(), false, 0, false);
			Ork2.setRunning();
			Ork2.addDamageHate(attacker, 0, 600);
			Ork2.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		if (Rnd.get(100) < 1)
		{
			L2Character player = isSummon ? attacker.getSummon() : attacker;
			L2Attackable Orc = (L2Attackable) addSpawn(EXPLODING_ORC_GHOST, npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, true);
			Orc.setRunning();
			Orc.addDamageHate(player, 0, 600);
			Orc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
			L2Attackable Ork2 = (L2Attackable) addSpawn(WRATHFUL_ORC_GHOST, npc.getX(), npc.getY(), npc.getZ() + 20, npc.getHeading(), false, 0, false);
			Ork2.setRunning();
			Ork2.addDamageHate(player, 0, 600);
			Ork2.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, player);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	public static void main(String[] args)
	{
		new NecromancerValley(NecromancerValley.class.getSimpleName(), "ai");
	}
}