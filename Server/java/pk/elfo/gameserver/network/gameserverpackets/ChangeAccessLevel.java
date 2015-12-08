package pk.elfo.gameserver.network.gameserverpackets;

import pk.elfo.util.network.BaseSendablePacket;

public class ChangeAccessLevel extends BaseSendablePacket
{
	public ChangeAccessLevel(String player, int access)
	{
		writeC(0x04);
		writeD(access);
		writeS(player);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}