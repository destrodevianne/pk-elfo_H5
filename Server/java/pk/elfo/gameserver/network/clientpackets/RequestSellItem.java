package pk.elfo.gameserver.network.clientpackets;

import static pk.elfo.gameserver.model.actor.L2Npc.INTERACTION_DISTANCE;
import static pk.elfo.gameserver.model.itemcontainer.PcInventory.MAX_ADENA;

import java.util.ArrayList;
import java.util.List;

import pk.elfo.Config;
import pk.elfo.gameserver.TradeController;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2TradeList;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2MerchantInstance;
import pk.elfo.gameserver.model.actor.instance.L2MerchantSummonInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.ExBuySellList;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.util.Util;

/**
 * PkElfo
 */

public final class RequestSellItem extends L2GameClientPacket
{
	private static final String _C__37_REQUESTSELLITEM = "[C] 37 RequestSellItem";
	
	private static final int BATCH_LENGTH = 16;
	
	private int _listId;
	private List<ItemHolder> _items = null;
	
	@Override
	protected void readImpl()
	{
		_listId = readD();
		int size = readD();
		if ((size <= 0) || (size > Config.MAX_ITEM_IN_PACKET) || ((size * BATCH_LENGTH) != _buf.remaining()))
		{
			return;
		}
		
		_items = new ArrayList<>(size);
		for (int i = 0; i < size; i++)
		{
			int objectId = readD();
			int itemId = readD();
			long count = readQ();
			if ((objectId < 1) || (itemId < 1) || (count < 1))
			{
				_items = null;
				return;
			}
			_items.add(new ItemHolder(itemId, objectId, count));
		}
	}
	
	@Override
	protected void runImpl()
	{
		processSell();
	}
	
	protected void processSell()
	{
		L2PcInstance player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		if (!getClient().getFloodProtectors().getTransaction().tryPerformAction("buy"))
		{
			player.sendMessage("You are buying too fast.");
			return;
		}
		
		if (_items == null)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// Alt game - Karma punishment
		if (!Config.ALT_GAME_KARMA_PLAYER_CAN_SHOP && (player.getKarma() > 0))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		L2Object target = player.getTarget();
		L2Character merchant = null;
		if (!player.isGM())
		{
			if ((target == null) || (!player.isInsideRadius(target, INTERACTION_DISTANCE, true, false)) // Distance is too far)
				|| (player.getInstanceId() != target.getInstanceId()))
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			if ((target instanceof L2MerchantInstance) || (target instanceof L2MerchantSummonInstance))
			{
				merchant = (L2Character) target;
			}
			else
			{
				sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
		}
		
		double taxRate = 0;
		
		L2TradeList list = null;
		if (!player.isUsingAIOItemMultisell() && merchant != null)
		{
			List<L2TradeList> lists;
			if (merchant instanceof L2MerchantInstance)
			{
				lists = TradeController.getInstance().getBuyListByNpcId(((L2MerchantInstance) merchant).getNpcId());
				taxRate = ((L2MerchantInstance) merchant).getMpc().getTotalTaxRate();
			}
			else
			{
				lists = TradeController.getInstance().getBuyListByNpcId(((L2MerchantSummonInstance) merchant).getNpcId());
				taxRate = 50;
			}
			
			if (!player.isGM())
			{
				if (lists == null)
				{
					Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId, Config.DEFAULT_PUNISH);
					return;
				}
				for (L2TradeList tradeList : lists)
				{
					if (tradeList.getListId() == _listId)
					{
						list = tradeList;
					}
				}
			}
			else
			{
				list = TradeController.getInstance().getBuyList(_listId);
			}
		}
		else
		{
			list = TradeController.getInstance().getBuyList(_listId);
		}
		
		if (list == null)
		{
			Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " sent a false BuyList list_id " + _listId, Config.DEFAULT_PUNISH);
			return;
		}
		
		long totalPrice = 0;
		// Proceed the sell
		for (ItemHolder i : _items)
		{
			L2ItemInstance item = player.checkItemManipulation(i.getObjectId(), i.getCount(), "sell");
			if ((item == null) || (!item.isSellable()))
			{
				continue;
			}
			
			long price = item.getReferencePrice() / 2;
			totalPrice += price * i.getCount();
			if (((MAX_ADENA / i.getCount()) < price) || (totalPrice > MAX_ADENA))
			{
				Util.handleIllegalPlayerAction(player, "Warning!! Character " + player.getName() + " of account " + player.getAccountName() + " tried to purchase over " + MAX_ADENA + " adena worth of goods.", Config.DEFAULT_PUNISH);
				return;
			}
			
			if (Config.ALLOW_REFUND)
			{
				item = player.getInventory().transferItem("Sell", i.getObjectId(), i.getCount(), player.getRefund(), player, merchant);
			}
			else
			{
				item = player.getInventory().destroyItem("Sell", i.getObjectId(), i.getCount(), player, merchant);
			}
		}
		player.addAdena("Sell", totalPrice, merchant, false);
		
		// Update current load as well
		StatusUpdate su = new StatusUpdate(player);
		su.addAttribute(StatusUpdate.CUR_LOAD, player.getCurrentLoad());
		player.sendPacket(su);
		player.sendPacket(new ExBuySellList(player, taxRate, true));
	}
	
	@Override
	public String getType()
	{
		return _C__37_REQUESTSELLITEM;
	}
}
