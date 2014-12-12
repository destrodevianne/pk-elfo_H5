package ai.group_template;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class StarStones extends AbstractNpcAI
{
	private static final int[] MOBS =
	{
		18684,
		18685,
		18686,
		18687,
		18688,
		18689,
		18690,
		18691,
		18692
	};
	
	private static final int RATE = 1;
	
	private StarStones(String name, String descr)
	{
		super(name, descr);
		registerMobs(MOBS, QuestEventType.ON_SKILL_SEE);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isSummon)
	{
		if (skill.getId() == 932)
		{
			int itemId = 0;
			
			switch (npc.getNpcId())
			{
				case 18684:
				case 18685:
				case 18686:
					// give Red item
					itemId = 14009;
					break;
				case 18687:
				case 18688:
				case 18689:
					// give Blue item
					itemId = 14010;
					break;
				case 18690:
				case 18691:
				case 18692:
					// give Green item
					itemId = 14011;
					break;
				default:
					// unknown npc!
					return super.onSkillSee(npc, caster, skill, targets, isSummon);
			}
			if (getRandom(100) < 33)
			{
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(RATE + 1, 2 * RATE), null, true);
			}
			else if (((skill.getLevel() == 1) && (getRandom(100) < 15)) || ((skill.getLevel() == 2) && (getRandom(100) < 50)) || ((skill.getLevel() == 3) && (getRandom(100) < 75)))
			{
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
				caster.addItem("StarStone", itemId, getRandom(1, RATE), null, true);
			}
			else
			{
				caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_FAILED);
			}
			npc.deleteMe();
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	public static void main(String[] args)
	{
		new StarStones(StarStones.class.getSimpleName(), "ai");
	}
}