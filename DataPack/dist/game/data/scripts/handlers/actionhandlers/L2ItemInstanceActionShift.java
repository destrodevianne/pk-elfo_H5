package handlers.actionhandlers;

import pk.elfo.gameserver.handler.IActionHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;

/**
 * Projeto PkElfo
 */

public class L2ItemInstanceActionShift implements IActionHandler
{
	@Override
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact)
	{
		if (activeChar.getAccessLevel().isGm())
		{
			NpcHtmlMessage html = new NpcHtmlMessage(target.getObjectId());
			final String html1 = StringUtil.concat("<html><body><center><font color=\"LEVEL\">Item Info</font></center><br><table border=0>", "<tr><td>Objeto ID: </td><td>", String.valueOf(target.getObjectId()), "</td></tr><tr><td>Item ID: </td><td>", String.valueOf(((L2ItemInstance) target).getItemId()), "</td></tr><tr><td>Dono ID: </td><td>", String.valueOf(((L2ItemInstance) target).getOwnerId()), "</td></tr><tr><td>Localizacao: </td><td>", String.valueOf(((L2ItemInstance) target).getLocation()), "</td></tr><tr><td><br></td></tr><tr><td>Classe: </td><td>", target.getClass().getSimpleName(), "</td></tr></table></body></html>");
			html.setHtml(html1);
			activeChar.sendPacket(html);
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2ItemInstance;
	}
}