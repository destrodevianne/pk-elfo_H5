package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.Config;
import pk.elfo.gameserver.LoginServerThread;
import pk.elfo.gameserver.LoginServerThread.SessionKey;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * This class ...
 * @version $Revision: 1.9.2.3.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class AuthLogin extends L2GameClientPacket
{
	private static final String _C__2B_AUTHLOGIN = "[C] 2B AuthLogin";
	// loginName + keys must match what the loginserver used.
	private String _loginName;
	/*
	 * private final long _key1; private final long _key2; private final long _key3; private final long _key4;
	 */
	private int _playKey1;
	private int _playKey2;
	private int _loginKey1;
	private int _loginKey2;
	
	@Override
	protected void readImpl()
	{
		_loginName = readS().toLowerCase();
		_playKey2 = readD();
		_playKey1 = readD();
		_loginKey1 = readD();
		_loginKey2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2GameClient client = getClient();
		if (_loginName.isEmpty() || !client.isProtocolOk())
		{
			client.close((L2GameServerPacket) null);
			return;
		}
		SessionKey key = new SessionKey(_loginKey1, _loginKey2, _playKey1, _playKey2);
		if (Config.DEBUG)
		{
			_log.info("user:" + _loginName);
			_log.info("key:" + key);
		}
		
		// avoid potential exploits
		if (client.getAccountName() == null)
		{
			client.setAccountName(_loginName);
			LoginServerThread.getInstance().addGameServerLogin(_loginName, client);
			LoginServerThread.getInstance().addWaitingClientAndSendRequest(_loginName, client, key);
		}
	}
	
	@Override
	public String getType()
	{
		return _C__2B_AUTHLOGIN;
	}
}