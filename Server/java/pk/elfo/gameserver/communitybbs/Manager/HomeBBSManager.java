package pk.elfo.gameserver.communitybbs.Manager;

import java.util.StringTokenizer;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

public class HomeBBSManager extends BaseBBSManager
{
    private HomeBBSManager()
    {
    }

    @Override
    public void parsecmd(String command, L2PcInstance activeChar)
    {
        if (command.equals("_bbslink"))
        {
            String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/home.htm");
            if (content == null)
            {
                content = "<html><body><br><br><center>404 :File not found: 'data/html/CommunityBoard/home.htm' </center></body></html>";
            }
            separateAndSend(content, activeChar);
        }
        else if (command.equals("_bbslink"))
        {
            String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/home.htm");
            if (content == null)
            {
                content = "<html><body><br><br><center>404 :File not found: 'data/html/CommunityBoard/home.htm' </center></body></html>";
            }
            separateAndSend(content, activeChar);
        }
        else if (command.startsWith("_bbstop;"))
        {
            StringTokenizer st = new StringTokenizer(command, ";");
            st.nextToken();
            final int idp = Integer.parseInt(st.nextToken());
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
    }

    public static HomeBBSManager getInstance()
    {
        return SingletonHolder._instance;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final HomeBBSManager _instance = new HomeBBSManager();
    }
}