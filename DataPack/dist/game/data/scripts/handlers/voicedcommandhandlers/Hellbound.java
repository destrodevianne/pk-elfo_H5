package handlers.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.instancemanager.HellboundManager;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */
public class Hellbound implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"hellbound"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (HellboundManager.getInstance().isLocked())
		{
			activeChar.sendMessage("Hellbound esta atualmente bloqueado.");
			return true;
		}
		
		final int maxTrust = HellboundManager.getInstance().getMaxTrust();
		activeChar.sendMessage("Hellbound level: " + HellboundManager.getInstance().getLevel() + " trust: " + HellboundManager.getInstance().getTrust() + (maxTrust > 0 ? "/" + maxTrust : ""));
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
