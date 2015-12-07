package pk.elfo.gameserver.scripting.scriptengine.listeners.clan;

import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.scripting.scriptengine.events.ClanWarEvent;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2JListener;

/**
 * Notifies when a clan war starts or ends
 * @author TheOne
 */
public abstract class ClanWarListener extends L2JListener
{
	public ClanWarListener()
	{
		register();
	}
	
	/**
	 * Clan war just started
	 * @param event
	 * @return
	 */
	public abstract boolean onWarStart(ClanWarEvent event);
	
	/**
	 * Clan war just ended
	 * @param event
	 * @return
	 */
	public abstract boolean onWarEnd(ClanWarEvent event);
	
	@Override
	public void register()
	{
		ClanTable.addClanWarListener(this);
	}
	
	@Override
	public void unregister()
	{
		ClanTable.removeClanWarListener(this);
	}
}
