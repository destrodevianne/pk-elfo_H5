package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.GameTimeController;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.datatables.MapRegionTable;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.handler.voicedcommandhandlers.Wedding.EscapeFinalizer;
import king.server.gameserver.instancemanager.CastleManager;
import king.server.gameserver.instancemanager.ClanHallManager;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.serverpackets.SystemMessage;

/**
 *
 *
 */
public class Cl implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS = 
    { 
    	"cl" 
    };

    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {   
       
        if (command.equalsIgnoreCase("cl"))
        {

           if (activeChar.getClan() == null)
              {
                 return false;
              }
       
            L2PcInstance leader;
            leader = (L2PcInstance)L2World.getInstance().findObject(activeChar.getClan().getLeaderId());

            if(leader == null)
            {
                activeChar.sendMessage("Your partner is not online.");
                return false;
            }
            else if(leader.isInJail())
            {
                activeChar.sendMessage("Your leader is in Jail.");
                return false;
            }
            else if(leader.isInOlympiadMode())
            {
                activeChar.sendMessage("Your leader is in the Olympiad now.");
                return false;
            }
            else if(leader.atEvent)
            {
                activeChar.sendMessage("Your leader is in an event.");
                return false;
            }
            else  if (leader.isInDuel())
            {
                activeChar.sendMessage("Your leader is in a duel.");
                return false;
            }
            else if (leader.isFestivalParticipant())
            {
                activeChar.sendMessage("Your leader is in a festival.");
                return false;
            }
            else if (leader.isInParty() && leader.getParty().isInDimensionalRift())
            {
                activeChar.sendMessage("Your leader is in dimensional rift.");
                return false;
            }
            else if (leader.inObserverMode())
            {
               activeChar.sendMessage("Your leader is in the observation.");
            }
            else if(leader.getClan() != null
                  && CastleManager.getInstance().getCastleByOwner(leader.getClan()) != null
                  && CastleManager.getInstance().getCastleByOwner(leader.getClan()).getSiege().getIsInProgress())
            {
               activeChar.sendMessage("Your leader is in siege, you can't go to your leader.");
               return false;
            }

            else if(activeChar.isInJail())
            {
                activeChar.sendMessage("You are in Jail!");
                return false;
            }
            else if(activeChar.isInOlympiadMode())
            {
                activeChar.sendMessage("You are in the Olympiad now.");
                return false;
            }
                    else if(activeChar._inEventVIP)
                     {
                          activeChar.sendPacket(SystemMessage.sendString("Your leader is in a VIP event."));
                        return false;
                     }
            else if(activeChar.atEvent)
            {
                activeChar.sendMessage("You are in an event.");
                return false;
            }
            else  if (activeChar.isInDuel())
            {
                activeChar.sendMessage("You are in a duel!");
                return false;
            }
            else if (activeChar.inObserverMode())
            {
               activeChar.sendMessage("You are in the observation.");
            }
            else if(activeChar.getClan() != null
                  && CastleManager.getInstance().getCastleByOwner(activeChar.getClan()) != null
                  && CastleManager.getInstance().getCastleByOwner(activeChar.getClan()).getSiege().getIsInProgress())
            {
               activeChar.sendMessage("You are in siege, you can't go to your leader.");
               return false;
            }
            else if (activeChar.isFestivalParticipant())
            {
                activeChar.sendMessage("You are in a festival.");
                return false;
            }
            else if (activeChar.isInParty() && activeChar.getParty().isInDimensionalRift())
            {
                activeChar.sendMessage("You are in the dimensional rift.");
                return false;
            }
            else if (activeChar == leader)
            {
                activeChar.sendMessage("You cannot teleport to yourself.");
                return false;
            }
           if(activeChar.getInventory().getItemByItemId(3470) == null)
           {
              activeChar.sendMessage("You need one or more Gold Bars to use the cl-teleport system.");
             return false;
           }
               int leaderx;
               int leadery;
               int leaderz;
               
               leaderx = leader.getX();
               leadery = leader.getY();
               leaderz = leader.getZ();
               
               activeChar.teleToLocation(leaderx, leadery, leaderz);
     activeChar.sendMessage("You have been teleported to your leader!");
    activeChar.getInventory().destroyItemByItemId("RessSystem", 3470, 1, activeChar, activeChar.getTarget());
    activeChar.sendMessage("One GoldBar has dissapeared! Thank you!");
        }
        return true;
    }
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }

}