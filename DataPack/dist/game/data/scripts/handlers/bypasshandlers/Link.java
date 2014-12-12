package handlers.bypasshandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * Projeto PkElfo
 */

public class Link implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"Link"
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
			String path = command.substring(5).trim();
			if (path.indexOf("..") != -1)
			{
				return false;
			}
			String filename = "data/html/" + path;
			NpcHtmlMessage html = new NpcHtmlMessage(((L2Npc) target).getObjectId());
			html.setFile(activeChar.getHtmlPrefix(), filename);
			html.replace("%objectId%", String.valueOf(((L2Npc) target).getObjectId()));
			activeChar.sendPacket(html);
			return true;
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception in " + getClass().getSimpleName(), e);
		}
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}