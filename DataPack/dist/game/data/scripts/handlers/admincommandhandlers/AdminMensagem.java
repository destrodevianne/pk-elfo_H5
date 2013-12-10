package handlers.admincommandhandlers;

import java.util.Collection;

import king.server.gameserver.handler.IAdminCommandHandler;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 *
 * PkElfo
 *
 */

// Comando custom para enviar Mensagens na tela
public class AdminMensagem implements IAdminCommandHandler
{
        private static final String[] ADMIN_COMMANDS =
        {
                "admin_msgtela",
        };

        @Override
        public boolean useAdminCommand(String command, L2PcInstance activeChar)
        {
                if (command.startsWith("admin_msgtela"))
                {
                        command = command.substring(13);
                        command = activeChar.getName() + ": " + command;
                       
                        final ExShowScreenMessage cs; 
                        cs = new ExShowScreenMessage (command, 1500);
                        Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().valueCollection();
                       
                        for (L2PcInstance playersOnline : pls)
                        {
                                if (playersOnline == null)
                                {
                                        continue;
                                }
                                playersOnline.sendPacket(cs);// envio da mensagem
                        }
                }
                else
                {
                        activeChar.sendMessage("Comando Correto: //msgtela + texto ");
                        return false; 
                }
                return false;
        }

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}