package handlers.voicedcommandhandlers;

import king.server.Config;
import king.server.gameserver.cache.HtmCache;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.TvTRoundEvent;
import king.server.gameserver.network.serverpackets.ActionFailed;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */
public class TvTRoundVoicedInfo implements IVoicedCommandHandler
{
	private static final String[] _voicedCommands = { "tvtround" };
	
	/**
	 * Set this to false and recompile script if you dont want to use string cache.
	 * This will decrease performance but will be more consistent against possible html editions during runtime
	 * Recompiling the script will get the new html would be enough too [DrHouse]
	 */
	private static final boolean USE_STATIC_HTML = true;
	private static final String HTML = HtmCache.getInstance().getHtm(null, "dist/game/data/html/mods/TvTRoundEvent/Status.htm");
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("tvtround"))
		{
			if (TvTRoundEvent.isStarting() || TvTRoundEvent.isStarted())
			{
				String htmContent = (USE_STATIC_HTML && !HTML.isEmpty()) ? HTML :
					HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "dist/game/data/html/mods/TvTRoundEvent/Status.htm");
				
				try
				{
					NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
					
					npcHtmlMessage.setHtml(htmContent);
					// npcHtmlMessage.replace("%objectId%",
					// String.valueOf(getObjectId()));
					npcHtmlMessage.replace("%roundteam1name%", Config.TVT_ROUND_EVENT_TEAM_1_NAME);
					npcHtmlMessage.replace("%roundteam1playercount%", String.valueOf(TvTRoundEvent.getTeamsPlayerCounts()[0]));
					npcHtmlMessage.replace("%roundteam1points%", String.valueOf(TvTRoundEvent.getTeamsPoints()[0]));
					npcHtmlMessage.replace("%roundteam2name%", Config.TVT_ROUND_EVENT_TEAM_2_NAME);
					npcHtmlMessage.replace("%roundteam2playercount%", String.valueOf(TvTRoundEvent.getTeamsPlayerCounts()[1]));
					npcHtmlMessage.replace("%roundteam2points%", String.valueOf(TvTRoundEvent.getTeamsPoints()[1]));
					activeChar.sendPacket(npcHtmlMessage);
				}
				catch (Exception e)
				{
					_log.warning("wrong TvT Round voiced: " + e);
				}
				
			}
			else
			{
				activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}