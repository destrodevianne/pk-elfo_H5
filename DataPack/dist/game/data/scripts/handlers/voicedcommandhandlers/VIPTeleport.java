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
                        if (activeChar == null)
                        {
                                return false;
                        }
                        else if(activeChar.atEvent)
                        {
                                activeChar.sendMessage("You are an Event.");
                                return false;
                        }
                        else if(activeChar.isInDuel())
                        {
                                activeChar.sendMessage("You are on Duel.");
                                return false;
                        }
                        else if(activeChar.isInOlympiadMode())
                        {
                                activeChar.sendMessage("You are in Olympiad.");
                                return false;
                        }
                        else if(activeChar.isInCombat())
                        {
                                activeChar.sendMessage("You can't teleport in Combat Mod.");
                                return false;
                        }
                        else if (activeChar.isFestivalParticipant())
                        {
                                activeChar.sendMessage("You are in a festival.");
                                return false;
                        }
                        else if (activeChar.isInJail())
                        {
                                activeChar.sendMessage("You are in Jail.");
                                return false;
                        }
                        else if (activeChar.inObserverMode())
                        {
                                activeChar.sendMessage("You are in Observ Mode.");
                                return false;
                        }
                        else if (activeChar.isDead())
                        {
                                activeChar.sendMessage("You Dead. Can't Teleport.");
                                return false;
                        }
                        else if (activeChar.isFakeDeath())
                        {
                                activeChar.sendMessage("You are Dead? week up :D");
                                return false;
                        }
                        else if (activeChar.getKarma() > 0)
                        {
                                activeChar.sendMessage("You can't use teleport command when you have karma.");
                                return false;
                        }
                        if(tlp != null)
                        {
                                teleportTo(activeChar, tlp);
                        }
                        else
                        {
                                activeChar.sendMessage("Wrong or no Coordinates given. Usage: /loc to display the coordinates.");
                                activeChar.sendMessage("Ex: .teleport <x> <y> <z>");
                                return false;
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