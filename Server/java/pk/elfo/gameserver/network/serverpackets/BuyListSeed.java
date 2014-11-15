package pk.elfo.gameserver.network.serverpackets;

import java.util.List;

import pk.elfo.gameserver.instancemanager.CastleManorManager.SeedProduction;
import javolution.util.FastList;

public final class BuyListSeed extends L2GameServerPacket
{
	private final int _manorId;
	private List<Seed> _list = null;
	private final long _money;
	
	public BuyListSeed(long currentMoney, int castleId, List<SeedProduction> seeds)
	{
		_money = currentMoney;
		_manorId = castleId;
		
		if ((seeds != null) && (seeds.size() > 0))
		{
			_list = new FastList<>();
			for (SeedProduction s : seeds)
			{
				if ((s.getCanProduce() > 0) && (s.getPrice() > 0))
				{
					_list.add(new Seed(s.getId(), s.getCanProduce(), s.getPrice()));
				}
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe9);
		
		writeQ(_money); // current money
		writeD(_manorId); // manor id
		
		if ((_list != null) && (_list.size() > 0))
		{
			writeH(_list.size()); // list length
			for (Seed s : _list)
			{
				writeD(s._itemId);
				writeD(s._itemId);
				writeD(0x00);
				writeQ(s._count); // item count
				writeH(0x05); // Custom Type 2
				writeH(0x00); // Custom Type 1
				writeH(0x00); // Equipped
				writeD(0x00); // Body Part
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
				writeQ(s._price); // price
			}
			_list.clear();
		}
		else
		{
			writeH(0x00);
		}
	}
	
	private static class Seed
	{
		public final int _itemId;
		public final long _count;
		public final long _price;
		
		public Seed(int itemId, long count, long price)
		{
			_itemId = itemId;
			_count = count;
			_price = price;
		}
	}
}