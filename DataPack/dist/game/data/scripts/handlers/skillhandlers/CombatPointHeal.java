package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.handler.SkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 */
 
public class CombatPointHeal implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.COMBATPOINTHEAL
	};
	
	@Override
	public void useSkill(L2Character actChar, L2Skill skill, L2Object[] targets)
	{
		// check for other effects
		ISkillHandler handler = SkillHandler.getInstance().getHandler(L2SkillType.BUFF);
		
		if (handler != null)
		{
			handler.useSkill(actChar, skill, targets);
		}
		
		for (L2Character target : (L2Character[]) targets)
		{
			if (target.isInvul())
			{
				continue;
			}
			
			double cp = skill.getPower();
			
			cp = Math.min(cp, target.getMaxRecoverableCp() - target.getCurrentCp());
			
			// Prevent negative amounts
			if (cp < 0)
			{
				cp = 0;
			}
			
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CP_WILL_BE_RESTORED);
			sm.addNumber((int) cp);
			target.sendPacket(sm);
			target.setCurrentCp(cp + target.getCurrentCp());
			StatusUpdate sump = new StatusUpdate(target);
			sump.addAttribute(StatusUpdate.CUR_CP, (int) target.getCurrentCp());
			target.sendPacket(sump);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}