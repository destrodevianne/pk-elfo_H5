package handlers.targethandlers;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;

/**
 * Projeto PkElfo
 */

public class TargetFlagPole implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		if (!activeChar.isPlayer())
		{
			return _emptyTargetList;
		}
		
		final L2PcInstance player = activeChar.getActingPlayer();
		final Fort fort = FortManager.getInstance().getFort(player);
		if ((player.getClan() == null) || (fort == null) || !player.checkIfOkToCastFlagDisplay(fort, true, skill, activeChar.getTarget()))
		{
			return _emptyTargetList;
		}
		
		return new L2Object[]
		{
			activeChar.getTarget()
		};
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_FLAGPOLE;
	}
}