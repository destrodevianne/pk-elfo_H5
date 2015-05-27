package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class Res implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"res" 
    };

	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {   
        if (command.equalsIgnoreCase("res") && activeChar.isVip())
        {
              if (!activeChar.isAlikeDead())
              {
                 activeChar.sendMessage("Voce nao pode ser ressucitado estando vivo.");
                 {
                	 return false;
                 }
              }
           if(activeChar.isInOlympiadMode())
           {
              activeChar.sendMessage("Voce nao pode ser ressucitado estando nas olympiad.");
              {
            	  return false;
              }
           }
           if(activeChar.getInventory().getItemByItemId(57) == null)
           {
              activeChar.sendMessage("Voce precisa de 10kk de Adena para usar o sistema de ressurreicao.");
              {
            	  return false;
              }
           }
              activeChar.sendMessage("You have been ressurected!");
              activeChar.getInventory().destroyItemByItemId("RessSystem", 57, 10000000, activeChar, activeChar.getTarget());
              activeChar.doRevive();
              activeChar.broadcastUserInfo();
              activeChar.sendMessage("10kk de Adena desapareceu! Obrigado!");
        }
       return true;
    }
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}