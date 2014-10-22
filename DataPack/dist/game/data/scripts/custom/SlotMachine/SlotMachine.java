package custom.SlotMachine;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javolution.text.TextBuilder;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.Rnd;
import pk.elfo.util.StringUtil;

/**
 * PkElfo
 */

public class SlotMachine extends Quest
{
       private static final String qn = "SlotMachine";
       private static String servername = "";
       private static List<int[]> rewards;
       private static List<int[]> feed;
       private static int npcid = 0;

       public SlotMachine(int questId, String name, String descr)
       {
    	   super(questId, name, descr);
    	   loadConfigs();
    	   addStartNpc(npcid);
    	   addTalkId(npcid);
    	   addFirstTalkId(npcid); 
       }
      
       @Override
       public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
       {
    	   if (npc.getNpcId() == npcid && event.equals("play") && checkstatus(player))
               {
    		   for (int[] i : feed)
    		   {
    			   if(player.getInventory().getItemByItemId(i[0]) == null || (player.getInventory().getItemByItemId(i[0]) != null && player.getInventory().getItemByItemId(i[0]).getCount() < i[1]))
    			   {
    				   player.sendMessage("Voce nao tem itens suficientes.");
    				   return null;
    			   }
    		   }
                      
    		   for (int[] i : feed)
    		   {
    			   player.destroyItem("SlotMachineConsumition", player.getInventory().getItemByItemId(i[0]).getObjectId(), i[1], null, false);
    		   }
                      
    		   try
    		   {
    			   for (int[] feeds : feed)
    			   {
    				   player.sendMessage("Consumido: "+feeds[1]+" de "+ItemTable.getInstance().createDummyItem(feeds[0]).getItemName()+" por SlotMachine.");
    			   }      
    			   run(player);
    		   }
    		   catch (InterruptedException e)
    		   {
    			   e.printStackTrace();
    		   }
               }
    	   return null;
       }
      
       void checkresult(L2PcInstance player)
       {
               SystemMessage systemMessage = null;
              
               if(player.win)
               {
                       for (int[] r : rewards)
                       {
                               PcInventory inv = player.getInventory();
                              
                               if (ItemTable.getInstance().createDummyItem(r[0]).isStackable())
                               {
                                       inv.addItem("SlotMachine", r[0], r[1], player, player);
                                      
                                       if (r[1] > 1)
                                       {
                                               systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
                                               systemMessage.addItemName(r[0]);
                                               systemMessage.addItemNumber(r[1]);
                                       }
                                       else
                                       {
                                               systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                                               systemMessage.addItemName(r[0]);
                                       }
                                       player.sendPacket(systemMessage);
                               }
                               else
                               {
                                       for (int i = 0; i < r[1]; ++i)
                                       {
                                               inv.addItem("SlotMachine", r[0], 1, player, player);
                                               systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                                               systemMessage.addItemName(r[0]);
                                               player.sendPacket(systemMessage);
                                       }
                               }
                       }
                       showresult(player, true);
                       player.useMagic(SkillTable.getInstance().getInfo(5965, 1), true, true);
               }
               else
               {
                       showresult(player, false);
               }
               player.setIsImmobilized(false);
               player.win = false;
       }
      
       boolean checkstatus(L2PcInstance player)
       {
               if(player == null)
               {
                       return false;
               }
               return true;
       }
      
