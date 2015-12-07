package pk.elfo.gameserver.scripts.data;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;

public class EnergySeed extends AbstractNpcAI1
{
	private static final int[] MOBS =
	{
		18678,
		18679,
		18680,
		18681,
		18682,
		18683
	};
	
	private static final int RATE = 1;
	
	private EnergySeed(String name, String descr)
	{
		super(name, descr);
		
		registerMobs(MOBS, QuestEventType.ON_SKILL_SEE);
	}
	
	@Override
	public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isSummon)
	{
		if (Util.contains(targets, npc))
		{
			if (npc.isInsideRadius(caster, 120, false, false))
			{
				if (skill.getId() == 5780)
				{
					int itemId = 0;
					
					switch (npc.getNpcId())
					{
						case 18678:
							itemId = 14016;
							break;
						case 18679:
							itemId = 14015;
							break;
						case 18680:
							itemId = 14017;
							break;
						case 18681:
							itemId = 14018;
							break;
						case 18682:
							itemId = 14020;
							break;
						case 18683:
							itemId = 14019;
							break;
						default:
							return super.onSkillSee(npc, caster, skill, targets, isSummon);
					}
					
					if (getRandom(100) < 33)
					{
						caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
						caster.addItem("EnergySeed", itemId, getRandom(RATE + 1, 2 * RATE), null, true);
					}
					else if (((skill.getLevel() == 1) && (getRandom(100) < 15)) || ((skill.getLevel() == 2) && (getRandom(100) < 50)) || ((skill.getLevel() == 3) && (getRandom(100) < 75)))
					{
						caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_SUCCEEDED);
						caster.addItem("EnergySeed", itemId, getRandom(1, RATE), null, true);
					}
					else
					{
						caster.sendPacket(SystemMessageId.THE_COLLECTION_HAS_FAILED);
					}
					npc.deleteMe();
				}
			}
		}
		return super.onSkillSee(npc, caster, skill, targets, isSummon);
	}
	
	public static void main(String[] args)
	{
		new EnergySeed(EnergySeed.class.getSimpleName(), "Energy Seed");
	}
}