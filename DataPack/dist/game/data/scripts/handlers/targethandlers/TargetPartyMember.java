package handlers.targethandlers;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */

public class TargetPartyMember implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if (target == null)
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return _emptyTargetList;
		}
		if (!target.isDead())
		{
			if ((target == activeChar) || (activeChar.isInParty() && target.isInParty() && (activeChar.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId())) || (activeChar.isPlayer() && target.isSummon() && (activeChar.getSummon() == target)) || (activeChar.isSummon() && target.isPlayer() && (activeChar == target.getSummon())))
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
		return L2TargetType.TARGET_PARTY_MEMBER;
	}
}