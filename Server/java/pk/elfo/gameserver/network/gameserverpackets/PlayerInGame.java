package pk.elfo.gameserver.network.gameserverpackets;

import pk.elfo.util.network.BaseSendablePacket;
import javolution.util.FastList;

public class PlayerInGame extends BaseSendablePacket
{
	public PlayerInGame(String player)
	{
		writeC(0x02);
		writeH(1);
		writeS(player);
	}
	
	public PlayerInGame(FastList<String> players)
	{
		writeC(0x02);
		writeH(players.size());
		for (String pc : players)
		{
			writeS(pc);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}