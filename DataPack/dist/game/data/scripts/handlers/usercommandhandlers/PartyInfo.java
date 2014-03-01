package handlers.usercommandhandlers;

import pk.elfo.gameserver.handler.IUserCommandHandler;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */

public class PartyInfo implements IUserCommandHandler
{
	private static final int[] COMMAND_IDS =
	{
		81
	};
	
	@Override
	public boolean useUserCommand(int id, L2PcInstance activeChar)
	{
		if (id != COMMAND_IDS[0])
		{
			return false;
		}
		
		activeChar.sendPacket(SystemMessageId.PARTY_INFORMATION);
		if (activeChar.isInParty())
		{
			final L2Party party = activeChar.getParty();
			switch (party.getLootDistribution())
			{
				case L2Party.ITEM_LOOTER:
					activeChar.sendPacket(SystemMessageId.LOOTING_FINDERS_KEEPERS);
					break;
				case L2Party.ITEM_ORDER:
					activeChar.sendPacket(SystemMessageId.LOOTING_BY_TURN);
					break;
				case L2Party.ITEM_ORDER_SPOIL:
					activeChar.sendPacket(SystemMessageId.LOOTING_BY_TURN_INCLUDE_SPOIL);
					break;
				case L2Party.ITEM_RANDOM:
					activeChar.sendPacket(SystemMessageId.LOOTING_RANDOM);
					break;
				case L2Party.ITEM_RANDOM_SPOIL:
					activeChar.sendPacket(SystemMessageId.LOOTING_RANDOM_INCLUDE_SPOIL);
					break;
			}
			
			if (!party.isLeader(activeChar))
			{
				final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PARTY_LEADER_C1);
				sm.addPcName(party.getLeader());
				activeChar.sendPacket(sm);
			}
			activeChar.sendMessage("Membros: " + party.getMemberCount() + "/9"); // TODO: Custom?
		}
		activeChar.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
		return true;
	}
	
	@Override
	public int[] getUserCommandList()
	{
		return COMMAND_IDS;
	}
}