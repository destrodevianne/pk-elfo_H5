package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.handler.VoicedCommandHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Projeto PkElfo
 */
public class VoiceCommand implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"voice"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		// only voice commands allowed
		if ((command.length() > 7) && (command.charAt(6) == '.'))
		{
			final String vc, vparams;
			int endOfCommand = command.indexOf(" ", 7);
			if (endOfCommand > 0)
			{
				vc = command.substring(7, endOfCommand).trim();
				vparams = command.substring(endOfCommand).trim();
			}
			else
			{
				vc = command.substring(7).trim();
				vparams = null;
			}
			
			if (vc.length() > 0)
			{
				IVoicedCommandHandler vch = VoicedCommandHandler.getInstance().getHandler(vc);
				if (vch != null)
				{
					return vch.useVoicedCommand(vc, activeChar, vparams);
				}
			}
		}
		
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}