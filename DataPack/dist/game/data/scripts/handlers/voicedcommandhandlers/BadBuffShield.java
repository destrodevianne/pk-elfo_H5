package handlers.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */
public class BadBuffShield implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands =
	{
		"shieldon",
		"shieldoff"
	};
	
	/**
	 * 
	 * @see king.server.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, king.server.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("shieldon"))
		{
			activeChar.sendMessage("You are now under the grief-buff protection");
			activeChar.setProtectedPlayer(true);
		}
		else if (command.equalsIgnoreCase("shieldoff"))
		{
			activeChar.sendMessage("The grief-buff protection is now desactivated");
			activeChar.setProtectedPlayer(false);
		}
		return true;
	}
	
	/**
	 * 
	 * @see king.server.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
