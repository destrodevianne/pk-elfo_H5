package handler.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class Logout implements IVoicedCommandHandler
{
	private static String[]	VOICED_COMMANDS	=
        	{ 
        		"logout"
        	};
        
    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
        {
                if (command.equalsIgnoreCase("logout"))
                {
                	activeChar.logout(false);
                }
                return true;
        }
    
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}