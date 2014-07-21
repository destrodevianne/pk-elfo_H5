package handlers.aioitemhandler;

import java.util.logging.Logger;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.datatables.AIOItemTable;
import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */

public class AIOTopList implements IAIOItemHandler
{
	private static final Logger _log = Logger.getLogger(AIOTopList.class.getName());
	
	/* (non-Javadoc)
	 * @see pk.elfo.gameserver.handler.IAIOItemHandler#getBypass()
	 */
	@Override
	public String getBypass()
	{
		return "toplist";
	}

	/* (non-Javadoc)
	 * @see pk.elfo.gameserver.handler.IAIOItemHandler#onBypassUse(pk.elfo.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public void onBypassUse(L2PcInstance player, String command)
	{
		if(command.startsWith("pvptop"))
		{
			final String html = HtmCache.getInstance().getHtm(null, "data/html/aioitem/pvptoplist.htm");
			if(html != null)
			{
				final String list = AIOItemTable.getInstance().getPvpRank();
				if(list == null)
				{
					_log.warning("AIOItemTable: PvP Rank is null!");
					return;
				}
				
				NpcHtmlMessage msg = new NpcHtmlMessage(5);
				msg.setHtml(html);
				msg.replace("%list%", list);
				player.sendPacket(msg);
			}
		}
		else if(command.startsWith("pktop"))
		{
			final String html = HtmCache.getInstance().getHtm(null, "data/html/aioitem/pktoplist.htm");
			if(html != null)
			{
				final String list = AIOItemTable.getInstance().getPkRank();
				if(list == null)
				{
					_log.warning("AIOItemTable: Pk Rank is null!");
					return;
				}
				
				NpcHtmlMessage msg = new NpcHtmlMessage(5);
				msg.setHtml(html);
				msg.replace("%list%", list);
				player.sendPacket(msg);
			}
		}
	}
}