package ai.group_template;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Sandstorms extends AbstractNpcAI
{
	private static final int SANDSTORM = 32350;
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		npc.setTarget(player);
		npc.doCast(L2Skill.valueOf(5435, 1));
		return super.onAggroRangeEnter(npc, player, isSummon);
	}
	
	public Sandstorms(String name, String descr)
	{
		super(name, descr);
		addAggroRangeEnterId(SANDSTORM);
	}
	
	public static void main(String[] args)
	{
		new Sandstorms(Sandstorms.class.getSimpleName(), "ai");
	}
}