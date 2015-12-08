package pk.elfo.gameserver.handler;

import java.util.logging.Logger;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public interface IActionHandler
{
	public static Logger _log = Logger.getLogger(IActionHandler.class.getName());
	
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact);
	
	public InstanceType getInstanceType();
}