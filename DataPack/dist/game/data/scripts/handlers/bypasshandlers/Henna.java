package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2SymbolMakerInstance;
import pk.elfo.gameserver.model.items.L2Henna;
import pk.elfo.gameserver.network.serverpackets.HennaEquipList;
import pk.elfo.gameserver.network.serverpackets.HennaRemoveList;

/**
 * Projeto PkElfo
 */

public class Henna implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"Draw",
		"RemoveList"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2SymbolMakerInstance))
		{
			return false;
		}
		
		if (command.equals("Draw"))
		{
			activeChar.sendPacket(new HennaEquipList(activeChar));
		}
		else if (command.equals("RemoveList"))
		{
			for (L2Henna henna : activeChar.getHennaList())
			{
				if (henna != null)
				{
					activeChar.sendPacket(new HennaRemoveList(activeChar));
					break;
				}
			}
		}
		return true;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}