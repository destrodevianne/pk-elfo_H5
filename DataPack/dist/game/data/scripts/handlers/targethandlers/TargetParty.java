package handlers.targethandlers;

import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetParty implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if (onlyFirst)
		{
			return new L2Character[]
			{
				activeChar
			};
		}
		
		targetList.add(activeChar);
		
		final int radius = skill.getSkillRadius();
		
		L2PcInstance player = activeChar.getActingPlayer();
		if (activeChar.isSummon())
		{
			if (L2Skill.addCharacter(activeChar, player, radius, false))
			{
				targetList.add(player);
			}
		}
		else if (activeChar.isPlayer())
		{
			if (L2Skill.addSummon(activeChar, player, radius, false))
			{
				targetList.add(player.getSummon());
			}
		}
		
		if (activeChar.isInParty())
		{
			// Get a list of Party Members
			for (L2PcInstance partyMember : activeChar.getParty().getMembers())
			{
				if ((partyMember == null) || (partyMember == player))
				{
					continue;
				}
				
				if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
				{
					break;
				}
				
				if (L2Skill.addCharacter(activeChar, partyMember, radius, false))
				{
					targetList.add(partyMember);
				}
				
				if (L2Skill.addSummon(activeChar, partyMember, radius, false))
				{
					targetList.add(partyMember.getSummon());
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_PARTY;
	}
}