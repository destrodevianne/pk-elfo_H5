package pk.elfo.gameserver.masteriopack.rankpvpsystem;

import java.util.logging.Logger;

import pk.elfo.gameserver.communitybbs.Manager.BaseBBSManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.ShowBoard;

/**
 * PkElfo
 */
public class RPSBBSManager extends BaseBBSManager
{	
	public static final Logger log = Logger.getLogger(RPSBBSManager.class.getName());
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
		if(RPSConfig.RANK_PVP_SYSTEM_ENABLED && RPSConfig.TOP_LIST_ENABLED)
		{
			if(command.startsWith("_bbsrps:"))
			{
				int page = 0;
				try
				{
					page = Integer.parseInt(command.split(":", 2)[1].trim());
				}
				catch(Exception e)
				{
					log.info(e.getMessage());
					page = 0;
				}
				
				separateAndSend(RPSHtmlCommunityBoard.getPage(activeChar, page), activeChar);
			}
		}
		else if(RPSConfig.RANK_PVP_SYSTEM_ENABLED && !RPSConfig.TOP_LIST_ENABLED)
		{
			ShowBoard sb = null;

			sb = new ShowBoard("<html><body><br><br><center>Community Board Top List is disabled in config file</center><br><br></body></html>", "101");

			activeChar.sendPacket(sb);
			activeChar.sendPacket(new ShowBoard(null, "102"));
			activeChar.sendPacket(new ShowBoard(null, "103"));
			
			sb = null;
		}
	}

	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		// 
	}

	private static RPSBBSManager _instance = new RPSBBSManager();

	public static RPSBBSManager getInstance()
	{
		return _instance;
	}
}
