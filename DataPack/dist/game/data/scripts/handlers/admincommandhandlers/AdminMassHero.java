package handlers.admincommandhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.SocialAction;

/**
 * Projeto PkElfo
 */
 
public class AdminMassHero implements IAdminCommandHandler
{
    protected static final Logger _log = Logger.getLogger(AdminMassHero.class.getName());

    @Override
    public String[] getAdminCommandList()
    {
        return ADMIN_COMMANDS;
    }

    @SuppressWarnings("cast")
    @Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if(activeChar == null)
        {
            return false;
        }

        if(command.startsWith("admin_masshero"))
        {
            for(L2PcInstance player : L2World.getInstance().getAllPlayers().valueCollection())
            {
                if(player instanceof L2PcInstance)
                {
                    /* Check to see if the player already is Hero */
                    if(!player.isHero())
                    {
                        player.setHero(true);
                        player.sendMessage("Admin is rewarding all online players with Hero Status.");
                        player.broadcastPacket(new SocialAction(player.getObjectId(), 16));
                        player.broadcastUserInfo();
                    }
                    player = null;
                }
            }
        }
        return true;
    }

    private static String[] ADMIN_COMMANDS = { "admin_masshero" };
}