package pk.elfo.gameserver.handler.custom;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class Cancelar implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"cancelar"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("cancelar") && activeChar.isVip())
		{
			activeChar.stopAllEffects();
		}
		
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}