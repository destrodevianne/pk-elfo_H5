package handlers.bypasshandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2OlympiadManagerInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.model.olympiad.OlympiadGameManager;
import pk.elfo.gameserver.model.olympiad.OlympiadGameTask;
import pk.elfo.gameserver.model.olympiad.OlympiadManager;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ExOlympiadMatchList;

/**
 * PkElfo
 */

public class OlympiadObservation implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"watchmatch",
		"arenachange"
	};
	
	@Override
	public final boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		try
		{
			final L2Npc olymanager = activeChar.getLastFolkNPC();
			
			if (command.startsWith(COMMANDS[0])) // list
			{
				activeChar.sendPacket(new ExOlympiadMatchList());
			}
			else
			{
				if ((olymanager == null) || !(olymanager instanceof L2OlympiadManagerInstance))
				{
					return false;
				}
				
				if (!activeChar.inObserverMode() && !activeChar.isInsideRadius(olymanager, 300, false, false))
				{
					return false;
				}
				
				if (OlympiadManager.getInstance().isRegisteredInComp(activeChar))
				{
					activeChar.sendPacket(SystemMessageId.WHILE_YOU_ARE_ON_THE_WAITING_LIST_YOU_ARE_NOT_ALLOWED_TO_WATCH_THE_GAME);
					return false;
				}
				
				if (!Olympiad.getInstance().inCompPeriod())
				{
					activeChar.sendPacket(SystemMessageId.THE_OLYMPIAD_GAME_IS_NOT_CURRENTLY_IN_PROGRESS);
					return false;
				}
				
				if (!TvTEvent.isInactive() && TvTEvent.isPlayerParticipant(activeChar.getObjectId()))
				{
					activeChar.sendMessage("Voce nao pode observar os jogos, enquanto estiver registrado para o TvT");
					return false;
				}

				final int arenaId = Integer.parseInt(command.substring(12).trim());
				final OlympiadGameTask nextArena = OlympiadGameManager.getInstance().getOlympiadTask(arenaId);
				if (nextArena != null)
				{
					activeChar.enterOlympiadObserverMode(nextArena.getZone().getSpawns().get(0), arenaId);
					activeChar.setInstanceId(OlympiadGameManager.getInstance().getOlympiadTask(arenaId).getZone().getInstanceId());
				}
			}
			return true;
			
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public final String[] getBypassList()
	{
		return COMMANDS;
	}
}