package pk.elfo.gameserver.network.serverpackets;

import java.util.Collection;

import pk.elfo.Config;
import pk.elfo.gameserver.model.L2TradeList;
import pk.elfo.gameserver.model.L2TradeList.L2TradeItem;

public final class BuyList extends L2GameServerPacket
{
	private final int _listId;
	private final Collection<L2TradeItem> _list;
	private final long _money;
	private double _taxRate = 0;
	
	public BuyList(L2TradeList list, long currentMoney, double taxRate)
	{
		_listId = list.getListId();
		_list = list.getItems();
		_money = currentMoney;
		_taxRate = taxRate;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xB7);
		writeD(0x00);
		writeQ(_money); // current money
		writeD(_listId);
		
		writeH(_list.size());
		
		for (L2TradeItem item : _list)
		{
			if ((item.getCurrentCount() > 0) || !item.hasLimitedStock())
			{
				writeD(item.getItemId());
				writeD(item.getItemId());
				writeD(0);
				writeQ(item.getCurrentCount() < 0 ? 0 : item.getCurrentCount());
				writeH(item.getTemplate().getType2());
				writeH(item.getTemplate().getType1()); // Custom Type 1
				writeH(0x00); // isEquipped
				writeD(item.getTemplate().getBodyPart()); // Body Part
				writeH(0x00); // Enchant
				writeH(0x00); // Custom Type
				writeD(0x00); // Augment
				writeD(-1); // Mana
				writeD(-9999); // Time
				writeH(0x00); // Element Type
				writeH(0x00); // Element Power
				for (byte i = 0; i < 6; i++)
				{
					writeH(0x00);
				}
				// Enchant Effects
				writeH(0x00);
				writeH(0x00);
				writeH(0x00);
				
				if ((item.getItemId() >= 3960) && (item.getItemId() <= 4026))
				{
					writeQ((long) (item.getPrice() * Config.RATE_SIEGE_GUARDS_PRICE * (1 + _taxRate)));
				}
				else
				{
					writeQ((long) (item.getPrice() * (1 + _taxRate)));
				}
			}
		}
	}
}