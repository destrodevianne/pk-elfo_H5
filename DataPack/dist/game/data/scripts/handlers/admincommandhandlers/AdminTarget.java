package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands: - target name = sets player with respective name as target
 * @version $Revision: 1.2.4.3 $ $Date: 2005/04/11 10:05:56 $
 */
public class AdminTarget implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_target"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_target"))
		{
			handleTarget(command, activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void handleTarget(String command, L2PcInstance activeChar)
	{
		try
		{
			String targetName = command.substring(13);
			L2PcInstance player = L2World.getInstance().getPlayer(targetName);
			if (player != null)
			{
				player.onAction(activeChar);
			}
			else
			{
				activeChar.sendMessage("Player " + targetName + " not found");
			}
		}
		catch (IndexOutOfBoundsException e)
		{
			activeChar.sendMessage("Please specify correct name.");
		}
	}
}
