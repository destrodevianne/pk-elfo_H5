package handlers.itemhandlers;

import java.util.logging.Logger;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.datatables.AIOItemTable;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */

public class AIOItem implements IItemHandler
{
	private static final Logger _log = Logger.getLogger(AIOItem.class.getName());
	
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		/*
		 * Null pointer check
		 */
		if (playable == null)
		{
			return false;
		}
		/*
		 * Only players can use it
		 */
		if (playable instanceof L2PcInstance)
		{
			L2PcInstance player = (L2PcInstance) playable;
			
			/*
			 * Minumun requirements to use it
			 */
			if (!AIOItemTable.getInstance().checkPlayerConditions(player))
			{
				return false;
			}
			
			String html = HtmCache.getInstance().getHtm(null, "data/html/aioitem/main.htm");
			
			if (html == null)
			{
				_log.severe("AIOItem: The main file [data/html/aioitem/main.htm] does not exist or is corrupted!");
				return false;
			}
			
			NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setHtml(html);
			player.sendPacket(msg);
			return true;
		}
		return false;
	}
}