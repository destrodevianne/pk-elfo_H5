package handlers.targethandlers;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;

/**
 * Projeto PkElfo
 */

public class TargetOwnerPet implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if (activeChar.isSummon())
		{
			target = ((L2Summon) activeChar).getOwner();
			if ((target != null) && !target.isDead())
			{
				return new L2Character[]
				{
					target
				};
			}
		}
		
		return _emptyTargetList;
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_OWNER_PET;
	}
}