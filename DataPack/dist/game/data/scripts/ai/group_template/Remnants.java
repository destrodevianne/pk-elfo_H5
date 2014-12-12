package ai.group_template;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class Remnants extends AbstractNpcAI
{
	private static final int[] NPCS =
	{
		18463,
		18464,
		18465
	};
	
	private static final int HOLY_WATER = 2358;
	
	// TODO: Find retail strings.
	// private static final String MSG = "The holy water affects Remnants Ghost. You have freed his soul.";
	// private static final String MSG_DEREK = "The holy water affects Derek. You have freed his soul.";
	
	/**
	 * Do not override onKill for Derek here. Let's make global Hellbound manipulations in Engine where it is possible.
	 * @param name
	 * @param descr
	 */
	private Remnants(String name, String descr)
	{
		super(name, descr);
		addSpawnId(NPCS);
		addSkillSeeId(NPCS);
	}
	
	@Override
	public final String onSpawn(L2Npc npc)
	{
		npc.setIsMortal(false);
		return super.onSpawn(npc);
	}
	
	@Override
	public final String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isSummon)
	{
		if (skill.getId() == HOLY_WATER)
		{
			if (!npc.isDead())
			{
				if ((targets.length > 0) && (targets[0] == npc))
				{
					if (npc.getCurrentHp() < (npc.getMaxHp() * 0.02)) // Lower, than 2%
					{
						npc.doDie(caster);
						//@formatter:off
						/*if (npc.getNpcId() == DEREK)
						{
							caster.sendMessage(MSG_DEREK);
						}
						else
						{
							caster.sendMessage(MSG);
						}*/
						//@formatter:on
					}
				}
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	public static void main(String[] args)
	{
		new Remnants(Remnants.class.getSimpleName(), "ai");
	}
}