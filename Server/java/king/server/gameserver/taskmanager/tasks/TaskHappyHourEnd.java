package king.server.gameserver.taskmanager.tasks;

import king.server.Config;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.ExShowScreenMessage;
import king.server.gameserver.taskmanager.Task;
import king.server.gameserver.taskmanager.TaskManager.ExecutedTask;

/**
 * PkElfo
 */
public class TaskHappyHourEnd extends Task
{
       private static final String NAME = "happy_hour_end";
     
       @Override
       public String getName() { return NAME; }

       @Override
       public void onTimeElapsed(ExecutedTask task)
       {
               _log.info("HappyHour finalizada.");
                             
               _log.info("Carregando e usando as configuracoes padrao.");
               Config.RATE_XP = OldConfig.RATE_XP.getFloatRate();
               Config.RATE_SP = OldConfig.RATE_SP.getFloatRate();
               Config.RATE_PARTY_XP = OldConfig.RATE_PARTY_XP.getFloatRate();
               Config.RATE_PARTY_SP = OldConfig.RATE_PARTY_SP.getFloatRate();
               Config.RATE_DROP_ITEMS = OldConfig. RATE_DROP_ITEMS.getFloatRate();
               Config.RATE_DROP_ITEMS_BY_RAID = OldConfig.RATE_DROP_ITEMS_BY_RAID.getFloatRate();
               Config.RATE_DROP_SPOIL = OldConfig.RATE_DROP_SPOIL.getFloatRate();
               Config.RATE_DROP_MANOR = OldConfig.RATE_DROP_MANOR.getIntegerRate();
               Config.RATE_QUEST_DROP = OldConfig.RATE_QUEST_DROP.getFloatRate();
               Config.RATE_QUEST_REWARD_XP = OldConfig.RATE_QUEST_REWARD_XP.getFloatRate();
               Config.RATE_QUEST_REWARD_SP = OldConfig.RATE_QUEST_REWARD_SP.getFloatRate();
               Config.RATE_QUEST_REWARD_POTION = OldConfig.RATE_QUEST_REWARD_POTION.getFloatRate();
               Config.RATE_QUEST_REWARD_SCROLL = OldConfig.RATE_QUEST_REWARD_SCROLL.getFloatRate();
               Config.RATE_QUEST_REWARD_RECIPE = OldConfig.RATE_QUEST_REWARD_RECIPE.getFloatRate();
               Config.RATE_QUEST_REWARD_MATERIAL = OldConfig.RATE_QUEST_REWARD_MATERIAL.getFloatRate();
               Config.ENCHANT_CHANCE = OldConfig.ENCHANT_CHANCE.getDoubleRate();
             
               // Y de esta ingeniosa forma se actualiza la informacion del Community
               _log.info(" RATE_XP: " + Config.RATE_XP); _log.info(" RATE_SP: " + Config.RATE_SP);
               _log.info(" RATE_PARTY_XP: " + Config.RATE_PARTY_XP); _log.info(" RATE_PARTY_SP: " + Config.RATE_PARTY_SP);
               _log.info(" RATE_DROP_ITEMS: " + Config.RATE_DROP_ITEMS); _log.info(" RATE_DROP_ITEMS_BY_RAID: " + Config.RATE_DROP_ITEMS_BY_RAID);
               _log.info(" RATE_DROP_SPOIL: " + Config.RATE_DROP_SPOIL); _log.info(" RATE_DROP_MANOR: " + Config.RATE_DROP_MANOR);
               _log.info(" RATE_QUEST_DROP: " + Config.RATE_QUEST_DROP); _log.info(" RATE_QUEST_REWARD_XP: " + Config.RATE_QUEST_REWARD_XP);
               _log.info(" RATE_QUEST_REWARD_SP: " + Config.RATE_QUEST_REWARD_SP); _log.info(" RATE_QUEST_REWARD_POTION: " + Config.RATE_QUEST_REWARD_POTION);
               _log.info(" RATE_QUEST_REWARD_SCROLL: " + Config.RATE_QUEST_REWARD_SCROLL); _log.info(" RATE_QUEST_REWARD_RECIPE: " + Config.RATE_QUEST_REWARD_RECIPE);
               _log.info(" RATE_QUEST_REWARD_RECIPE: " + Config.RATE_QUEST_REWARD_RECIPE); _log.info(" RATE_QUEST_REWARD_MATERIAL: " + Config.RATE_QUEST_REWARD_MATERIAL);
               _log.info(" ENCHANT_CHANCE: " + Config.ENCHANT_CHANCE);
             
               ExShowScreenMessage ess = new ExShowScreenMessage("Evento Happyhour finalizado!",1000);
               L2PcInstance[] PjsOnline = L2World.getInstance().getAllPlayersArray();
               for (L2PcInstance pj : PjsOnline)
                       pj.sendPacket(ess);
                             
       }
     
       @Override
       public void initializate()
       {
               super.initializate();
               _log.info("*** Happyhour (End time) *** Inicializada");
       }

}
