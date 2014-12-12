package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import pk.elfo.gameserver.datatables.PremiumTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */

public class AdminPremium implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_add_premium",
		"admin_remove_premium"
	};

	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String cmd = st.nextToken();
		L2PcInstance player = null;
		L2Object targetObj = activeChar.getTarget();

		if (targetObj instanceof L2PcInstance)
		{
			player = targetObj.getActingPlayer();
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return false;
		}

		if (cmd.startsWith("admin_add_premium"))
		{
			if (st.hasMoreTokens())
			{
				String val;
				val = st.nextToken();

				if (val.equalsIgnoreCase("unlim"))
				{
					if (PremiumTable.setUnlimitedPremium(player))
					{
						activeChar.sendMessage("Unlimited Premium Service for " + player.getName() + " was activated");
					}
				}
				else
				{
					long millis = Util.toMillis(val);
					if (millis > 0)
					{
						if (PremiumTable.addTime(player, millis))
						{
							activeChar.sendMessage("Premium Service for " + player.getName() + " was activated");
						}
					}
					else
					{
						activeChar.sendMessage("Error in parameters");
					}
				}
			}
			else
			{
				PremiumTable.setTemporaryPremium(player);
				activeChar.sendMessage("Premium Service for " + player.getName() + " was activated. It stays until player's logout");
			}
		}
		else if (cmd.startsWith("admin_remove_premium"))
		{
			PremiumTable.removeService(player);
			activeChar.sendMessage("Premium Service for " + player.getName() + " was cancelled");
		}

		return true;
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}