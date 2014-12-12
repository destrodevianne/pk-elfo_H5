package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetClanMember implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if (activeChar.isNpc())
		{
			// for buff purposes, returns friendly mobs nearby and mob itself
			final L2Npc npc = (L2Npc) activeChar;
			if ((npc.getFactionId() == null) || npc.getFactionId().isEmpty())
			{
				return new L2Character[]
				{
					activeChar
				};
			}
			final Collection<L2Object> objs = activeChar.getKnownList().getKnownObjects().values();
			for (L2Object newTarget : objs)
			{
				if (newTarget.isNpc() && npc.getFactionId().equals(((L2Npc) newTarget).getFactionId()))
				{
					if (!Util.checkIfInRange(skill.getCastRange(), activeChar, newTarget, true))
					{
						continue;
					}
					if (((L2Npc) newTarget).getFirstEffect(skill) != null)
					{
						continue;
					}
					targetList.add((L2Npc) newTarget);
					break;
				}
			}
			if (targetList.isEmpty())
			{
				targetList.add(npc);
			}
		}
		else
		{
			return _emptyTargetList;
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_CLAN_MEMBER;
	}
}