package handlers.aioitemhandler;

import java.util.logging.Logger;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */

public class AIOChatHandler implements IAIOItemHandler
{
	private static final Logger _log = Logger.getLogger(AIOChatHandler.class.getName());
	private static final String ROOT = "data/html/";
	
	/* (non-Javadoc)
	 * @see pk.elfo.gameserver.handler.IAIOItemHandler#getBypass()
	 */
	@Override
	public String getBypass()
	{
		return "chat";
	}

	/* (non-Javadoc)
	 * @see pk.elfo.gameserver.handler.IAIOItemHandler#onBypassUse(pk.elfo.gameserver.model.actor.instance.L2PcInstance, java.lang.String)
	 */
	@Override
	public void onBypassUse(L2PcInstance player, String command)
	{
		final String fileRoot = ROOT + command + ".htm";
		final String html = HtmCache.getInstance().getHtm(null, fileRoot);
		if(html != null)
		{
			NpcHtmlMessage msg = new NpcHtmlMessage(5);
			msg.setHtml(html);
			msg.replace("%name%", player.getName());
			player.sendPacket(msg);
		}
		else
		{
			player.sendMessage("A pagina requerida nao existe. Ask Admin to check it");
			_log.warning("AIOChatHandler: Missing HTML page requested, at: "+fileRoot);
		}
	}
}