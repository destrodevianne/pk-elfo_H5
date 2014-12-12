package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExGetPremiumItemList;

/**
 * Projeto PkElfo
 */

public class ReceivePremium implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"ReceivePremium"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (activeChar.getPremiumItemList().isEmpty())
		{
			activeChar.sendPacket(SystemMessageId.THERE_ARE_NO_MORE_VITAMIN_ITEMS_TO_BE_FOUND);
			return false;
		}
		
		activeChar.sendPacket(new ExGetPremiumItemList(activeChar));
		
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}