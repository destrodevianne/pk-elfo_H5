package handlers.admincommandhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEventTeleporter;
import pk.elfo.gameserver.model.entity.TvTRoundManager;

/**
 * Projeto PkElfo
 */

public class AdminTvTRoundEvent implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_tvtround_add",
		"admin_tvtround_remove",
		"admin_tvtround_advance"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_tvtround_add"))
		{
			L2Object target = activeChar.getTarget();
			
			if (!(target instanceof L2PcInstance))
			{
				activeChar.sendMessage("You should select a player!");
				return true;
			}
			
			add(activeChar, (L2PcInstance) target);
		}
		else if (command.equals("admin_tvtround_remove"))
		{
			L2Object target = activeChar.getTarget();
			
			if (!(target instanceof L2PcInstance))
			{
				activeChar.sendMessage("You should select a player!");
				return true;
			}
			
			remove(activeChar, (L2PcInstance) target);
		}
		else if (command.equals("admin_tvtround_advance"))
		{
			TvTRoundManager.getInstance().skipDelay();
		}
		
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void add(L2PcInstance activeChar, L2PcInstance playerInstance)
	{
		if (TvTRoundEvent.isPlayerParticipant(playerInstance.getObjectId()))
		{
			activeChar.sendMessage("Player already participated in the event!");
			return;
		}
		
		if (!TvTRoundEvent.addParticipant(playerInstance))
		{
			activeChar.sendMessage("Player instance could not be added, it seems to be null!");
			return;
		}
		
		if (TvTRoundEvent.isStarted())
		{
			new TvTRoundEventTeleporter(playerInstance, TvTRoundEvent.getParticipantTeamCoordinates(playerInstance.getObjectId()), true, false);
		}
	}
	
	private void remove(L2PcInstance activeChar, L2PcInstance playerInstance)
	{
		if (!TvTRoundEvent.removeParticipant(playerInstance.getObjectId()))
		{
			activeChar.sendMessage("Player is not part of the event!");
			return;
		}
		
		new TvTRoundEventTeleporter(playerInstance, Config.TVT_ROUND_EVENT_PARTICIPATION_NPC_COORDINATES, true, true);
	}
}
