package handlers.admincommandhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.TradeController;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2TradeList;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.BuyList;
import pk.elfo.gameserver.network.serverpackets.ExBuySellList;
 
/**
 * Projeto PkElfo
 */

public class AdminShop implements IAdminCommandHandler
{
	private static final Logger _log = Logger.getLogger(AdminShop.class.getName());
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_buy",
		"admin_gmshop"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.startsWith("admin_buy"))
		{
			try
			{
				handleBuyRequest(activeChar, command.substring(10));
			}
			catch (IndexOutOfBoundsException e)
			{
				activeChar.sendMessage("Please specify buylist.");
			}
		}
		else if (command.equals("admin_gmshop"))
		{
			AdminHelpPage.showHelpPage(activeChar, "gmshops.htm");
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void handleBuyRequest(L2PcInstance activeChar, String command)
	{
		int val = -1;
		try
		{
			val = Integer.parseInt(command);
		}
		catch (Exception e)
		{
			_log.warning("admin buylist failed:" + command);
		}
		
		L2TradeList list = TradeController.getInstance().getBuyList(val);
		
		if (list != null)
		{
			activeChar.sendPacket(new BuyList(list, activeChar.getAdena(), 0));
			activeChar.sendPacket(new ExBuySellList(activeChar, 0, false));
		}
		else
		{
			_log.warning("no buylist with id:" + val);
		}
		activeChar.sendPacket(ActionFailed.STATIC_PACKET);
	}
}
