package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

/**
 * PkElfo
 */
 
public class GiveVitality implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.GIVE_VITALITY
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		for (L2Object target : targets)
		{
			if (target.isPlayer())
			{
				if (skill.hasEffects())
				{
					target.getActingPlayer().stopSkillEffects(skill.getId());
					skill.getEffects(activeChar, target.getActingPlayer());
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
					sm.addSkillName(skill);
					target.sendPacket(sm);
				}
				target.getActingPlayer().updateVitalityPoints((float) skill.getPower(), false, false);
				target.getActingPlayer().sendPacket(new UserInfo(target.getActingPlayer()));
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}