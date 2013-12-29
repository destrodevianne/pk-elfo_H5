package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

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
	 * @see pk.elfo.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String, pk.elfo.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
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
	 * @see pk.elfo.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
	 */
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}
