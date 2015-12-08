package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExBrProductList;

/**
 * Author: VISTALL Company: J Develop Station Date: 1:50:38/10.04.2010
 */
public class RequestBrProductList extends L2GameClientPacket
{
	private static final String TYPE = "[C] D0:8A RequestBrProductList";
	
	@Override
	protected void readImpl()
	{
		
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		player.sendPacket(new ExBrProductList());
	}
	
	@Override
	public String getType()
	{
		return TYPE;
	}
}
