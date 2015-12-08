package pk.elfo.gameserver.communitybbs.Manager;

import java.util.StringTokenizer;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

public class AdminBBSManager extends BaseBBSManager
{
	/**
	 * PkElfo
	 */
	public static AdminBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private AdminBBSManager()
	{
	}
	
	/**
	 * @see com.l2jserver.gameserver.communitybbs.Manager.BaseBBSManager#parsecmd(java.lang.String, com.l2jserver.gameserver.model.actor.instance.L2PcInstance)
	 */
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if (!activeChar.isGM())
		{
			return;
		}
		if (command.startsWith("admin_bbs"))
		{
			separateAndSend("<html><body><br><br><center>This Page is only an exemple :)<br><br>command=" + command + "</center></body></html>", activeChar);
		}
		else if (command.startsWith("_bbsadmin;"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			int idp = Integer.parseInt(st.nextToken());
			String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/" + idp + ".htm");
			if (content == null)
			{
				content = "<html><body><br><br><center>404 :File not found: 'data/html/CommunityBoard/" + idp + ".htm' </center></body></html>";
			}
			separateAndSend(content, activeChar);
		}
		else
		{
			ShowBoard sb = new ShowBoard("<html><body><br><br><center>the command: " + command + " is not implemented yet</center><br><br></body></html>", "101");
			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
		}
		
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		if (!activeChar.isGM())
		{
			return;
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AdminBBSManager _instance = new AdminBBSManager();
	}
}