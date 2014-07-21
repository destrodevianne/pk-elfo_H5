package pk.elfo.gameserver.handler;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public interface IAIOItemHandler
{
	public String getBypass();
	
	public void onBypassUse(L2PcInstance player, String command);
}