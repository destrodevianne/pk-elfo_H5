package handlers.aioitemhandler;

import java.util.logging.Logger;

import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.SiegeInfo;

/**
 * PkElfo
 */

public final class AIOSiegeHandler implements IAIOItemHandler
{
	private final String BYPASS = "siege";
	private final Logger _log = Logger.getLogger(AIOSiegeHandler.class.getName());
	
	@Override
	public String getBypass()
	{
		return BYPASS;
	}

	@Override
	public void onBypassUse(L2PcInstance player, String command)
	{
		int castle = 0;
		try
		{
			castle = Integer.parseInt(command);
		}
		catch(NumberFormatException nfe)
		{
			_log.warning("AIOSiegeHandler: Wrong castle id given: "+command);
		}
		
		if(castle >= 1 && castle <= 9)
			player.sendPacket(new SiegeInfo(CastleManager.getInstance().getCastleById(castle)));
		else
			player.sendMessage("Could not find the requested castle.");
	}
}