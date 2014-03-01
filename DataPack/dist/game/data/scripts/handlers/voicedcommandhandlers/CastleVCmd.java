package handlers.voicedcommandhandlers;

import pk.elfo.gameserver.handler.IVoicedCommandHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * PkElfo
 */

public class CastleVCmd implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"opendoors",
		"closedoors",
		"ridewyvern"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String params)
	{
		switch (command)
		{
			case "opendoors":
				if (!params.equals("castle"))
				{
					activeChar.sendMessage("Somente portas do castelo podem ser abertas.");
					return false;
				}
				
				if (!activeChar.isClanLeader())
				{
					activeChar.sendPacket(SystemMessageId.ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
					return false;
				}
				
				final L2DoorInstance door = (L2DoorInstance) activeChar.getTarget();
				if (door == null)
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
					return false;
				}
				
				final Castle castle = CastleManager.getInstance().getCastleById(activeChar.getClan().getCastleId());
				if (castle == null)
				{
					activeChar.sendMessage("Seu clan nao possui um castelo.");
					return false;
				}
				
				if (castle.getSiege().getIsInProgress())
				{
					activeChar.sendPacket(SystemMessageId.GATES_NOT_OPENED_CLOSED_DURING_SIEGE);
					return false;
				}
				
				if (castle.checkIfInZone(door.getX(), door.getY(), door.getZ()))
				{
					activeChar.sendPacket(SystemMessageId.GATE_IS_OPENING);
					door.openMe();
				}
				break;
			case "closedoors":
				if (!params.equals("castle"))
				{
					activeChar.sendMessage("Somente portas do castelo podem ser fechadas.");
					return false;
				}
				if (!activeChar.isClanLeader())
				{
					activeChar.sendPacket(SystemMessageId.ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
					return false;
				}
				final L2DoorInstance door2 = (L2DoorInstance) activeChar.getTarget();
				if (door2 == null)
				{
					activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
					return false;
				}
				final Castle castle2 = CastleManager.getInstance().getCastleById(activeChar.getClan().getCastleId());
				if (castle2 == null)
				{
					activeChar.sendMessage("Seu clan nao possui um castelo.");
					return false;
				}
				
				if (castle2.getSiege().getIsInProgress())
				{
					activeChar.sendPacket(SystemMessageId.GATES_NOT_OPENED_CLOSED_DURING_SIEGE);
					return false;
				}
				
				if (castle2.checkIfInZone(door2.getX(), door2.getY(), door2.getZ()))
				{
					activeChar.sendMessage("O portao esta sendo fechado.");
					door2.closeMe();
				}
				break;
			case "ridewyvern":
				if (activeChar.isClanLeader() && (activeChar.getClan().getCastleId() > 0))
				{
					activeChar.mount(12621, 0, true);
				}
				break;
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}