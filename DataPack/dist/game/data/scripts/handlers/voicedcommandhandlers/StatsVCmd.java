package handlers.voicedcommandhandlers;

import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.entity.L2Event;
import king.server.gameserver.network.SystemMessageId;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;
import king.server.gameserver.network.serverpackets.SystemMessage;
import king.server.util.StringUtil;

/**
 * PkElfo
 */
public class StatsVCmd implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"stats"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		if (!command.equals("stats") || (params == null) || params.isEmpty())
		{
			activeChar.sendMessage("Use: .stats <player name>");
			return false;
		}
		
		final L2PcInstance pc = L2World.getInstance().getPlayer(params);
		if ((pc == null))
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			return false;
		}
		
		if (pc.getClient().isDetached())
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_OFFLINE);
			sm.addPcName(pc);
			activeChar.sendPacket(sm);
			return false;
		}
		
		if (!L2Event.isParticipant(pc) || (pc.getEventStatus() == null))
		{
			activeChar.sendMessage("Esse jogador nao e um participante do evento.");
			return false;
		}
		
		final StringBuilder replyMSG = StringUtil.startAppend(300 + (pc.getEventStatus().kills.size() * 50), "<html><body>" + "<center><font color=\"LEVEL\">[ L2J EVENT ENGINE ]</font></center><br><br>Statistics for player <font color=\"LEVEL\">", pc.getName(), "</font><br>Total kills <font color=\"FF0000\">", String.valueOf(pc.getEventStatus().kills.size()), "</font><br><br>Detailed list: <br>");
		for (L2PcInstance plr : pc.getEventStatus().kills)
		{
			StringUtil.append(replyMSG, "<font color=\"FF0000\">", plr.getName(), "</font><br>");
		}
		replyMSG.append("</body></html>");
		final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