       void run(L2PcInstance player) throws InterruptedException
       {
               int a = Rnd.get(10,30);
               int ar = a % 3;
               int b = Rnd.get(10,30);
               int br = b % 3;
               int c = Rnd.get(10,30);
               int i = 1;
               player.setIsImmobilized(true);
              
               while(i <= a)
               {
                       if (!checkstatus(player))
                       {
                               return;
                       }
                      
                       if(i % 3 == 0)
                       {
                               showpage(player, "a", "b", "c");
                       }
                       else if(i % 3 == 1)
                       {
                               showpage(player, "b", "b", "c");
                       }
                       else if(i % 3 == 2)
                       {
                               showpage(player, "c", "b", "c");
                       }
                       Thread.sleep(150);
                       i++;
               }
              
               Thread.sleep(1000);
               i = 1;
               while(i <= b)
               {
                       if (!checkstatus(player))
                       {
                               return;
                       }
                      
                       if (ar == 0)
                       {
                               if(i % 3 == 0)
                               {
                                       showpage(player, "a", "b", "c");
                               }                              
                               else if(i % 3 == 1)
                               {
                                       showpage(player, "a", "c", "c");
                               }
                               else if(i % 3 == 2)
                               {
                                       showpage(player, "a", "a", "c");
                               }
                       }
                      
                       else if (ar == 1)
                       {
                               if(i % 3 == 0)
                               {
                                       showpage(player, "b", "b", "c");
                               }
                               else if(i % 3 == 1)
                               {
                                       showpage(player, "b", "c", "c");
                               }
                               else if(i % 3 == 2)
                               {
                                       showpage(player, "b", "a", "c");
                               }
                       }
                      
                       else if (ar == 2)
                       {
                               if(i % 3 == 0)
                               {
                                       showpage(player, "c", "b", "c");
                               }
                               else if(i % 3 == 1)
                               {
                                       showpage(player, "c", "c", "c");
                                       player.win = true;
                               }
                               else if(i % 3 == 2)
                               {
                                       showpage(player, "c", "a", "c");
                               }
                       }
                       Thread.sleep(150);
                       i++;
               }
              
               Thread.sleep(1000);
               i = 1;
               while(i <= c)
               {
                       if (!checkstatus(player))
                       {
                               return;
                       }
                      
                       if (br == 0)
                       {
                               if (ar == 0)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "a", "b", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "a", "b", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "a", "b", "b");
                                       }
                               }
                              
