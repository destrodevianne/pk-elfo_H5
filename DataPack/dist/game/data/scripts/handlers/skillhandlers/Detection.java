package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * Projeto PkElfo
 */
 
public class Detection implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.DETECTION
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		final boolean hasParty;
		final boolean hasClan;
		final boolean hasAlly;
		final L2PcInstance player = activeChar.getActingPlayer();
		if (player != null)
		{
			hasParty = player.isInParty();
			hasClan = player.getClanId() > 0;
			hasAlly = player.getAllyId() > 0;
			
			for (L2PcInstance target : activeChar.getKnownList().getKnownPlayersInRadius(skill.getSkillRadius()))
			{
				if ((target != null) && target.getAppearance().getInvisible())
				{
					if (hasParty && (target.getParty() != null) && (player.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId()))
					{
						continue;
					}
					if (hasClan && (player.getClanId() == target.getClanId()))
					{
						continue;
					}
					if (hasAlly && (player.getAllyId() == target.getAllyId()))
					{
						continue;
					}
					
					L2Effect eHide = target.getFirstEffect(L2EffectType.HIDE);
					if (eHide != null)
					{
						eHide.exit();
					}
				}
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}