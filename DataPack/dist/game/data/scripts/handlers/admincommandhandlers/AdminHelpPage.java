package handlers.admincommandhandlers;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
 
/**
 * Projeto PkElfo
 */

public class AdminHelpPage implements IAdminCommandHandler
{
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_help"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		
		if (command.startsWith("admin_help"))
		{
			try
			{
				String val = command.substring(11);
				showHelpPage(activeChar, val);
			}
			catch (StringIndexOutOfBoundsException e)
			{
				// case of empty filename
			}
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	// FIXME: implement method to send html to player in L2PcInstance directly
	// PUBLIC & STATIC so other classes from package can include it directly
	public static void showHelpPage(L2PcInstance targetChar, String filename)
	{
		String content = HtmCache.getInstance().getHtmForce(targetChar.getHtmlPrefix(), "data/html/admin/" + filename);
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setHtml(content);
		targetChar.sendPacket(adminReply);
	}
}
