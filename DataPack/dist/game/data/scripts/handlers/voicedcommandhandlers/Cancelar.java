package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class Cancelar implements IVoicedCommandHandler
{
        private static final String[] VOICED_COMMANDS =
        {
                "cancelar"
        };
        
        @Override
        public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
        {
                if (command.equalsIgnoreCase("cancelar") && activeChar.isVip());
                {
                        activeChar.stopAllEffects();
                }
 
                return true;
        }
        
		@Override
		public String[] getVoicedCommandList()
		{
			return _voicedCommands;
		}
}