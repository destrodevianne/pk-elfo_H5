package handlers.admincommandhandlers;

import king.server.gameserver.handler.IAdminCommandHandler;
import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * PkElfo
 */
public class AdminDeport implements IAdminCommandHandler
{
   private static String[] _adminCommands =
   {
         "admin_deport"
   };

   @Override
   public boolean useAdminCommand(String command, L2PcInstance activeChar)
   {

      L2Object target = activeChar.getTarget();

      if(activeChar.getTarget() instanceof L2PcInstance)
      {
         if(command.startsWith("admin_deport"))
         {
            ((L2PcInstance) activeChar.getTarget()).teleToLocation(82698, 148638, -3473);
         }
      }
      return false;

   }

   @Override
   public String[] getAdminCommandList()
   {
      return _adminCommands;
   }
}