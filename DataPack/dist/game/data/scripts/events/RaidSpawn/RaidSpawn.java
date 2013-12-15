package events.RaidSpawn;

import java.util.List;

import javolution.util.FastList;

import king.server.gameserver.Announcements;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2MonsterInstance;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.quest.Quest;

/**
 * PkElfo
 */
public class RaidSpawn extends Quest
{
   private static final int _first_event = 5;// min
   private static final int _time_event = 10;// min
   
   private static List<L2Npc> _npc_spawn = new FastList<>();
   
   private static final int[] _raids =
   {
      25514,
      22216,
      25286,
      25283
   };
   
   private static final String[] _name_raids =
   {
      "Queen Shyeed",
      "Tyrannosaurus",
      "Anakim",
      "Lilith"
   };
   
   private static final String[] _locations =
   {
      "in the colliseum",
      "near the entrance of the Garden of Eva",
      "close to the western entrance of the Cemetary",
      "at Gludin's Harbor"
   };
   
   /**
    * x, y, z.
    */
   private static final int[][] _spawns =
   {
      {
         150086,
         46733,
         -3407
      },
      {
         84805,
         233832,
         -3669
      },
      {
         161385,
         21032,
         -3671
      },
      {
         89199,
         149962,
         -3581
      }
   };
   
   /**
    * ItemdId, Chance, Max Drop, Min Drop.
    */
   private static final int[][] DROPLIST =
   {
      {// Giant's Codex
         6622,
         50,
         1,
         1
      },
      {// Revita Pop
         20034,
         50,
         1,
         1
      }
   
   };
   
   public RaidSpawn()
   {
      super(-1, RaidSpawn.class.getSimpleName(), "custom");
      
      addKillId(_raids);
      
      startQuestTimer("SpawnRaid", _first_event * 60000, null, null);
   }
   
   public static void main(String[] args)
   {
      new RaidSpawn();
   }
   
   @Override
   public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
   {
      cancelQuestTimer("DespawnRaid", null, null);
      startQuestTimer("SpawnRaid", _time_event * 60000, null, null);
      
      dropItem(npc, killer, DROPLIST);
      _npc_spawn.clear();
      Announcements.getInstance().announceToAll("Next Raid Spawn in " + _time_event);
      return super.onKill(npc, killer, isSummon);
   }
   
   @Override
   public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
   {
      if (event.equals("SpawnRaid"))
      {
         final int random = getRandom(_raids.length - 1);
         
         L2Npc mobs = addSpawn(_raids[random], _spawns[random][0], _spawns[random][1], _spawns[random][2], 0, false, 0);
         _npc_spawn.add(mobs);
         
         Announcements.getInstance().announceToAll("Raid " + _name_raids[random] + " Spawn " + _locations[random]);
         Announcements.getInstance().announceToAll("Have " + _time_event + " minutes to kill");
         
         startQuestTimer("DespawnRaid", _time_event * 60000, null, null);
         return null;
      }
      if (event.equals("DespawnRaid"))
      {
         if (!_npc_spawn.isEmpty())
         {
            for (L2Npc h : _npc_spawn)
            {
               h.deleteMe();
            }
         }
         _npc_spawn.clear();
         startQuestTimer("SpawnRaid", 1000, null, null);// 1 min spawn raid
         return null;
      }
      return null;
   }
   
   private static void dropItem(L2Npc mob, L2PcInstance player, int[][] droplist)
   {
      final int chance = getRandom(100);
      
      for (int[] drop : droplist)
      {
         if (chance > drop[1])
         {
            ((L2MonsterInstance) mob).dropItem(player, drop[0], getRandom(drop[2], drop[3]));
            return;
         }
      }
   }
}