package pk.elfo.gameserver.network.gameserverpackets;

import pk.elfo.util.network.BaseSendablePacket;

public class TempBan extends BaseSendablePacket
{
	public TempBan(String accountName, String ip, long time, String reason)
	{
		writeC(0x0A);
		writeS(accountName);
		writeS(ip);
		writeQ(System.currentTimeMillis() + (time * 60000));
		if (reason != null)
		{
			writeC(0x01);
			writeS(reason);
		}
		else
		{
			writeC(0x00);
		}
	}
	
	public TempBan(String accountName, String ip, long time)
	{
		this(accountName, ip, time, null);
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}