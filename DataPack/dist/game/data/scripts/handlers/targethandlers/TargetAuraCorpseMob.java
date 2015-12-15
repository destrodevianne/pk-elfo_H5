package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetAuraCorpseMob implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		// Go through the L2Character _knownList
		final Collection<L2Character> objs = activeChar.getKnownList().getKnownCharactersInRadius(skill.getSkillRadius());
		for (L2Character obj : objs)
		{
			if (obj.isL2Attackable() && obj.isDead())
			{
				if (onlyFirst)
				{
					return new L2Character[]
					{
						obj
					};
				}
				
				if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
				{
					break;
				}
				targetList.add(obj);
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_AURA_CORPSE_MOB;
	}
}