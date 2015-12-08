package pk.elfo.gameserver.network.communityserver.readpackets;

import java.util.logging.Logger;

import org.netcon.BaseReadPacket;

public final class ConnectionError extends BaseReadPacket
{
	private static final Logger _log = Logger.getLogger(ConnectionError.class.getName());
	
	public ConnectionError(final byte[] data)
	{
		super(data);
	}
	
	@Override
	public final void run()
	{
		_log.info("ConnectionErrorPacket received: " + super.readC());
	}
}