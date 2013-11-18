package handlers.voicedcommandhandlers;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class VIPTeleport implements IVoicedCommandHandler
{
        private static final String[] _voicedCommands =
        {
                "teleport"
        };
        
        public boolean useVoicedCommand(String command, L2PcInstance activeChar, String tlp)
        {
                if (command.equalsIgnoreCase("teleport")&& activeChar.isVip())
                {
                    
                    if (activeChar.isFestivalParticipant())
                    {
                    activeChar.sendMessage("Comando bloqueado voce esta em um Evento!");
                    return false;
                    }
                    else if(activeChar.isInJail())
                    {
                    activeChar.sendMessage("Comando bloqueado, voce esta na Jaula");
                    return false;
                    }
                    else if(activeChar.isDead())
                    {
                    activeChar.sendMessage("Comando bloqueado porque voce esta Morto.");
                    return false;
                    }
                    else if(activeChar.isInCombat())
                    {
                    activeChar.sendMessage("Comando bloqueado em pvp ou em modo de Combat.");
                    return false;
                    }
                    else if (activeChar.isInDuel())
                    {
                    activeChar.sendMessage("Comando bloqueado voce esta em um Duelo.");
                    return false;
                    }
                    else if (activeChar.isInOlympiadMode())
                    {
                    activeChar.sendMessage("Comando bloqueado em olympiada.");
                    return false;
                    }
                    else if (activeChar.getKarma() > 0)
                    {
                    activeChar.sendMessage("Ha.. ha.. ha, voce nao pode teleportar com Karma");
                    return false;
                    }
                    else if (activeChar.inObserverMode())
                    {
                    activeChar.sendMessage("Comando Bloqueado quando porque voce esta no modo de observador.");
                    return false;
                    }
                    else if (!activeChar.inObserverMode() && !activeChar.isInOlympiadMode() && !activeChar.isInDuel() && !activeChar.isInCombat() && !activeChar.isDead() && !activeChar.isInJail())
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
                        
                        activeChar.sendMessage("You have been teleported to " + Cords);
                }
                catch (NoSuchElementException nsee)
                {
                        activeChar.sendMessage("Wrong or no Coordinates given. Usage: /loc to display the coordinates.");
                        activeChar.sendMessage("Ex: .teleport <x> <y> <z>");
                }
        }
        
        public String[] getVoicedCommandList()
        {
                return _voicedCommands;
        }
}