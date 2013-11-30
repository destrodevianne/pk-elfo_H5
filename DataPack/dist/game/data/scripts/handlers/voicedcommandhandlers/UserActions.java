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
    	"pmon",
    	"buff_block",
    	"buff_unblock"
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
                                                       activeChar.sendMessage("Seu efeito personalizado agora esta desativado!");
                                                       ExShowScreenMessage message1 = new ExShowScreenMessage("Seu efeito personalizado agora esta desativado!",4000);
                                                       activeChar.sendPacket(message1);
                                                       L2PcInstance._isoneffect = false;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Seu efeito personalizado agora esta ativado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Seu efeito personalizado agora esta ativado", 4000);
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
                       if ((command.startsWith("tradeoff")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._istraderefusal == false)
                                       {
                                               activeChar.setTradeRefusal(true);
                                               activeChar.sendMessage("Recusa de Trade habilitado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Recusa de Trade agora esta ativado!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._istraderefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
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
                       if ((command.startsWith("tradeon")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._istraderefusal == true)
                                       {
                                               activeChar.setTradeRefusal(false);
                                               activeChar.sendMessage("Recusa de Trade desabilitado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Recusa de Trade agora esta desativado!", 4000);
                                               activeChar.sendPacket(message1);
                                       L2PcInstance._istraderefusal = false;
                               }
                                       else
                                       {
                                               activeChar.sendMessage("Voce nao esta com o comando ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce nao esta com o comando ativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("O comando foi desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("expoff")))
                       {
                               if (Config.ENABLE_EXP_REFUSAL)
                               {
                                       if (L2PcInstance._isexpsprefusal == false)
                                       {
                                               activeChar.sendMessage("Comando de desabilitar ganho de Exp/sp esta ativado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Comando de desabilitar ganho de Exp/sp esta ativado!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._isexpsprefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("O comando foi desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
                                       activeChar.sendPacket(message1);
                               }
                       }
                       if ((command.startsWith("expon")))
                       {
                               if (Config.ENABLE_TRADE_REFUSAL)
                               {
                                       if (L2PcInstance._isexpsprefusal == true)
                                       {
                                               activeChar.sendMessage("Comando de desabilitar ganho de Exp/sp esta desativado");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("Comando de desabilitar ganho de Exp/sp esta desativado!", 4000);
                                               activeChar.sendPacket(message1);
                                               L2PcInstance._isexpsprefusal = false;
                                       }
                                      else
                                       {
                                               activeChar.sendMessage("Voce ja esta com o comando desativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando desativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("O comando foi desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
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
                                               activeChar.sendMessage("O comando de desabilitar o Pm esta desativado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("O comando de desabilitar o Pm esta desativado!", 4000);
                                               activeChar.sendPacket(message1);
                                              L2PcInstance._ispmrefusal = false;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;          
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("O comando foi desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
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
                                               activeChar.sendMessage("O comando de desabilitar o Pm esta ativado");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("O comando de desabilitar o Pm esta ativado!", 4000);
                                              activeChar.sendPacket(message1);
                                               L2PcInstance._ispmrefusal = true;
                                       }
                                       else
                                       {
                                               activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                               ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
                                               activeChar.sendPacket(message1);       
                                               return false;  
                                       }
                               }
                               else
                               {
                                       activeChar.sendMessage("O comando foi desativado");
                                       ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
                                       activeChar.sendPacket(message1);
                                       return false;  
                                      
                               }
                       }
                      if ((command.startsWith("buff_block")))
                      {
                              if (Config.ENABLE_BUFF_REFUSAL)
                              {
                                      if (L2PcInstance._isbuffrefusal == true)
                                      {
                                              activeChar.setMessageRefusal(false);
                                              activeChar.sendMessage("O comando de desabilitar debuff esta desativado");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("O comando de desabilitar debuff esta desativado!", 4000);
                                              activeChar.sendPacket(message1);
                                              L2PcInstance._isbuffrefusal = false;
                                      }
                                      else
                                      {
                                              activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
                                              activeChar.sendPacket(message1);       
                                              return false;  
                                      }
                              }
                              else
                              {
                                      activeChar.sendMessage("O comando foi desativado");
                                      ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
                                      activeChar.sendPacket(message1);
                                      return false;  
                                     
                              }
                      }
                     if ((command.startsWith("buff_unblock")))
                      {
                              if (Config.ENABLE_BUFF_REFUSAL)
                              {
                                      if (L2PcInstance._isbuffrefusal == false)
                                      {
                                              activeChar.setMessageRefusal(true);
                                              activeChar.sendMessage("O comando de desabilitar debuff esta ativado");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("O comando de desabilitar debuff esta ativado!", 4000);
                                              activeChar.sendPacket(message1);
                                              L2PcInstance._isbuffrefusal = true;
                                      }
                                      else
                                      {
                                              activeChar.sendMessage("Voce ja esta com o comando ativado!");
                                              ExShowScreenMessage message1 = new ExShowScreenMessage("Voce ja esta com o comando ativado!", 4000);
                                              activeChar.sendPacket(message1);       
                                              return false;  
                                      }
                              }
                              else
                              {
                                      activeChar.sendMessage("O comando foi desativado");
                                      ExShowScreenMessage message1 = new ExShowScreenMessage("O comando foi desativado pelo ADM!", 4000);
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