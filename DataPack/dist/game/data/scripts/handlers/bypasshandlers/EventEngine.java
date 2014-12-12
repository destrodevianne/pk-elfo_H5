package handlers.bypasshandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.L2Event;

/**
 * Projeto PkElfo
 */

public class EventEngine implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"event_participate",
		"event_unregister"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		try
		{
			if (command.equalsIgnoreCase("event_participate"))
			{
				L2Event.registerPlayer(activeChar);
				return true;
			}
			else if (command.equalsIgnoreCase("event_unregister"))
			{
				L2Event.removeAndResetPlayer(activeChar);
				return true;
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}