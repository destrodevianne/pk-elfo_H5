package handlers.bypasshandlers;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.EtcStatusUpdate;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;
import pk.elfo.util.StringUtil;

/**
 * PkElfo
 */

public class RemoveDeathPenalty implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"remove_dp"
	};
	
	private static final int[] PEN_CLEAR_PRICE =
	{
		3600,
		8640,
		25200,
		50400,
		86400,
		144000,
		144000,
		144000
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		try
		{
			final int cmdChoice = Integer.parseInt(command.substring(10, 11).trim());
			final L2Npc npc = (L2Npc) target;
			switch (cmdChoice)
			{
				case 1:
					String filename = "data/html/default/30981-1.htm";
					NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
					html.setFile(activeChar.getHtmlPrefix(), filename);
					html.replace("%objectId%", String.valueOf(npc.getObjectId()));
					html.replace("%dp_price%", String.valueOf(PEN_CLEAR_PRICE[activeChar.getExpertiseLevel()]));
					activeChar.sendPacket(html);
					break;
				case 2:
					NpcHtmlMessage Reply = new NpcHtmlMessage(npc.getObjectId());
					final StringBuilder replyMSG = StringUtil.startAppend(400, "<html><body>Black Judge:<br>");
					
					if (activeChar.getDeathPenaltyBuffLevel() > 0)
					{
						if (activeChar.getAdena() >= PEN_CLEAR_PRICE[activeChar.getExpertiseLevel()])
						{
							if (!activeChar.reduceAdena("DeathPenality", PEN_CLEAR_PRICE[activeChar.getExpertiseLevel()], npc, true))
							{
								return false;
							}
							activeChar.setDeathPenaltyBuffLevel(activeChar.getDeathPenaltyBuffLevel() - 1);
							activeChar.sendPacket(SystemMessageId.DEATH_PENALTY_LIFTED);
							activeChar.sendPacket(new EtcStatusUpdate(activeChar));
							return true;
						}
						replyMSG.append("A ferida que voce recebeu de toque da morte esta muito profunda para ser curada pela quantidade de dinheiro que voce tem para me dar. Consiga mais dinheiro se quiser tirar a marca da morte de voce.");
					}
					else
					{
						replyMSG.append("Voce nao tem mais feridas de morte que necessitam de cura.<br>" + "Va em frente e lute, tanto para este mundo quanto para a sua propria gloria.");
					}
					
					replyMSG.append("</body></html>");
					Reply.setHtml(replyMSG.toString());
					activeChar.sendPacket(Reply);
					break;
			}
			return true;
		}
		catch (Exception e)
		{
			_log.info("Exception in " + getClass().getSimpleName());
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}