package pk.elfo.gameserver.communitybbs;

import pk.elfo.Config;
import pk.elfo.gameserver.communitybbs.Manager.ClanBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.HomeBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.PostBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.RegionBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.TopBBSManager;
import pk.elfo.gameserver.communitybbs.Manager.TopicBBSManager;
import pk.elfo.gameserver.masteriopack.rankpvpsystem.RPSBBSManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

public class CommunityBoard
{
    private CommunityBoard()
    {
    }

    public static CommunityBoard getInstance()
    {
        return SingletonHolder._instance;
    }

    public void handleCommands(L2GameClient client, String command)
    {
        L2PcInstance activeChar = client.getActiveChar();
        if (activeChar == null)
        {
            return;
        }

        switch (Config.COMMUNITY_TYPE)
        {
            default:
            case 0: // disabled
                activeChar.sendPacket(SystemMessageId.CB_OFFLINE);
                break;
            case 1: // old
                RegionBBSManager.getInstance().parsecmd(command, activeChar);
                break;
            case 2: // new
                if (command.startsWith("_bbsclan"))
                {
                    ClanBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbsmemo"))
                {
                    TopicBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbstopics"))
                {
                    TopicBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbsposts"))
                {
                    PostBBSManager.getInstance().parsecmd(command, activeChar);
                }

                else if (command.startsWith("_bbslink"))
                {
                    HomeBBSManager.getInstance().parsecmd(command, activeChar);
                }

                else if (command.startsWith("_bbstop"))
                {
                    TopBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbshome"))
                {
                    TopBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbsloc"))
                {
                    RegionBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else if (command.startsWith("_bbsrps"))
                {
                    RPSBBSManager.getInstance().parsecmd(command, activeChar);
                }
                else
                {
                    ShowBoard sb = new ShowBoard("<html><body><br><br><center>Coming Soon!</center><br><br></body></html>", "101");
                    activeChar.sendPacket(sb);
                    activeChar.sendPacket(new ShowBoard(null, "102"));
                    activeChar.sendPacket(new ShowBoard(null, "103"));
                }
                break;
        }
    }

    /**
     * @param client
     * @param url
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     * @param arg5
     */
    public void handleWriteCommands(L2GameClient client, String url, String arg1, String arg2, String arg3, String arg4, String arg5)
    {
        L2PcInstance activeChar = client.getActiveChar();
        if (activeChar == null)
        {
            return;
        }

        switch (Config.COMMUNITY_TYPE)
        {
            case 2:
                if (url.equals("Topic"))
                {
                    TopicBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
                }
                else if (url.equals("Post"))
                {
                    PostBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
                }
                else if (url.equals("Region"))
                {
                    RegionBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
                }
                else if (url.equals("Notice"))
                {
                    ClanBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
                }
                else
                {
                    ShowBoard sb = new ShowBoard("<html><body><br><br><center>Coming Soon!</center><br><br></body></html>", "101");
                    activeChar.sendPacket(sb);
                    activeChar.sendPacket(new ShowBoard(null, "102"));
                    activeChar.sendPacket(new ShowBoard(null, "103"));
                }
                break;
            case 1:
                RegionBBSManager.getInstance().parsewrite(arg1, arg2, arg3, arg4, arg5, activeChar);
                break;
            default:
            case 0:
                ShowBoard sb = new ShowBoard("<html><body><br><br><center>Server is temporarily unavailable.</center><br><br></body></html>", "101");
                activeChar.sendPacket(sb);
                activeChar.sendPacket(new ShowBoard(null, "102"));
                activeChar.sendPacket(new ShowBoard(null, "103"));
                break;
        }
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder
    {
        protected static final CommunityBoard _instance = new CommunityBoard();
    }
}