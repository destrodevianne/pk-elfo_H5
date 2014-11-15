package pk.elfo.gameserver.network.communityserver.readpackets;

import org.netcon.BaseReadPacket;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.CSShowComBoard;

public final class RequestPlayerShowBoard extends BaseReadPacket
{
	// private static final Logger _log = Logger.getLogger(RequestPlayerShowBoard.class.getName());
	
	public RequestPlayerShowBoard(final byte[] data)
	{
		super(data);
	}
	
	@Override
	public final void run()
	{
		final int playerObjId = super.readD();
		final int length = super.readD();
		final byte[] html = super.readB(length);
		
		L2PcInstance player = L2World.getInstance().getPlayer(playerObjId);
		if (player == null)
		{
			return;
		}
		player.sendPacket(new CSShowComBoard(html));
	}
}