                               else if (ar == 1)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "b", "b", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "b", "b", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "b", "b", "b");
                                               player.win = true;
                                       }
                               }
                              
                               else if (ar == 2)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "c", "b", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "c", "b", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "c", "b", "b");
                                       }
                               }
                       }
                      
                       else if (br == 1)
                       {
                               if (ar == 0)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "a", "c", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "a", "c", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "a", "c", "b");
                                       }
                               }
                              
                               else if (ar == 1)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "b", "c", "c");
                                      }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "b", "c", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "b", "c", "b");
                                       }
                               }
                              
                               else if (ar == 2)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "c", "c", "c");
                                               player.win = true;
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "c", "c", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "c", "c", "b");
                                       }
                               }
                       }
                      
                       else if (br == 2)
                       {
                               if (ar == 0)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "a", "a", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "a", "a", "a");
                                               player.win = true;
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "a", "a", "b");
                                       }
                               }
                              
                               else if (ar == 1)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "b", "a", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "b", "a", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "b", "a", "b");
                                       }
                               }
                              
                               else if (ar == 2)
                               {
                                       if(i % 3 == 0)
                                       {
                                               showpage(player, "c", "a", "c");
                                       }
                                       else if(i % 3 == 1)
                                       {
                                               showpage(player, "c", "a", "a");
                                       }
                                       else if(i % 3 == 2)
                                       {
                                               showpage(player, "c", "a", "b");
                                       }
                               }
                       }
                       Thread.sleep(150);
                       i++;
               }
               Thread.sleep(2000);
              
               if(checkstatus(player))
               {
                       checkresult(player);
               }
       }
      
       @Override
       public String onFirstTalk(L2Npc npc, L2PcInstance player)
       {
               showmain(player);
               return null;
       }
      
       void showpage(L2PcInstance activeChar, String a, String b, String c)
       {
    	   NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
    	   TextBuilder replyMSG = new TextBuilder("<html><title>"+servername+" Evento Slot Machine</title><body>");
    	   replyMSG.append("<center><img src=\"l2ui.squaregray\" width=290 height=1></center>");
    	   replyMSG.append("<center>" + "<table width=270 cellpadding=0 cellspacing=0>" + "<tr>" + "<td width=270>" + "<table width=270  height=50 cellspacing=0 cellpadding=0>" + "<tr>");
    	   replyMSG.append("<td width=45>" + "<center><img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32></center>" + "</td>");
    	   replyMSG.append("<td width=170>" + "<center><font color=af9f47>Menu Principal Slot Machine</font></center>");
    	   replyMSG.append("</td>" + "<td width=32>" + "<img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32>" + "</td>" + "</tr>" + "</table>" + "<center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" + "<table width=270 height=30 bgcolor=5b574c cellspacing=0 cellpadding=7>" + "<tr>" + "<td valign=top>" + "<table width=270 cellspacing=0 cellpadding=0>" + "<tr>");
    	   replyMSG.append("<td><center>Jogando com Slot Machine...</center>" + "</td>" + "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>");
    	   replyMSG.append("<br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" + "<table width=270 height=30 cellpadding=0 cellspacing=0 valign=top>" + "<tr>" + "<td>" + "<table width=270 height=40 bgcolor=090908 cellspacing=0 cellpadding=7>" + "<tr>" + "<td valign=top>" + "<table width=270 cellspacing=0 cellpadding=0>" + "<tr>");
    	   replyMSG.append("<td height=24 valign=top>" + "<center>" + "<table cellspacing=-1>" + "<tr>" + "<td><img src=\"icon.etc_dice_"+a+"_i00\" " + "width=32 height=32>" + "</td>" + "<td>" + "<img src=\"icon.etc_dice_"+b+"_i00\" width=32 height=32>" + "</td>" + "<td>" + "<img src=\"icon.etc_dice_"+c+"_i00\" width=32 height=32>" + "</td>" + "</tr>" + "</table>" + "</center><br><br>");
    	   replyMSG.append("</td><br>" + "<br></tr>" + "</table>" + "</td>" + "</tr>" + "</table>" + "</td>" + "</tr>" + "</table>" + "<br><br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>");
    	   replyMSG.append("<center><table width=270>" + "<tr>" + "<td width=270>" + "<center><font color=444444>By PkElfo</color><br>" + "</center>" + "</td>" + "</tr>" + "</table></center>");
    	   replyMSG.append("</tr>" + "</td>" + "</table>" + "</center>");
    	   replyMSG.append("</body></html>");
    	   adminReply.setHtml(replyMSG.toString());
    	   activeChar.sendPacket(adminReply);            
       }
      
       void showmain(L2PcInstance activeChar)
       {
               NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
               TextBuilder replyMSG = new TextBuilder("<html><title>"+servername+" Evento Slot Machine</title><body>");
               replyMSG.append("<center><img src=\"l2ui.squaregray\" width=290 height=1></center>");
        replyMSG.append("<center>" +
        "<table width=270 cellpadding=0 cellspacing=0>" +
               "<tr>" +
                       "<td width=270>" +     
                               "<table width=270  height=50 cellspacing=0 cellpadding=0>" +
                                       "<tr>");
               replyMSG.append("<td width=45>" +
                                                       "<center><img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32></center>" +
                                               "</td>");
               replyMSG.append("<td width=170>" +                     
                                       "<center><font color=af9f47>Menu Principal Slot Machine</font></center>");
               replyMSG.append("</td>" +
                                               "<td width=32>" +
                                               "<img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32>" +
                                               "</td>" +
                               "</tr>" +
                               "</table>" +
                              
                                       "<center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" +
                              
                               "<table width=270 height=30 bgcolor=5b574c cellspacing=0 cellpadding=7>" +
                                       "<tr>" +
                                               "<td valign=top>" +
                                                       "<table width=270 cellspacing=0 cellpadding=0>" +
                                                               "<tr>");
                                       replyMSG.append("<td><center>Voce quer jogar?</center>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table>" +
                                               "</td>" +
                                       "</tr>" +
                               "</table>");
  
                               replyMSG.append("<br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" +
                              
                               "<table width=270 height=30 cellpadding=0 cellspacing=0 valign=top>" +
                                       "<tr>" +
                                               "<td>" +
                                                       "<table width=270 height=40 bgcolor=090908 cellspacing=0 cellpadding=7>" +
                                                               "<tr>" +
                                                                       "<td valign=top>" +
                                                                               "<table width=270 cellspacing=0 cellpadding=0>" +
                                                                                       "<tr>");
                                                               replyMSG.append("<td height=24 valign=top>Oi <font color=LEVEL>"+activeChar.getName()+"</font>! Voce esta no Evento SlotMachine." +
                                                                               " Toda vez que voce jogar aqui voce vai perder:<br>");
                                                               for (int[] feeds : feed)
                                                               {
                                                                       replyMSG.append("-<font color=LEVEL>"+feeds[1]+"</font> de <font color=LEVEL>"+ItemTable.getInstance().createDummyItem(feeds[0]).getItemName()+"</font><br>");
                                                               }      
                                                              
                                                               replyMSG.append("<center><button value=\"Vamos jogar\" action=\"bypass -h Quest SlotMachine play\" width=70 height=30 " +
                                                                       "back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center><br>");
                                                       replyMSG.append("</td><br></tr>" +
                                                                               "</table>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table>" +
                                               "</td>" +
                                       "</tr>" +
                               "</table>" +
                              
                               "<br><br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>");
        
        replyMSG.append("<center><table width=270>" +
                                                               "<tr>" +
                                                                       "<td width=270>" +
                                                                               "<center><font color=444444>By PkElfo</color><br>" +
                                                                               "</center>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table></center>");
        replyMSG.append("</tr>" +
                       "</td>" +
               "</table>" +
               "</center>");
               replyMSG.append("</body></html>");
        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);            
       }
      
       void showresult(L2PcInstance activeChar, boolean l)
       {              
               String result ="";
               if(l)
               {
                       result = "Parabens, voce ganhou!";
               }
               else
               {
                       result = "Sinto muito que voce perdeu...";
               }
               NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
               TextBuilder replyMSG = new TextBuilder("<html><title>"+servername+" Evento Slot Machine</title><body>");
               replyMSG.append("<center><img src=\"l2ui.squaregray\" width=290 height=1></center>");
        replyMSG.append("<center>" +
        "<table width=270 cellpadding=0 cellspacing=0>" +
               "<tr>" +
                       "<td width=270>" +     
                               "<table width=270  height=50 cellspacing=0 cellpadding=0>" +
                                       "<tr>");
               replyMSG.append("<td width=45>" +
                                                       "<center><img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32></center>" +
                                               "</td>");
               replyMSG.append("<td width=170>" +                     
                                       "<center><font color=af9f47>Menu principal do Slot Machine</font></center>");
               replyMSG.append("</td>" +
                                               "<td width=32>" +
                                               "<img src=br_cashtex.item.br_cash_rune_of_rp_i00 width=32 height=32>" +
                                               "</td>" +
                               "</tr>" +
                               "</table>" +
                              
                                       "<center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" +
                              
                               "<table width=270 height=30 bgcolor=5b574c cellspacing=0 cellpadding=7>" +
                                       "<tr>" +
                                               "<td valign=top>" +
                                                       "<table width=270 cellspacing=0 cellpadding=0>" +
                                                               "<tr>");
              
                                       replyMSG.append("<td><center>"+result+"</center>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table>" +
                                               "</td>" +
                                       "</tr>" +
                               "</table>");
  
                               replyMSG.append("<br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>" +
                              
                               "<table width=270 height=30 cellpadding=0 cellspacing=0 valign=top>" +
                                       "<tr>" +
                                               "<td>" +
                                                       "<table width=270 height=40 bgcolor=090908 cellspacing=0 cellpadding=7>" +
                                                               "<tr>" +
                                                                       "<td valign=top>" +
                                                                               "<table width=270 cellspacing=0 cellpadding=0>" +
                                                                                       "<tr>");
                                                               replyMSG.append("<td height=24 valign=top>Voce quer jogar novamente <font color=LEVEL>"+activeChar.getName()+"</font>?");
                                                               replyMSG.append("<center><button value=\"Vamos jogar\" action=\"bypass -h Quest SlotMachine play\" width=70 height=30 " +
                                                                       "back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center><br>");
                                                       replyMSG.append("</td><br></tr>" +
                                                                               "</table>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table>" +
                                               "</td>" +
                                       "</tr>" +
                               "</table>" +
                              
                               "<br><br><center><img src=\"l2ui.squaregray\" width=290 height=1></center><br>");
        
        replyMSG.append("<center><table width=270>" +
                                                               "<tr>" +
                                                                       "<td width=270>" +
                                                                               "<center><font color=444444>By PkElfo</color><br>" +
                                                                               "</center>" +
                                                                       "</td>" +
                                                               "</tr>" +
                                                       "</table></center>");
        replyMSG.append("</tr>" +
                       "</td>" +
               "</table>" +
               "</center>");
               replyMSG.append("</body></html>");
        adminReply.setHtml(replyMSG.toString());
        activeChar.sendPacket(adminReply);    
       }
      
       void loadConfigs()
       {
               try
               {
                       Properties prop = new Properties();
                       prop.load(new FileInputStream(new File("./config/Eventos/SlotMachine.properties")));
                       servername = prop.getProperty("ServerName", "PkElfo");
                       npcid = Integer.parseInt(prop.getProperty("NpcId", "100"));
                       rewards = new ArrayList<>();
                       String[] propertySplit = prop.getProperty("Rewards", "57,100").split(";");
                      
                       for (String reward : propertySplit)
                       {
                               String[] rewardSplit = reward.split(",");
                              
                               if (rewardSplit.length != 2)
                               {
                                       _log.warning(StringUtil.concat("SlotMachine invalid config property -> Reward \"", reward, "\""));
                               }
                               else
                               {
                                       try
                                       {
                                               rewards.add(new int[]{Integer.parseInt(rewardSplit[0]), Integer.parseInt(rewardSplit[1])});
                                       }
                                       catch (NumberFormatException nfe)
                                       {
                                               if (!reward.isEmpty())
                                               {
                                                       _log.warning(StringUtil.concat("SlotMachine invalid config property -> Reward \"", reward, "\""));
                                               }
                                       }
                               }
                       }      
                       feed = new ArrayList<>();
                       propertySplit = prop.getProperty("Feed", "57,1").split(";");
                      
                       for (String feeds : propertySplit)
                       {
                               String[] feedSplit = feeds.split(",");
                               if (feedSplit.length != 2)
                               {
                                       _log.warning(StringUtil.concat("SlotMachine invalid config property -> Feed \"", feeds, "\""));
                               }
                               else
                               {
                                       try
                                       {
                                               feed.add(new int[]{Integer.parseInt(feedSplit[0]), Integer.parseInt(feedSplit[1])});
                                       }
                                       catch (NumberFormatException nfe)
                                       {
                                               if (!feeds.isEmpty())
                                               {
                                                       _log.warning(StringUtil.concat("SlotMachine invalid config property -> Feed \"", feeds, "\""));
                                               }
                                       }
                               }
                       }      
               }
               catch(Exception e)
               {
                       e.printStackTrace();
               }
       }      
      
       public static void main(String args[])
       {
               new SlotMachine(-1, qn, "custom");
       }
}