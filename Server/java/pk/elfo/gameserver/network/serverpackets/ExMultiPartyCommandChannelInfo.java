package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.L2CommandChannel;
import pk.elfo.gameserver.model.L2Party;

public class ExMultiPartyCommandChannelInfo extends L2GameServerPacket
{
	private final L2CommandChannel _channel;
	
	public ExMultiPartyCommandChannelInfo(L2CommandChannel channel)
	{
		_channel = channel;
	}
	
	@Override
	protected void writeImpl()
	{
		if (_channel == null)
		{
			return;
		}
		
		writeC(0xFE);
		writeH(0x31);
		
		writeS(_channel.getLeader().getName());
		writeD(0x00); // Channel loot 0 or 1
		writeD(_channel.getMemberCount());
		
		writeD(_channel.getPartys().size());
		for (L2Party p : _channel.getPartys())
		{
			writeS(p.getLeader().getName());
			writeD(p.getLeaderObjectId());
			writeD(p.getMemberCount());
		}
	}
}