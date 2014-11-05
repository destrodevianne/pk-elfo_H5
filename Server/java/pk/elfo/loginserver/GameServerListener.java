package pk.elfo.loginserver;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

import pk.elfo.Config;
import javolution.util.FastList;

/**
 * PkElfo
 */
 
public class GameServerListener extends FloodProtectedListener
{
	private static Logger _log = Logger.getLogger(GameServerListener.class.getName());
	private static List<GameServerThread> _gameServers = new FastList<>();
	
	public GameServerListener() throws IOException
	{
		super(Config.GAME_SERVER_LOGIN_HOST, Config.GAME_SERVER_LOGIN_PORT);
		setName(getClass().getSimpleName());
	}
	
	@Override
	public void addClient(Socket s)
	{
		if (Config.DEBUG)
		{
			_log.info("Received gameserver connection from: " + s.getInetAddress().getHostAddress());
		}
		GameServerThread gst = new GameServerThread(s);
		_gameServers.add(gst);
	}
	
	public void removeGameServer(GameServerThread gst)
	{
		_gameServers.remove(gst);
	}
}