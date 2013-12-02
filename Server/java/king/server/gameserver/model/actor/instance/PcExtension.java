package king.server.gameserver.model.actor.instance;

import java.util.logging.Logger;

import king.server.Config;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class PcExtension
{
	protected static final Logger _log = Logger.getLogger(PcExtension.class.getName());
	private L2PcInstance _activeChar = null;
	
	public PcExtension(L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			_log.warning("[PcExtension] _activeChar: There can be a null value!");
			return;
		}
		_activeChar = activeChar;
		if (Config.DEBUG)
		{
			_log.info("[PcExtension] _activeChar: " + _activeChar.getObjectId() + " - " + _activeChar.getName() + ".");
		}
	}

	public L2PcInstance getPlayer()
	{
		return _activeChar;
	}
}