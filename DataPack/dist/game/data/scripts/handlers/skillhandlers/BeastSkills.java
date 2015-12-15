package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2TamedBeastInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * Projeto PkElfo
 */
 
public class BeastSkills implements ISkillHandler
{
	// private static Logger _log = Logger.getLogger(BeastSkills.class.getName());
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.BEAST_FEED,
		L2SkillType.BEAST_RELEASE,
		L2SkillType.BEAST_RELEASE_ALL,
		L2SkillType.BEAST_SKILL,
		L2SkillType.BEAST_ACCOMPANY
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2SkillType type = skill.getSkillType();
		L2PcInstance player = activeChar.getActingPlayer();
		L2Object target = player.getTarget();
		
		switch (type)
		{
			case BEAST_FEED:
				L2Object[] targetList = skill.getTargetList(activeChar);
				
				if (targetList == null)
				{
					return;
				}
				
				// This is just a dummy skill handler for the golden food and crystal food skills,
				// since the AI responce onSkillUse handles the rest.
				break;
			case BEAST_RELEASE:
				if ((target != null) && (target instanceof L2TamedBeastInstance))
				{
					((L2TamedBeastInstance) target).deleteMe();
				}
				break;
			case BEAST_RELEASE_ALL:
				if (player.getTrainedBeasts() != null)
				{
					for (L2TamedBeastInstance beast : player.getTrainedBeasts())
					{
						beast.deleteMe();
					}
				}
				break;
			case BEAST_ACCOMPANY:
				// Unknown effect now
				break;
			case BEAST_SKILL:
				if ((target != null) && (target instanceof L2TamedBeastInstance))
				{
					((L2TamedBeastInstance) target).castBeastSkills();
				}
				break;
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}