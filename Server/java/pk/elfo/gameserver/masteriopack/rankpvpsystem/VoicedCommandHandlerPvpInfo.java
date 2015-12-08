package pk.elfo.gameserver.masteriopack.rankpvpsystem;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */
public class VoicedCommandHandlerPvpInfo implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS = {"pvpinfo"};

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if(activeChar == null)
			return false;
		
		if(activeChar.getRPSCookie() == null)
			return false;
		
		RPSCookie pc = activeChar.getRPSCookie();
		
		// reset death status:
		if(!activeChar.isDead())
		{
			pc.setDeathStatus(null);
		}
		
		// save target of active player when command executed:
		if(activeChar.getTarget() != null && activeChar.getTarget() instanceof L2PcInstance)
		{
			pc.setTarget((L2PcInstance) activeChar.getTarget());
		}
		else
		{
			pc.setTarget(activeChar);
			activeChar.sendMessage("PvP Status executed on self!");
		}

		RPSHtmlPvpStatus.sendPage(activeChar, pc.getTarget());

	    return true;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}

}