package handlers.bypasshandlers;

import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2ClanHallManagerInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2WarehouseInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.gameserver.network.serverpackets.SortedWareHouseWithdrawalList;
import pk.elfo.gameserver.network.serverpackets.WareHouseDepositList;
import pk.elfo.gameserver.network.serverpackets.WareHouseWithdrawalList;
import pk.elfo.gameserver.network.serverpackets.SortedWareHouseWithdrawalList.WarehouseListType;

/**
 * Projeto PkElfo
 */

public class ClanWarehouse implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"withdrawc",
		"withdrawsortedc",
		"depositc"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2WarehouseInstance) && !(target instanceof L2ClanHallManagerInstance))
		{
			return false;
		}
		
		if (activeChar.isEnchanting())
		{
			return false;
		}
		
		if (activeChar.getClan() == null)
		{
			activeChar.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
			return false;
		}
		
		if (activeChar.getClan().getLevel() == 0)
		{
			activeChar.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
			return false;
		}
		
		try
		{
			activeChar.setIsUsingAIOItemWareHouse(false);
			if (command.toLowerCase().startsWith(COMMANDS[0])) // WithdrawC
			{
				if (Config.L2JMOD_ENABLE_WAREHOUSESORTING_CLAN)
				{
					NpcHtmlMessage msg = new NpcHtmlMessage(((L2Npc) target).getObjectId());
					msg.setFile(activeChar.getHtmlPrefix(), "data/html/mods/WhSortedC.htm");
					msg.replace("%objectId%", String.valueOf(((L2Npc) target).getObjectId()));
					activeChar.sendPacket(msg);
				}
				else
				{
					showWithdrawWindow(activeChar, null, (byte) 0);
				}
				return true;
			}
			else if (command.toLowerCase().startsWith(COMMANDS[1])) // WithdrawSortedC
			{
				final String param[] = command.split(" ");
				
				if (param.length > 2)
				{
					showWithdrawWindow(activeChar, WarehouseListType.valueOf(param[1]), SortedWareHouseWithdrawalList.getOrder(param[2]));
				}
				else if (param.length > 1)
				{
					showWithdrawWindow(activeChar, WarehouseListType.valueOf(param[1]), SortedWareHouseWithdrawalList.A2Z);
				}
				else
				{
					showWithdrawWindow(activeChar, WarehouseListType.ALL, SortedWareHouseWithdrawalList.A2Z);
				}
				return true;
			}
			else if (command.toLowerCase().startsWith(COMMANDS[2])) // DepositC
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
				activeChar.setActiveWarehouse(activeChar.getClan().getWarehouse());
				activeChar.tempInventoryDisable();
				
				if (Config.DEBUG)
				{
					_log.fine("Source: L2WarehouseInstance.java; Player: " + activeChar.getName() + "; Command: showDepositWindowClan; Message: Showing items to deposit.");
				}
				
				activeChar.sendPacket(new WareHouseDepositList(activeChar, WareHouseDepositList.CLAN));
				return true;
			}
			
			return false;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	private static final void showWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		if ((player.getClanPrivileges() & L2Clan.CP_CL_VIEW_WAREHOUSE) != L2Clan.CP_CL_VIEW_WAREHOUSE)
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
			return;
		}
		
		player.setActiveWarehouse(player.getClan().getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
			return;
		}
		
		for (L2ItemInstance i : player.getActiveWarehouse().getItems())
		{
			if (i.isTimeLimitedItem() && (i.getRemainingTime() <= 0))
			{
				player.getActiveWarehouse().destroyItem("L2ItemInstance", i, player, null);
			}
		}
		
		if (itemtype != null)
		{
			player.sendPacket(new SortedWareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN, itemtype, sortorder));
		}
		else
		{
			player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN));
		}
		
		if (Config.DEBUG)
		{
			_log.fine("Source: L2WarehouseInstance.java; Player: " + player.getName() + "; Command: showRetrieveWindowClan; Message: Showing stored items.");
		}
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}