package handlers.bypasshandlers;

import java.util.StringTokenizer;
import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.TradeController;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.L2TradeList;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.ShopPreviewList;

/**
 * Projeto PkElfo
 */

public class Wear implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"Wear"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (!Config.ALLOW_WEAR)
		{
			return false;
		}
		
		try
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			if (st.countTokens() < 1)
			{
				return false;
			}
			
			showWearWindow(activeChar, Integer.parseInt(st.nextToken()));
			return true;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	private static final void showWearWindow(L2PcInstance player, int val)
	{
		player.tempInventoryDisable();
		
		if (Config.DEBUG)
		{
			_log.fine("Showing wearlist");
		}
		
		L2TradeList list = TradeController.getInstance().getBuyList(val);
		
		if (list != null)
		{
			ShopPreviewList bl = new ShopPreviewList(list, player.getAdena(), player.getExpertiseLevel());
			player.sendPacket(bl);
		}
		else
		{
			_log.warning("no buylist with id:" + val);
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}