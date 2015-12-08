package pk.elfo.gameserver.network.gameserverpackets;

import java.util.List;

import pk.elfo.util.network.BaseSendablePacket;

public class AuthRequest extends BaseSendablePacket
{
	/**
	 * Format: cccSddb c desired ID c accept alternative ID c reserve Host s ExternalHostName s InetranlHostName d max players d hexid size b hexid
	 * @param id
	 * @param acceptAlternate
	 * @param hexid
	 * @param port
	 * @param reserveHost
	 * @param maxplayer
	 * @param subnets the subnets lists
	 * @param hosts the hosts list
	 */
	public AuthRequest(int id, boolean acceptAlternate, byte[] hexid, int port, boolean reserveHost, int maxplayer, List<String> subnets, List<String> hosts)
	{
		writeC(0x01);
		writeC(id);
		writeC(acceptAlternate ? 0x01 : 0x00);
		writeC(reserveHost ? 0x01 : 0x00);
		writeH(port);
		writeD(maxplayer);
		writeD(hexid.length);
		writeB(hexid);
		writeD(subnets.size());
		for (int i = 0; i < subnets.size(); i++)
		{
			writeS(subnets.get(i));
			writeS(hosts.get(i));
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}