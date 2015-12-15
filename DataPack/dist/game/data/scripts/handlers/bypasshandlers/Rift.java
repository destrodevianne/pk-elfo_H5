package handlers.bypasshandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.instancemanager.DimensionalRiftManager;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Projeto PkElfo
 */

public class Rift implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"enterrift",
		"changeriftroom",
		"exitrift"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		if (command.toLowerCase().startsWith(COMMANDS[0])) // EnterRift
		{
			try
			{
				Byte b1 = Byte.parseByte(command.substring(10)); // Selected Area: Recruit, Soldier etc
				DimensionalRiftManager.getInstance().start(activeChar, b1, (L2Npc) target);
				return true;
			}
			catch (Exception e)
			{
				_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
			}
		}
		else
		{
			final boolean inRift = activeChar.isInParty() && activeChar.getParty().isInDimensionalRift();
			
			if (command.toLowerCase().startsWith(COMMANDS[1])) // ChangeRiftRoom
			{
				if (inRift)
				{
					activeChar.getParty().getDimensionalRift().manualTeleport(activeChar, (L2Npc) target);
				}
				else
				{
					DimensionalRiftManager.getInstance().handleCheat(activeChar, (L2Npc) target);
				}
				
				return true;
			}
			else if (command.toLowerCase().startsWith(COMMANDS[2])) // ExitRift
			{
				if (inRift)
				{
					activeChar.getParty().getDimensionalRift().manualExitRift(activeChar, (L2Npc) target);
				}
				else
				{
					DimensionalRiftManager.getInstance().handleCheat(activeChar, (L2Npc) target);
				}
				
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}