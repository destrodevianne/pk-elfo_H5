package pk.elfo.gameserver.network.gameserverpackets;

import java.util.List;

import pk.elfo.util.network.BaseSendablePacket;

public class ReplyCharacters extends BaseSendablePacket
{
	public ReplyCharacters(String account, int chars, List<Long> timeToDel)
	{
		writeC(0x08);
		writeS(account);
		writeC(chars);
		writeC(timeToDel.size());
		for (long time : timeToDel)
		{
			writeQ(time);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}