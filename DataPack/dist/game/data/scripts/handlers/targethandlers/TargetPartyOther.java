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

public class TargetPartyOther implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if ((target != null) && (target != activeChar) && activeChar.isInParty() && target.isInParty() && (activeChar.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId()))
		{
			if (!target.isDead())
			{
				if (target.isPlayer())
				{
					switch (skill.getId())
					{
					// FORCE BUFFS may cancel here but there should be a proper condition
						case 426:
							if (!target.getActingPlayer().isMageClass())
							{
								return new L2Character[]
								{
									target
								};
							}
							return _emptyTargetList;
						case 427:
							if (target.getActingPlayer().isMageClass())
							{
								return new L2Character[]
								{
									target
								};
							}
							return _emptyTargetList;
					}
				}
				return new L2Character[]
				{
					target
				};
			}
			return _emptyTargetList;
		}
		activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
		return _emptyTargetList;
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_PARTY_OTHER;
	}
}