package handlers.usercommandhandlers;

import king.server.gameserver.handler.IUserCommandHandler;
import king.server.gameserver.instancemanager.MapRegionManager;
import king.server.gameserver.instancemanager.ZoneManager;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.base.Race;
import king.server.gameserver.model.zone.type.L2RespawnZone;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
public class Loc implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		0
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		int region;
		L2RespawnZone zone = ZoneManager.getInstance().getZone(activeChar, L2RespawnZone.class);
		if (zone != null)
		{
			region = MapRegionManager.getInstance().getRestartRegion(activeChar, zone.getAllRespawnPoints().get(Race.Human)).getLocId();
		}
		else
		{
			region = MapRegionManager.getInstance().getMapRegionLocId(activeChar);
		}
		
		SystemMessage sm;
		if (region > 0)
		{
			sm = SystemMessage.getSystemMessage(region);
			if (sm.getSystemMessageId().getParamCount() == 3)
			{
				sm.addNumber(activeChar.getX());
				sm.addNumber(activeChar.getY());
				sm.addNumber(activeChar.getZ());
			}
		}
		else
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.CURRENT_LOCATION_S1);
			sm.addString(activeChar.getX() + ", " + activeChar.getY() + ", " + activeChar.getZ());
		}
		activeChar.sendPacket(sm);
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
