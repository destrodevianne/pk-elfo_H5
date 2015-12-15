package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Projeto PkElfo
 */
 
public class TransformDispel implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.TRANSFORMDISPEL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2PcInstance pc = activeChar.getActingPlayer();
		
		if (pc.isAlikeDead() || pc.isCursedWeaponEquipped())
		{
			return;
		}
		
		if (pc.isTransformed() || pc.isInStance())
		{
			if (pc.isFlyingMounted() && !pc.isInsideZone(ZoneId.LANDING))
			{
				pc.sendPacket(SystemMessageId.TOO_HIGH_TO_PERFORM_THIS_ACTION);
			}
			else
			{
				pc.stopTransformation(true);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}