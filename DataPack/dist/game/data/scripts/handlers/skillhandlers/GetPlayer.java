package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.serverpackets.ValidateLocation;
import pk.elfo.util.Rnd;

/**
 * Projeto PkElfo
 */
 
public class GetPlayer implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.GET_PLAYER
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (activeChar.isAlikeDead())
		{
			return;
		}
		for (L2Object target : targets)
		{
			if (target.isPlayer())
			{
				L2PcInstance trg = target.getActingPlayer();
				if (trg.isAlikeDead())
				{
					continue;
				}
				// trg.teleToLocation(activeChar.getX(), activeChar.getY(), activeChar.getZ(), true);
				trg.setXYZ(activeChar.getX() + Rnd.get(-10, 10), activeChar.getY() + Rnd.get(-10, 10), activeChar.getZ());
				trg.sendPacket(new ValidateLocation(trg));
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}