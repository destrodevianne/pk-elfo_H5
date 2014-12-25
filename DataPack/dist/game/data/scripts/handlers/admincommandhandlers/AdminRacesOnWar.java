package handlers.admincommandhandlers;

import pk.elfo.gameserver.custom.racesonwar.RacesOnWar;
import pk.elfo.gameserver.custom.racesonwar.StateRacesOnWar;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Broadcast;

/**
 * Projeto PkElfo
 */

public class AdminRacesOnWar implements IAdminCommandHandler
{
    private static final String[] _adminCommands =
    {
        "admin_startrw",
        "admin_stoprw"
    };

    @Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
        if (command.equals("admin_startrw"))
        {
            if (RacesOnWar.getStateRaceOnWar() == StateRacesOnWar.ACTIVE)
            {
                activeChar.sendMessage("[Races On War]: O Evento ja esta ativo!");
                return false;
            }
            RacesOnWar.setStateRaceOnWar(StateRacesOnWar.ACTIVE);
            RacesOnWar.notifyAllPlayer(true);
            Broadcast.announceToOnlinePlayers("[Races On War]: Evento foi iniciado!");
            System.out.println("[Races On War]: Evento foi iniciado!");
        }
        
        if (command.equals("admin_stoprw"))
        {
             if (RacesOnWar.getStateRaceOnWar() == StateRacesOnWar.INACTIVE)
             {
                 activeChar.sendMessage("[Races On War]: Evento ja foi finalizado!");
                 return false;
             }
             RacesOnWar.setStateRaceOnWar(StateRacesOnWar.INACTIVE);
             RacesOnWar.notifyAllPlayer(false);
             RacesOnWar.endRacesOnWar();
             Broadcast.announceToOnlinePlayers("[Races On War]: Evento foi finalizado!");
             System.out.println("[Race On War]: Evento foi finalizado");
        }
        return true;
    }

    @Override
    public String[] getAdminCommandList()
    {
        return _adminCommands;
    }
}