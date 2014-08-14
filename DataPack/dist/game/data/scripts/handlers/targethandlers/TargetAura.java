package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.zone.ZoneId;
import javolution.util.FastList;

/**
 * PkElfo
 */

public class TargetAura implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		final boolean srcInArena = (activeChar.isInsideZone(ZoneId.PVP) && !activeChar.isInsideZone(ZoneId.SIEGE));
		
		final L2PcInstance sourcePlayer = activeChar.getActingPlayer();
		
		final Collection<L2Character> objs = activeChar.getKnownList().getKnownCharactersInRadius(skill.getSkillRadius());
		
		if (skill.getSkillType() == L2SkillType.DUMMY)
		{
			if (onlyFirst)
			{
				return new L2Character[]
				{
					activeChar
				};
			}
			
			targetList.add(activeChar);
			for (L2Character obj : objs)
			{
				if (!((obj == activeChar) || (obj == sourcePlayer) || obj.isNpc() || obj.isL2Attackable()))
				{
					continue;
				}
				
				if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
				{
					break;
				}
				targetList.add(obj);
			}
		}
		else
		{
			for (L2Character obj : objs)
			{
				if (obj.isL2Attackable() || obj.isPlayable())
				{
					if (!L2Skill.checkForAreaOffensiveSkills(activeChar, obj, skill, srcInArena))
					{
						continue;
					}
					
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
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_AURA;
	}
}