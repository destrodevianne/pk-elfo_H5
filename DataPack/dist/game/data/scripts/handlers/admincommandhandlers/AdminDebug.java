package handlers.admincommandhandlers;

import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class AdminDebug implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_debug"
	};
	
	@Override
	public final boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		String[] commandSplit = command.split(" ");
		if (ADMIN_COMMANDS[0].equalsIgnoreCase(commandSplit[0]))
		{
			L2Object target;
			if (commandSplit.length > 1)
			{
				target = L2World.getInstance().getPlayer(commandSplit[1].trim());
				if (target == null)
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
					return true;
				}
			}
			else
			{
				target = activeChar.getTarget();
			}
			
			if (target instanceof L2Character)
			{
				setDebug(activeChar, (L2Character) target);
			}
			else
			{
				setDebug(activeChar, activeChar);
			}
		}
		return true;
	}
	
	@Override
	public final String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private final void setDebug(L2PcInstance activeChar, L2Character target)
	{
		if (target.isDebug())
		{
			target.setDebug(null);
			activeChar.sendMessage("Parar a depuracao " + target.getName());
		}
		else
		{
			target.setDebug(activeChar);
			activeChar.sendMessage("Iniciar a depuracao " + target.getName());
		}
	}
}