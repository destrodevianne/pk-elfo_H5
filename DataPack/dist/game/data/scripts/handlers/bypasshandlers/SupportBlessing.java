package handlers.bypasshandlers;

import pk.elfo.gameserver.datatables.SkillTable.FrequentSkill;
import pk.elfo.gameserver.handler.IBypassHandler;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class SupportBlessing implements IBypassHandler
{
	private static final String[] COMMANDS =
	{
		"GiveBlessing"
	};
	
	@Override
	public boolean useBypass(String command, L2PcInstance activeChar, L2Character target)
	{
		if (!target.isNpc())
		{
			return false;
		}
		
		final L2Npc npc = (L2Npc) target;
		
		// If the player is too high level, display a message and return
		if ((activeChar.getLevel() > 39) || (activeChar.getClassId().level() >= 2))
		{
			npc.showChatWindow(activeChar, "data/html/default/SupportBlessingHighLevel.htm");
			return true;
		}
		npc.setTarget(activeChar);
		npc.doCast(FrequentSkill.BLESSING_OF_PROTECTION.getSkill());
		return false;
	}
	
	@Override
	public String[] getBypassList()
	{
		return COMMANDS;
	}
}