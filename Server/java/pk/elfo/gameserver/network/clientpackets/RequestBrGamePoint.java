package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ExBrGamePoint;

/**
 * Author: VISTALL Company: J Develop Station Date: 0:36:26/10.04.2010
 */
public class RequestBrGamePoint extends L2GameClientPacket
{
	private static final String TYPE = "[C] D0:89 RequestBrGamePoint";
	
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
		player.sendPacket(new ExBrGamePoint(player));
	}
	
	@Override
	public String getType()
	{
		return TYPE;
	}
}
