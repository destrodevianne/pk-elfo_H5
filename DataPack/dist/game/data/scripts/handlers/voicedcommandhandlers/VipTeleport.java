package handlers.voicedcommandhandlers;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class VipTeleport implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = 
    	{ 
    	"teleport" 
    	};
    
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String tlp)
    {
        if (command.equalsIgnoreCase("teleport") && activeChar.isVip())
        {
            if (activeChar == null)
            {
                return false;
            }
            else if(activeChar.isInDuel())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando em um Duelo.");
                return false;
            }
            else if(activeChar.isInOlympiadMode())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando nas Olimpiadas.");
                return false;
            }
            else if(activeChar.isInCombat())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando em Modo de Combate.");
                return false;
            }
            else if (activeChar.isFestivalParticipant())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando em um Festival.");
                return false;
            }
            else if (activeChar.isInJail())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando na Prisao.");
                return false;
            }
            else if (activeChar.inObserverMode())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando em Modo de Observacao.");
                return false;
            }
            else if (activeChar.isDead())
            {
                activeChar.sendMessage("Voce nao pode teleportar estando morto.");
                return false;
            }
            else if (activeChar.isFakeDeath())
            {
                activeChar.sendMessage("Voce esta morto? :D");
                return false;
            }
            else if (activeChar.getKarma() > 0)
            {
                activeChar.sendMessage("Voce nao pode teleportar estando com PK.");
                return false;
            }
            else if (!activeChar.isVip())
            {
                activeChar.sendMessage("Voce precisa ser VIP para utilizar este comando.");
                return false;
            }           
            else if (!Config.ENABLE_VIP_TELEPORT && !activeChar.isVip()) 
            {
            	if(tlp != null)
            	{
            		teleportTo(activeChar, tlp);
            	}
            	else
            	{
            		activeChar.sendMessage("Para teleportar utilize o comando desta maneira:");
            		activeChar.sendMessage("Ex: .teleport <x> <y> <z>");
            		return false;
            	}
            }
        }
        return true;
    }
    
    private void teleportTo(L2PcInstance activeChar, String Cords)
    {
        try
        {
            StringTokenizer st = new StringTokenizer(Cords);
            String x1 = st.nextToken();
            int x = Integer.parseInt(x1);
            String y1 = st.nextToken();
            int y = Integer.parseInt(y1);
            String z1 = st.nextToken();
            int z = Integer.parseInt(z1);
            
            activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
            activeChar.teleToLocation(x, y, z, false);
            
            activeChar.sendMessage("Voce foi teletransportado para " + Cords);
        }
        catch (NoSuchElementException nsee)
        {
            activeChar.sendMessage("Para teleportar utilize o comando desta maneira:");
            activeChar.sendMessage("Ex: .teleport <x> <y> <z>");
        }
    }

	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}