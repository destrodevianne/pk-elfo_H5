package pk.elfo.gameserver.network.communityserver.writepackets;

import org.netcon.BaseWritePacket;

public final class BlowFishKey extends BaseWritePacket
{
	public BlowFishKey(final byte[] tempKey)
	{
		super.writeC(0x00);
		super.writeC(0x00);
		super.writeD(tempKey.length);
		super.writeB(tempKey);
	}
}