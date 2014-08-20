package pk.elfo.gameserver.model;

import java.util.Vector;

import pk.elfo.PkElfo_Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.olympiad.OlympiadManager;
import pk.elfo.gameserver.util.Broadcast;
import pk.elfo.util.Rnd;

public class RandomFight
{
    public static enum State{INACTIVE,REGISTER,LOADING,FIGHT}
    public static State state = State.INACTIVE;

   public static Vector<L2PcInstance> players = new Vector<>();

   protected void openRegistrations()
   {
       state = State.REGISTER;
       Broadcast.announceToOnlinePlayers("O Evento Random Fight vai comecar em 1 minute.");
       Broadcast.announceToOnlinePlayers("Para se registrar digite: ?register");
       Broadcast.announceToOnlinePlayers("Para cancelar digite: ?unregister a qualquer momento.");
       ThreadPoolManager.getInstance().scheduleGeneral(new checkRegist(), 300000 );
   }

   protected void checkRegistrations()
    {
       state=State.LOADING;

       if(players.isEmpty() || players.size() < 2)
       {
    	   Announcements.getInstance().announceToAll("O Evento Random Fight nao sera iniciado por falta de participantes.");
    	   Announcements.getInstance().announceToAll("Next Event in: "+PkElfo_Config.EVERY_MINUTES+" Minutes");
           clean();
           return;
       }
       Broadcast.announceToOnlinePlayers("Quantidade de registrados: "+players.size());
       Broadcast.announceToOnlinePlayers("2 Jogadores aleatorios serao escolhidos em 60 segundos!");
       ThreadPoolManager.getInstance().scheduleGeneral(new pickPlayers(), 60000 );
    }

   protected void pickPlayers()
    {
       if(players.isEmpty() || players.size() < 2)
        {
           Broadcast.announceToOnlinePlayers("O Evento Random Fight nao sera iniciado por falta de participantes.");
           clean();
           return;
       }

       for(L2PcInstance p : players)
    	   if(p.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(p))
           {
               players.remove(p);
               p.sendMessage("Voce automaticamente deixou o evento por causa de suas obrigacoes com as Olimpiadas.");
           }

       int rnd1=Rnd.get(players.size());
       int rnd2=Rnd.get(players.size());

       while(rnd2==rnd1)
           rnd2=Rnd.get(players.size());

       for(L2PcInstance player : players)
       {
           if(player != players.get(rnd1) && player != players.get(rnd2))
               players.remove(player);
       }
       Announcements.getInstance().announceToAll("Jogadores selecionados: "+players.firstElement().getName()+" VS "+players.lastElement().getName());
       Announcements.getInstance().announceToAll("Os jogadores vao ser teletransportados em 30 segundos");
       ThreadPoolManager.getInstance().scheduleGeneral(new teleportPlayers(), 30000);
    }

   protected void teleport()
    {
       if(players.isEmpty() || players.size() < 2)
        {
           Broadcast.announceToOnlinePlayers("O Evento Random Fight nao sera iniciado por falta de participantes.");
           clean();
           return;
       }
       Broadcast.announceToOnlinePlayers("Jogadores teleportados!");

       players.firstElement().teleToLocation(PkElfo_Config.TELEPORT_P1X,PkElfo_Config.TELEPORT_P1Y,PkElfo_Config.TELEPORT_P1Z);
       players.lastElement().teleToLocation(PkElfo_Config.TELEPORT_P2X,PkElfo_Config.TELEPORT_P2Y,PkElfo_Config.TELEPORT_P2Z);
       players.firstElement().setTeam(1);
       players.lastElement().setTeam(2);

       //para,etc

       players.firstElement().sendMessage("A luta vai comecar em 15 segundos!");
       players.lastElement().sendMessage("A luta vai comecar em 15 segundos!");

       ThreadPoolManager.getInstance().scheduleGeneral(new fight(), 15000);
    }

   protected void startFight()
    {

       if(players.isEmpty() || players.size() < 2)
        {
           Broadcast.announceToOnlinePlayers("O evento foi abortado porque um dos participantes esta offline!");
           clean();
           return;
       }

       state = State.FIGHT;
       Broadcast.announceToOnlinePlayers("FIGHT STARTED!");
       players.firstElement().updatePvPFlag(1);
       players.lastElement().updatePvPFlag(1);
       players.firstElement().sendMessage("Start Fight!!");
       players.lastElement().sendMessage("Start Fight!");
       ThreadPoolManager.getInstance().scheduleGeneral(new checkLast(), 120000 );
    }

    protected void lastCheck()
    {
       if(state == State.FIGHT)
       {
           if(players.isEmpty() || players.size() < 2)
           {
               revert();
               clean();
              return;
           }

           int alive=0;
           for(L2PcInstance player : players)
           {
               if(!player.isDead())
                   alive++;
           }

           if(alive==2)
           {
               Broadcast.announceToOnlinePlayers("O Evento Random Fight terminou!");
               clean();
               revert();
           }
       }
    }

   public static void revert()
   {
       if(!players.isEmpty())
       for(L2PcInstance p : players)
       {
           if(p == null)
               continue;

           if(p.isDead())
               p.doRevive();

           p.setCurrentHp(p.getMaxHp());
           p.setCurrentCp(p.getMaxCp());
           p.setCurrentMp(p.getMaxMp());
           p.broadcastUserInfo();
           p.teleToLocation(82698,148638,-3473);
       }
   }

   public static void clean()
   {
       if(state == State.FIGHT)
           for(L2PcInstance p : players)
               p.setTeam(0);

       players.clear();
       state = State.INACTIVE;
   }

   protected RandomFight()
   {
       ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new Event(), 60000 * PkElfo_Config.EVERY_MINUTES , 60000 * PkElfo_Config.EVERY_MINUTES);
   }

   public static RandomFight getInstance()
   {
       return SingletonHolder._instance;
   }

   private static class SingletonHolder
   {
       protected static final RandomFight _instance = new RandomFight();
   }

   protected class Event implements Runnable
   {
       @Override
       public void run()
       {
           if(state == State.INACTIVE)
               openRegistrations();
       }
   }

   protected class checkRegist implements Runnable
   {

       @Override
       public void run()
       {
               checkRegistrations();
       }
   }

   protected class pickPlayers implements Runnable
   {
       @Override
       public void run()
       {
           pickPlayers();
       }
   }

   protected class teleportPlayers implements Runnable
   {
       @Override
       public void run()
       {
           teleport();
       }
   }

   protected class fight implements Runnable
   {
       @Override
       public void run()
       {
           startFight();
       }
   }

   protected class checkLast implements Runnable
   {
       @Override
       public void run()
       {
           lastCheck();
       }
   }
}