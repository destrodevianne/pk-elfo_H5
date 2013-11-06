package king.server.gameserver.handler;

import java.util.logging.Logger;

import king.server.gameserver.model.L2Object;
import king.server.gameserver.model.L2Object.InstanceType;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public interface IActionHandler
{
	public static Logger _log = Logger.getLogger(IActionHandler.class.getName());
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact);
	public InstanceType getInstanceType();
}