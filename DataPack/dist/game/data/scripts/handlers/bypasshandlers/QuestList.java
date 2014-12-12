package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2AdventurerInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExShowQuestInfo;

/**
 * Projeto PkElfo
 */

public class QuestList implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"questlist"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2AdventurerInstance))
		{
			return false;
		}
		
		activeChar.sendPacket(ExShowQuestInfo.STATIC_PACKET);
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}