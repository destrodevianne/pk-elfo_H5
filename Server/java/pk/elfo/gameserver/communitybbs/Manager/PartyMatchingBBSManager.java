package pk.elfo.gameserver.communitybbs.Manager;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.communitybbs.PartyMatchingBoard;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

/**
 * PkElfo
 */

public class PartyMatchingBBSManager extends BaseBBSManager
{
    @Override
    public void parsecmd(String command, L2PcInstance activeChar)
    {
        if (command.equals("_bbslink"))
        {
            PartyMatchingBoard partyMembers = new PartyMatchingBoard();
            String content = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/CommunityBoard/home.htm").replace("%partyMatchingMembers%", partyMembers.loadPartyMatchingMembersList());

            if (content == null)
            {
                content = "<html><body><br><br><center>404 :File not found: 'data/html/CommunityBoard/home.htm'</center></body></html>";
            }

            separateAndSend(content, activeChar);
        }
        else
        {
            ShowBoard sb = new ShowBoard("<html><body><br><br><center>The command: " + command + " is not implemented yet.</center><br><br></body></html>", "101");
            activeChar.sendPacket(sb);
            activeChar.sendPacket(new ShowBoard(null, "102"));
            activeChar.sendPacket(new ShowBoard(null, "103"));
        }
    }

    @Override
    public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
    {
    }

    public static PartyMatchingBBSManager getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final PartyMatchingBBSManager _instance = new PartyMatchingBBSManager();
    }
}