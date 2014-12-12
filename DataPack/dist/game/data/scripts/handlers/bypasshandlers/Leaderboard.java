package handlers.bypasshandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.instancemanager.leaderboards.ArenaLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.CraftLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.FishermanLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.TvTLeaderboard;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
 
/**
 * Projeto PkElfo
 */

public class Leaderboard implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"rank_arena_info",
		"rank_fisherman_info",
		"rank_craft_info",
		"rank_tvt_info"
	};

	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!(target instanceof L2Npc))
			return false;

		NpcHtmlMessage html = new NpcHtmlMessage(((L2Npc)target).getObjectId());
		
		if (command.toLowerCase().startsWith(COMMANDS[0]) && Config.RANK_ARENA_ENABLED)
		{
			html.setHtml(ArenaLeaderboard.getInstance().showHtm(activeChar.getObjectId()));
			activeChar.sendPacket(html);
		}
		else if (command.toLowerCase().startsWith(COMMANDS[1]) && Config.RANK_FISHERMAN_ENABLED)
		{
			html.setHtml(FishermanLeaderboard.getInstance().showHtm(activeChar.getObjectId()));
			activeChar.sendPacket(html);
		}
		else if (command.toLowerCase().startsWith(COMMANDS[2]) && Config.RANK_CRAFT_ENABLED)
		{
			html.setHtml(CraftLeaderboard.getInstance().showHtm(activeChar.getObjectId()));
			activeChar.sendPacket(html);
		}
		else if (command.toLowerCase().startsWith(COMMANDS[3]) && Config.RANK_TVT_ENABLED)
		{
			html.setHtml(TvTLeaderboard.getInstance().showHtm(activeChar.getObjectId()));
			activeChar.sendPacket(html);
		}
		return false;
	}

	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}