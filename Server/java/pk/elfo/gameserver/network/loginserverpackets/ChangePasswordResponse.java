package pk.elfo.gameserver.network.loginserverpackets;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.network.BaseRecievePacket;

public class ChangePasswordResponse extends BaseRecievePacket
{
	public ChangePasswordResponse(byte[] decrypt)
	{
		super(decrypt);
		// boolean isSuccessful = readC() > 0;
		String character = readS();
		String msgToSend = readS();
		
		L2PcInstance player = L2World.getInstance().getPlayer(character);
		
		if (player != null)
		{
			player.sendMessage(msgToSend);
		}
	}
}