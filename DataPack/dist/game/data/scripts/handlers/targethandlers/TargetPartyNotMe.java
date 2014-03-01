package handlers.targethandlers;

import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

/**
 * PkElfo
 */

public class TargetPartyNotMe implements ITargetTypeHandler
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
		
		L2PcInstance player = null;
		
		if (activeChar.isSummon())
		{
			player = ((L2Summon) activeChar).getOwner();
			targetList.add(player);
		}
		else if (activeChar.isPlayer())
		{
			player = activeChar.getActingPlayer();
			if (activeChar.getSummon() != null)
			{
				targetList.add(activeChar.getSummon());
			}
		}
		
		if (activeChar.getParty() != null)
		{
			List<L2PcInstance> partyList = activeChar.getParty().getMembers();
			
			for (L2PcInstance partyMember : partyList)
			{
				if (partyMember == null)
				{
					continue;
				}
				else if (partyMember == player)
				{
					continue;
				}
				else if (!partyMember.isDead() && Util.checkIfInRange(skill.getSkillRadius(), activeChar, partyMember, true))
				{
					if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
					{
						break;
					}
					
					targetList.add(partyMember);
					
					if ((partyMember.getSummon() != null) && !partyMember.getSummon().isDead())
					{
						targetList.add(partyMember.getSummon());
					}
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_PARTY_NOTME;
	}
}