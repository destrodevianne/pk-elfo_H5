package pk.elfo.gameserver.network.gameserverpackets;

import pk.elfo.util.network.BaseSendablePacket;

public class PlayerLogout extends BaseSendablePacket
{
	public PlayerLogout(String player)
	{
		writeC(0x03);
		writeS(player);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}