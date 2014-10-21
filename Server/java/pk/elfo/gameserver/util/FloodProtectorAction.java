package pk.elfo.gameserver.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.gameserver.GameTimeController;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.util.StringUtil;

/**
 * Flood protector implementation.
 * @author fordfrog
 */
public final class FloodProtectorAction
{
	/**
	 * Logger
	 */
	private static final Logger _log = Logger.getLogger(FloodProtectorAction.class.getName());
	/**
	 * Client for this instance of flood protector.
	 */
	private final L2GameClient _client;
	/**
	 * Configuration of this instance of flood protector.
	 */
	private final FloodProtectorConfig _config;
	/**
	 * Next game tick when new request is allowed.
	 */
	private volatile int _nextGameTick = GameTimeController.getGameTicks();
	/**
	 * Request counter.
	 */
	private final AtomicInteger _count = new AtomicInteger(0);
	/**
	 * Flag determining whether exceeding request has been logged.
	 */
	private boolean _logged;
	/**
	 * Flag determining whether punishment application is in progress so that we do not apply punisment multiple times (flooding).
	 */
	private volatile boolean _punishmentInProgress;
	
	/**
	 * Creates new instance of FloodProtectorAction.
	 * @param client the game client for which flood protection is being created
	 * @param config flood protector configuration
	 */
	public FloodProtectorAction(final L2GameClient client, final FloodProtectorConfig config)
	{
		super();
		_client = client;
		_config = config;
	}
	
	/**
	 * Checks whether the request is flood protected or not.
	 * @param command command issued or short command description
	 * @return true if action is allowed, otherwise false
	 */
	public boolean tryPerformAction(final String command)
	{
		final int curTick = GameTimeController.getGameTicks();
		
		if ((_client.getActiveChar() != null) && _client.getActiveChar().canOverrideCond(PcCondOverride.FLOOD_CONDITIONS))
		{
			return true;
		}
		
		if ((curTick < _nextGameTick) || _punishmentInProgress)
		{
			if (_config.LOG_FLOODING && !_logged && _log.isLoggable(Level.WARNING))
			{
				log(" called command ", command, " ~", String.valueOf((_config.FLOOD_PROTECTION_INTERVAL - (_nextGameTick - curTick)) * GameTimeController.MILLIS_IN_TICK), " ms after previous command");
				_logged = true;
			}
			
			_count.incrementAndGet();
			
			if (!_punishmentInProgress && (_config.PUNISHMENT_LIMIT > 0) && (_count.get() >= _config.PUNISHMENT_LIMIT) && (_config.PUNISHMENT_TYPE != null))
			{
				_punishmentInProgress = true;
				
				if ("kick".equals(_config.PUNISHMENT_TYPE))
				{
					kickPlayer();
				}
				else if ("ban".equals(_config.PUNISHMENT_TYPE))
				{
					banAccount();
				}
				else if ("jail".equals(_config.PUNISHMENT_TYPE))
				{
					jailChar();
				}
				
				_punishmentInProgress = false;
			}
			return false;
		}
		
		if (_count.get() > 0)
		{
			if (_config.LOG_FLOODING && _log.isLoggable(Level.WARNING))
			{
				log(" issued ", String.valueOf(_count), " extra requests within ~", String.valueOf(_config.FLOOD_PROTECTION_INTERVAL * GameTimeController.MILLIS_IN_TICK), " ms");
			}
		}
		
		_nextGameTick = curTick + _config.FLOOD_PROTECTION_INTERVAL;
		_logged = false;
		_count.set(0);
		return true;
	}
	
	/**
	 * Kick player from game (close network connection).
	 */
	private void kickPlayer()
	{
		if (_client.getActiveChar() != null)
		{
			_client.getActiveChar().logout(false);
		}
		else
		{
			_client.closeNow();
		}
		
		if (_log.isLoggable(Level.WARNING))
		{
			log("kicked for flooding");
		}
	}
	
	/**
	 * Bans char account and logs out the char.
	 */
	private void banAccount()
	{
		if (_client.getActiveChar() != null)
		{
			_client.getActiveChar().setPunishLevel(L2PcInstance.PunishLevel.ACC, _config.PUNISHMENT_TIME);
			
			if (_log.isLoggable(Level.WARNING))
			{
				log(" banned for flooding ", _config.PUNISHMENT_TIME <= 0 ? "forever" : "for " + (_config.PUNISHMENT_TIME / 60000) + " mins");
			}
			
			_client.getActiveChar().logout();
		}
		else
		{
			log(" unable to ban account: no active player");
		}
	}
	
	/**
	 * Jails char.
	 */
	private void jailChar()
	{
		if (_client.getActiveChar() != null)
		{
			_client.getActiveChar().setPunishLevel(L2PcInstance.PunishLevel.JAIL, _config.PUNISHMENT_TIME);
			
			if (_log.isLoggable(Level.WARNING))
			{
				log(" jailed for flooding ", _config.PUNISHMENT_TIME <= 0 ? "forever" : "for " + (_config.PUNISHMENT_TIME / 60000) + " mins");
			}
		}
		else
		{
			log(" unable to jail: no active player");
		}
	}
	
	private void log(String... lines)
	{
		final StringBuilder output = StringUtil.startAppend(100, _config.FLOOD_PROTECTOR_TYPE, ": ");
		String address = null;
		try
		{
			if (!_client.isDetached())
			{
				address = _client.getConnection().getInetAddress().getHostAddress();
			}
		}
		catch (Exception e)
		{
		}
		
		switch (_client.getState())
		{
			case IN_GAME:
				if (_client.getActiveChar() != null)
				{
					StringUtil.append(output, _client.getActiveChar().getName());
					StringUtil.append(output, "(", String.valueOf(_client.getActiveChar().getObjectId()), ") ");
				}
				break;
			case AUTHED:
				if (_client.getAccountName() != null)
				{
					StringUtil.append(output, _client.getAccountName(), " ");
				}
				break;
			case CONNECTED:
				if (address != null)
				{
					StringUtil.append(output, address);
				}
				break;
			default:
				throw new IllegalStateException("Missing state on switch");
		}
		
		StringUtil.append(output, lines);
		_log.warning(output.toString());
	}
}