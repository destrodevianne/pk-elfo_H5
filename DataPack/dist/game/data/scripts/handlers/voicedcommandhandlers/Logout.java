package handler.voicedcommandhandlers;

import .gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class Logout implements IVoicedCommandHandler
{
        private static final String[] VOICED_COMMANDS = { "logout"};

        public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
        {
                if (command.equalsIgnoreCase("logout"))
                        activeChar.logout(false);
                return true;
        }

        public String[] getVoicedCommandList()
        {
                return VOICED_COMMANDS;
        }
}