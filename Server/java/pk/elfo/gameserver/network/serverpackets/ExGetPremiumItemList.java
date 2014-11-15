package pk.elfo.gameserver.network.serverpackets;

import java.util.Map;
import java.util.Map.Entry;

import pk.elfo.gameserver.model.L2PremiumItem;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExGetPremiumItemList extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	
	private final Map<Integer, L2PremiumItem> _map;
	
	public ExGetPremiumItemList(L2PcInstance activeChar)
	{
		_activeChar = activeChar;
		_map = _activeChar.getPremiumItemList();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x86);
		if (!_map.isEmpty())
		{
			writeD(_map.size());
			for (Entry<Integer, L2PremiumItem> entry : _map.entrySet())
			{
				L2PremiumItem item = entry.getValue();
				writeD(entry.getKey());
				writeD(_activeChar.getObjectId());
				writeD(item.getItemId());
				writeQ(item.getCount());
				writeD(0x00); // ?
				writeS(item.getSender());
			}
		}
		else
		{
			writeD(0x00);
		}
	}
}