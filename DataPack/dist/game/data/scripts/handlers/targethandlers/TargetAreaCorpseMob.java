package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetAreaCorpseMob implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if (!target.isL2Attackable() || !target.isDead())
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return _emptyTargetList;
		}
		
		if (onlyFirst)
		{
			return new L2Character[]
			{
				target
			};
		}
		
		targetList.add(target);
		
		final boolean srcInArena = (activeChar.isInsideZone(ZoneId.PVP) && !activeChar.isInsideZone(ZoneId.SIEGE));
		
		final int radius = skill.getSkillRadius();
		final Collection<L2Character> objs = activeChar.getKnownList().getKnownCharacters();
		for (L2Character obj : objs)
		{
			if (!(obj.isL2Attackable() || obj.isPlayable()) || !Util.checkIfInRange(radius, target, obj, true))
			{
				continue;
			}
			
			if (!L2Skill.checkForAreaOffensiveSkills(activeChar, obj, skill, srcInArena))
			{
				continue;
			}
			
			targetList.add(obj);
		}
		
		if (targetList.isEmpty())
		{
			return _emptyTargetList;
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_AREA_CORPSE_MOB;
	}
}