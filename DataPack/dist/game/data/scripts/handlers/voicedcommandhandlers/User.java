package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.GameTimeController;
import king.server.gameserver.cache.HtmCache;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.ExShowScreenMessage;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */
public class User implements IVoicedCommandHandler
{
       public static final String[] VOICED_COMMANDS = { "user" , "user1", "user2", "user3" };
      
       @Override
       public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
       {
                  if (Config.SHOW_USER)
          {
                  if(command.equalsIgnoreCase("user"))
                          {
                                  User.showUserPage(activeChar, "user.htm");
                          }      
                  if(command.equalsIgnoreCase("user1"))
                          {
                                  User.showUserPage(activeChar, "user1.htm");
                          }  
                  if(command.equalsIgnoreCase("user2"))
                          {
                                  User.showUserPage(activeChar, "user2.htm");
                          }
                  if(command.equalsIgnoreCase("user3"))
                          {
                                  User.showUserPage(activeChar, "user3.htm");
                          }
         }
                  else
                  {
                                       activeChar.sendMessage("Este comando esta desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("Este comando foi desabiltado pelo ADM!", 4000);
                                       activeChar.sendPacket(message1);
                                       return false;
                  }    
                       return false;      
       }        
      
       public static String getServerRunTime()
       {
          int timeSeconds = (GameTimeController.getGameTicks() - 36000) / 10;
          String timeResult = "";
          if (timeSeconds >= 86400)
                  timeResult = Integer.toString(timeSeconds / 86400) + " Days " + Integer.toString((timeSeconds % 86400) / 3600) + " hours";
          else
                  timeResult = Integer.toString(timeSeconds / 3600) + " Hours " + Integer.toString((timeSeconds % 3600) / 60) + " mins";
          return timeResult;
       }
            
       public static String getRealOnline()
       {
          int counter = 0;
          for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
          {
                  if (onlinePlayer.isOnline() && (onlinePlayer.getClient() != null && !onlinePlayer.getClient().isDetached()))
                  {
                          counter++;
                  }
          }
          String realOnline = "<tr><td fixwidth=11></td><td FIXWIDTH=280>Players Ativos</td><td FIXWIDTH=470><font color=FF6600>" + counter + "</font></td></tr>" + "<tr><td fixwidth=11></td><td FIXWIDTH=280>Players com Shops</td><td FIXWIDTH=470><font color=FF6600>" + (L2World.getInstance().getAllPlayersCount() - counter) + "</font></td></tr>";
          return realOnline;
       }
      
       public static void showUserPage(L2PcInstance targetChar, String filename)
       {
          String content = HtmCache.getInstance().getHtmForce(targetChar.getHtmlPrefix(), "data/html/userpanel/" + filename);
          NpcHtmlMessage UserPanelReply = new NpcHtmlMessage(5);
          UserPanelReply.setHtml(content);
          UserPanelReply.replace("%online%", String.valueOf(L2World.getInstance().getAllPlayers().size()));
          UserPanelReply.replace("%name%", String.valueOf(targetChar.getName()));
          UserPanelReply.replace("%serveronline%", getRealOnline());
          UserPanelReply.replace("%servercapacity%", Integer.toString(Config.MAXIMUM_ONLINE_USERS));
          UserPanelReply.replace("%serverruntime%", getServerRunTime());
          UserPanelReply.replace("%playernumber%", String.valueOf(L2World.getInstance().getAllPlayers().size()));
                  if (!Config.ENABLE_SPECIAL_EFFECT)
                  {
                       UserPanelReply.replace("%effect%", "Disabled");
                  }
                  else if (L2PcInstance._isoneffect == true)
                  {
                          UserPanelReply.replace("%effect%", "ON");
                  }
                  else  
                  {
                          UserPanelReply.replace("%effect%", "OFF");
                  }
                  if (!Config.ENABLE_PM_REFUSAL)
                  {
                          UserPanelReply.replace("%pm%", "Disabled");
                  }
                  else if (L2PcInstance._ispmrefusal == true)
                  {
                          UserPanelReply.replace("%pm%", "ON");
                  }
                  else
                  {
                          UserPanelReply.replace("%pm%", "OFF");
                  }
                  if (!Config.ENABLE_BUFF_REFUSAL)
                  {
                          UserPanelReply.replace("%debuff%", "Disabled");
                  }
                  else if (L2PcInstance._isbuffrefusal == true)
                  {
                          UserPanelReply.replace("%debuff%", "ON");
                  }
                  else
                  {
                          UserPanelReply.replace("%debuff%", "OFF");
                  }
                  if (!Config.ENABLE_TRADE_REFUSAL)
                  {
                          UserPanelReply.replace("%trade%", "Disabled");
                  }
                  else if (L2PcInstance._istraderefusal == true)
                  {
                          UserPanelReply.replace("%trade%", "ON");
                  }
                  else
                  {
                          UserPanelReply.replace("%trade%", "OFF");
                  }
                  if (!Config.ENABLE_EXP_REFUSAL)
                  {
                       UserPanelReply.replace("%exp%", "Disabled");
                  }
                  else if (L2PcInstance._isexpsprefusal == true)
                  {
                       UserPanelReply.replace("%exp%", "ON");
                  }
                  else
                  {
                       UserPanelReply.replace("%exp%", "OFF");
                  }
                  if (!Config.ENABLE_SPECIAL_EFFECT)
                  {
                          UserPanelReply.replace("%effect%", "Disabled");
                  }
                  else if (L2PcInstance._isoneffect == true)
                  {
                          UserPanelReply.replace("%effect%", "ON");
                  }
                  else  
                  {
                          UserPanelReply.replace("%effect%", "OFF");
                  }               
                  targetChar.sendPacket(UserPanelReply);
        }

      
       @Override
       public String[] getVoicedCommandList()
       {
               return VOICED_COMMANDS;
       }
}