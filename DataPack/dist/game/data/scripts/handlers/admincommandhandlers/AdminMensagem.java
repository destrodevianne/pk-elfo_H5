package handlers.admincommandhandlers;

import java.util.Collection;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 *
 * Projeto PkElfo
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
                        AdminHelpPage.showHelpPage(activeChar, "main_menu.htm");
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