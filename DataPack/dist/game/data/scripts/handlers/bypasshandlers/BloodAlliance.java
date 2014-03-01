package handlers.bypasshandlers;

import java.util.StringTokenizer;
import java.util.logging.Level;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2CastleWarehouseInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Castle Warehouse - Blood Alliance.
 * PkElfo
 */
public class BloodAlliance implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"HonoraryItem",
		"Receive",
		"Exchange"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2CastleWarehouseInstance))
		{
			return false;
		}
		
		final L2CastleWarehouseInstance npc = (L2CastleWarehouseInstance) target;
		try
		{
			NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
			StringTokenizer st = new StringTokenizer(command, " ");
			String actualCommand = st.nextToken(); // Get actual command
			
			if (actualCommand.equalsIgnoreCase(COMMANDS[0])) // "HonoraryItem"
			{
				if (npc.isMyLord(activeChar))
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-4.htm");
					html.replace("%blood%", Integer.toString(activeChar.getClan().getBloodAllianceCount()));
				}
				else
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-3.htm");
				}
			}
			else if (actualCommand.equalsIgnoreCase(COMMANDS[1])) // "Receive"
			{
				if (!npc.isMyLord(activeChar))
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-5.htm");
				}
				else
				{
					activeChar.addItem("BloodAlliance", 9911, activeChar.getClan().getBloodAllianceCount(), activeChar, true);
					activeChar.getClan().resetBloodAllianceCount();
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-6.htm");
				}
			}
			else if (actualCommand.equalsIgnoreCase(COMMANDS[2])) // "Exchange"
			{
				if (activeChar.getInventory().getInventoryItemCount(9911, -1) > 0)
				{
					activeChar.destroyItemByItemId("BloodAllianceExchange", 9911, 1, activeChar, true);
					activeChar.addItem("BloodAllianceExchange", 9910, 30, activeChar, true);
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-7.htm");
				}
				else
				{
					html.setFile(activeChar.getHtmlPrefix(), "data/html/castlewarehouse/castlewarehouse-8.htm");
				}
				
			}
			html.replace("%objectId%", String.valueOf(npc.getObjectId()));
			activeChar.sendPacket(html);
			return true;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}