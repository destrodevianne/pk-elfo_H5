package handlers.admincommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.EtcStatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class AdminClanFull implements IAdminCommandHandler
{
	private static final String ADMIN_COMMANDS[] =
    {
		"admin_clanfull"
    };

	public AdminClanFull() {}

	@Override
    public boolean useAdminCommand(String command, L2PcInstance activeChar)
    {
		if (command.startsWith("admin_clanfull"))
		{
			try
			{
				adminAddClanSkill(activeChar);
				activeChar.sendMessage("Todas as skills de clan foram adicionadas.");
			}
			catch (Exception e)
			{
				activeChar.sendMessage("Use: //clanfull");
			}
		}
		return true;
	}

	private void adminAddClanSkill(L2PcInstance activeChar)
	{
		L2Object target = activeChar.getTarget();
		if (target == null)
		{
			target = activeChar;
		}
		L2PcInstance player = null;
		if (target instanceof L2PcInstance)
		{
			player = (L2PcInstance) target;
		}
		else
		{
			activeChar.sendPacket(new SystemMessage(SystemMessageId.INCORRECT_TARGET));
			return;
		}
		if (!player.isClanLeader())
		{
			player.sendPacket((new SystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER)).addString(player.getName()));
			return;
		}
		player.getClan().changeLevel(Config.CLAN_LEVEL);
		player.ClanSkills();
		player.sendPacket(new EtcStatusUpdate(activeChar));
	}

	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}