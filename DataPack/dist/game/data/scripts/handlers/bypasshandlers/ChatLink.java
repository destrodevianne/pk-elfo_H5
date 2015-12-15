package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Projeto PkElfo
 */

public class ChatLink implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"Chat"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		int val = 0;
		try
		{
			val = Integer.parseInt(command.substring(5));
		}
		catch (Exception ioobe)
		{
			
		}
		((L2Npc) target).showChatWindow(activeChar, val);
		
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}