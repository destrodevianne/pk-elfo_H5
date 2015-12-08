package pk.elfo.gameserver.handler;

import java.util.logging.Logger;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

public interface ISkillHandler
{
	public static Logger _log = Logger.getLogger(ISkillHandler.class.getName());
	
	/**
	 * this is the worker method that is called when using an item.
	 * @param activeChar
	 * @param skill
	 * @param targets
	 */
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets);
	
	/**
	 * this method is called at initialization to register all the item ids automatically
	 * @return all known itemIds
	 */
	public L2SkillType[] getSkillIds();
}