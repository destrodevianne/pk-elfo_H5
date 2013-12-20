package handlers.usercommandhandlers;

import java.text.SimpleDateFormat;

import king.server.gameserver.handler.IUserCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;
import king.server.util.StringUtil;

/**
 * PkElfo
 */
public class ClanPenalty implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		100
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		boolean penalty = false;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		final StringBuilder htmlContent = StringUtil.startAppend(500, "<html><body><center><table width=270 border=0 bgcolor=111111><tr><td width=170>Penalty</td><td width=100 align=center>Expiration Date</td></tr></table><table width=270 border=0><tr>");
		
		if (activeChar.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			StringUtil.append(htmlContent, "<td width=170>Unable to join a clan.</td><td width=100 align=center>", format.format(activeChar.getClanJoinExpiryTime()), "</td>");
			penalty = true;
		}
		
		if (activeChar.getClanCreateExpiryTime() > System.currentTimeMillis())
		{
			StringUtil.append(htmlContent, "<td width=170>Unable to create a clan.</td><td width=100 align=center>", format.format(activeChar.getClanCreateExpiryTime()), "</td>");
			penalty = true;
		}
		
		if ((activeChar.getClan() != null) && (activeChar.getClan().getCharPenaltyExpiryTime() > System.currentTimeMillis()))
		{
			StringUtil.append(htmlContent, "<td width=170>Unable to invite a clan member.</td><td width=100 align=center>", format.format(activeChar.getClan().getCharPenaltyExpiryTime()), "</td>");
			penalty = true;
		}
		
		if (!penalty)
		{
			htmlContent.append("<td width=170>No penalty is imposed.</td><td width=100 align=center></td>");
		}
		
		htmlContent.append("</tr></table><img src=\"L2UI.SquareWhite\" width=270 height=1></center></body></html>");
		
		final NpcHtmlMessage penaltyHtml = new NpcHtmlMessage(0);
		penaltyHtml.setHtml(htmlContent.toString());
		activeChar.sendPacket(penaltyHtml);
		
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}
