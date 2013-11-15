/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.effects.AbnormalEffect;
import king.server.gameserver.network.serverpackets.ExShowScreenMessage;

/**
 * @author NeverMore
 *
 */

public class UserActions implements IVoicedCommandHandler
{
    public static final String[] VOICED_COMMANDS = 
    	{ 
    	"effecton" , 
    	"effectoff", 
    	"tradeoff", 
    	"tradeon" ,
    	"expoff" , 
    	"expon", 
    	"pmoff", 
    	"pmon" 
    	};
      
       @Override
       public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
       {
                       if ((command.startsWith("effecton")))
                       {
                               if (Config.ENABLE_SPECIAL_EFFECT)
                               {
                                       if (L2PcInstance._isoneffect == false)
                                       {
                                                       activeChar.startAbnormalEffect(AbnormalEffect.VITALITY);
                                                       activeChar.sendMessage("Seu efeito personalizado esta habilitado!");
                                                       ExShowScreenMessage message1 = new ExShowScreenMessage("Seu efeito personalizado agora esta ativado!", 4000);
                                                       activeChar.sendPacket(message1);
                                                       L2PcInstance._isoneffect = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Seu efeito ja esta ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Seu efeito ja esta ativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("Este comando esta desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("Este comando foi desativado pelo ADM!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("effectoff")))
                       {
                               if (Config.ENABLE_SPECIAL_EFFECT)
                               {
                                       if (L2PcInstance._isoneffect == true)
                                       {
                                                       activeChar.stopAbnormalEffect(AbnormalEffect.VITALITY);
                                                       activeChar.sendMessage("Your custom effect is now disabled!");
                                                       ExShowScreenMessage message1 = new ExShowScreenMessage("Your custom effect is now disabled!",4000);
                                                       activeChar.sendPacket(message1);
                                                       L2PcInstance._isoneffect = false;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("You dont have effect enabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You dont have effect enabled", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("tradeoff")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._istraderefusal == false)
                                       {
                                               activeChar.setTradeRefusal(true);
                                               activeChar.sendMessage("Trade refusal enabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Trade refusal mode is now enabled!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._istraderefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("You are already in trade refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are already in trade refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("tradeon")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._istraderefusal == true)
                                       {
                                               activeChar.setTradeRefusal(false);
                                               activeChar.sendMessage("Trade refusal disabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Trade refusal mode is now disabled!", 4000);
                                               activeChar.sendPacket(message1);
                                       L2PcInstance._istraderefusal = false;
                               }
                                       else
                                       {
                                               activeChar.sendMessage("You are not in trade refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are not in trade refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("expoff")))
                       {
                               if (Config.ENABLE_EXP_REFUSAL)
                               {
                                       if (L2PcInstance._isexpsprefusal == false)
                                       {
                                               activeChar.sendMessage("Exp/sp refusal enabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Exp/sp refusal mode is now enabled!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._isexpsprefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("You are already in exp/sp refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are already in exp/sp refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("expon")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._isexpsprefusal == true)
                                       {
                                               activeChar.sendMessage("Exp/sp refusal disabled");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("Exp/sp refusal mode is now disabled!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._isexpsprefusal = false;
                                       }
                                      else
                                       {
                                               activeChar.sendMessage("You are not in exp/sp refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are not in exp/sp refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }      
                       if ((command.startsWith("pmon")))
                       {
                               if ( Config.ENABLE_PM_REFUSAL)
                               {
                                       if (L2PcInstance._ispmrefusal == true)
                                       {
                                              activeChar.setMessageRefusal(false);
                                               activeChar.sendMessage("Pm refusal mode disabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Pm refusal mode is now disabled!", 4000);
                                               activeChar.sendPacket(message1);
                                              L2PcInstance._ispmrefusal = false;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("You are not in pm refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are not in pm refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                      activeChar.sendPacket(message1);
                                       return false;                                          
                               }
                              
                       }
                      if ((command.startsWith("pmoff")))
                       {
                               if (Config.ENABLE_PM_REFUSAL)
                               {
                                       if (L2PcInstance._ispmrefusal == false)
                                       {
                                               activeChar.setMessageRefusal(true);
                                               activeChar.sendMessage("Pm refusal mode enabled");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Pm refusal mode is now enabled!", 4000);
                                              activeChar.sendPacket(message1);
                                               L2PcInstance._ispmrefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("You are already in pm refusal mode!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("You are already in pm refusal mode!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;  
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("This command is disabled");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("This command is disabled by admin!", 4000);
                                       activeChar.sendPacket(message1);
                                       return false;  
                                      
                               }
                       }
       return false;          
       }
      
       @Override
       public String[] getVoicedCommandList()
       {
               return VOICED_COMMANDS;
       }
}