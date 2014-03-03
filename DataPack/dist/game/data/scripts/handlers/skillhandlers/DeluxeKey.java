package handlers.skillhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * PkElfo
 */
 
public class DeluxeKey implements ISkillHandler
{
	private static Logger _log = Logger.getLogger(DeluxeKey.class.getName());
	
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.DELUXE_KEY_UNLOCK
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2Object[] targetList = skill.getTargetList(activeChar);
		
		if (targetList == null)
		{
			return;
		}
		
		_log.fine("Fundicao da chave Delux sucedida.");
		
		// This is just a dummy skill handler for the golden food and crystal food skills,
		// since the AI responce onSkillUse handles the rest.
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}