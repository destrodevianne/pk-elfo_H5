package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Trap;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.Quest.TrapAction;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class Trap implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.DETECT_TRAP,
		L2SkillType.REMOVE_TRAP
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if ((activeChar == null) || (skill == null))
		{
			return;
		}
		
		switch (skill.getSkillType())
		{
			case DETECT_TRAP:
			{
				for (L2Character target : activeChar.getKnownList().getKnownCharactersInRadius(skill.getSkillRadius()))
				{
					if (!target.isTrap())
					{
						continue;
					}
					
					if (target.isAlikeDead())
					{
						continue;
					}
					
					final L2Trap trap = (L2Trap) target;
					
					if (trap.getLevel() <= skill.getPower())
					{
						trap.setDetected(activeChar);
					}
				}
				break;
			}
			case REMOVE_TRAP:
			{
				for (L2Character target : (L2Character[]) targets)
				{
					if (!target.isTrap())
					{
						continue;
					}
					
					if (target.isAlikeDead())
					{
						continue;
					}
					
					final L2Trap trap = (L2Trap) target;
					
					if (!trap.canSee(activeChar))
					{
						if (activeChar.isPlayer())
						{
							activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
						}
						continue;
					}
					
					if (trap.getLevel() > skill.getPower())
					{
						continue;
					}
					
					if (trap.getTemplate().getEventQuests(Quest.QuestEventType.ON_TRAP_ACTION) != null)
					{
						for (Quest quest : trap.getTemplate().getEventQuests(Quest.QuestEventType.ON_TRAP_ACTION))
						{
							quest.notifyTrapAction(trap, activeChar, TrapAction.TRAP_DISARMED);
						}
					}
					
					trap.unSummon();
					if (activeChar.isPlayer())
					{
						activeChar.sendPacket(SystemMessageId.A_TRAP_DEVICE_HAS_BEEN_STOPPED);
					}
				}
			}
			default:
				break;
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}