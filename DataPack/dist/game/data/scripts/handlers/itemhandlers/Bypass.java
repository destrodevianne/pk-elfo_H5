package handlers.itemhandlers;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */
 
public class Bypass implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof L2PcInstance))
		{
			return false;
		}
		L2PcInstance activeChar = (L2PcInstance) playable;
		final int itemId = item.getItemId();
		
		String filename = "data/html/item/" + itemId + ".htm";
		String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), filename);
		NpcHtmlMessage html = new NpcHtmlMessage(0, itemId);
		if (content == null)
		{
			html.setHtml("<html><body>Meu texto esta faltando:<br>" + filename + "</body></html>");
			activeChar.sendPacket(html);
		}
		else
		{
			html.setHtml(content);
			html.replace("%itemId%", String.valueOf(item.getObjectId()));
			activeChar.sendPacket(html);
		}
		return true;
	}
}