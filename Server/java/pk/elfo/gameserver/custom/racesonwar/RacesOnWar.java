package pk.elfo.gameserver.custom.racesonwar;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExShowScreenMessage;
import pk.elfo.gameserver.util.Broadcast;

/**
 * Projeto PkElfo
 */

public class RacesOnWar
{
    private static StateRacesOnWar stateRaceOnWar = StateRacesOnWar.INACTIVE;
 
    private static int dElfoPoint = 0;
    private static int elfoPoint = 0;
    private static int humanPoint = 0;
    private static int orcPoint = 0;
    private static int dwarfPoint = 0;
	private static int kamaelPoint = 0;

    public static void setStateRaceOnWar(StateRacesOnWar state)
    {
        stateRaceOnWar = state;
    }
  
    public static StateRacesOnWar getStateRaceOnWar()
    {
        return stateRaceOnWar;
    }
 
    public static void onDie(L2PcInstance player, L2PcInstance killer)
    {
        if (player != null)
        {
            if (player.getClient().getConnection().getInetAddress().getHostAddress().equalsIgnoreCase(killer.getClient().getConnection().getInetAddress().getHostAddress()))
            {   
                killer.sendMessage("[Races On War]: Player " + player.getName() + " detectado com o mesmo IP!");
                player.sendMessage("[Races On War]: Player " + killer.getName() + " detectado com o mesmo IP!");
                return;
            }
            
            else if (player.getRace() != killer.getRace())
            {                    
                //              ID  QUANTIDADE
                killer.addItem("", 57, 10000000, null, true);
                addPointToRace(killer);
                Broadcast.announceToOnlinePlayers("[Races On War]: Player "+player.getName() + "(" + player.getRace()+") foi derrotado por " + killer.getName() + "("+killer.getRace()+")");
            }
        }
    }

    public static void notifyAllPlayer(boolean notify)
    {
        if (notify)
        {
            for (L2PcInstance p : L2World.getInstance().getAllPlayers().values())
                p.sendPacket(new ExShowScreenMessage("[Race On War]: Iniciado !!! Go Go Go!!!" , 10000));
        }
        else
        {
            for (L2PcInstance p : L2World.getInstance().getAllPlayers().values())        
                p.sendPacket(new ExShowScreenMessage("[Race On War]: Finalizado !!!" , 10000));             
        }
    }
    
    private static void addPointToRace(L2PcInstance player)
    {
        switch (player.getRace())
        {
            case DarkElf:
                ++dElfoPoint;
            break;
          
            case Elf:
                ++elfoPoint;
            break;
                
            case Human:
                ++humanPoint;
            break;
            
            case Orc:
                ++orcPoint;
            break;
            
            case Dwarf:
                ++dwarfPoint;
            break;
			
			case Kamael:
                ++kamaelPoint;
            break;
        }
    }

    public static void endRacesOnWar()
    {    
        Broadcast.announceToOnlinePlayers("[Races On War]: Dark Elfos Total de kills -> "+dElfoPoint);               
        Broadcast.announceToOnlinePlayers("[Races On War]: Elfos Total de Kills -> " +elfoPoint);             
        Broadcast.announceToOnlinePlayers("[Races On War]: Humanos Total de Kills -> "+humanPoint);             
        Broadcast.announceToOnlinePlayers("[Races On War]: Orcs Total de Kills -> " +orcPoint);             
        Broadcast.announceToOnlinePlayers("[Races On War]: Anoes Total de Kills -> " +dwarfPoint);
		Broadcast.announceToOnlinePlayers("[Races On War]: Kamaels Total de Kills -> " +kamaelPoint);
       
        dElfoPoint = 0;
        elfoPoint = 0;
        humanPoint = 0;
        orcPoint = 0;
        dwarfPoint = 0;
		kamaelPoint = 0;
        
        setStateRaceOnWar(StateRacesOnWar.INACTIVE);
    }
}