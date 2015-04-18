package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2Henna;

/**
 * @author Zoey76
 */
public class HennaRemoveList extends L2GameServerPacket
{
	private final L2PcInstance _player;
	
	public HennaRemoveList(L2PcInstance player)
	{
		_player = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xE6);
		writeQ(_player.getAdena());
		writeD(0x00);
		writeD(3 - _player.getHennaEmptySlots());
		
		for (L2Henna henna : _player.getHennaList())
		{
			if (henna != null)
			{
				writeD(henna.getDyeId());
				writeD(henna.getDyeItemId());
				writeD(henna.getCancelCount());
				writeD(0x00);
				writeD(henna.getCancelFee());
				writeD(0x00);
				writeD(0x01);
			}
		}
	}
}
