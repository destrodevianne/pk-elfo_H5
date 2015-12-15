package handlers.skillhandlers;

import pk.elfo.Config;
import pk.elfo.gameserver.GeoData;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Formulas;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.FlyToLocation;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.network.serverpackets.ValidateLocation;
import pk.elfo.gameserver.network.serverpackets.FlyToLocation.FlyType;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */
 
public class InstantJump implements ISkillHandler
{
	
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.INSTANT_JUMP
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		L2Character target = (L2Character) targets[0];
		
		if (Formulas.calcPhysicalSkillEvasion(target, skill))
		{
			if (activeChar.isPlayer())
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_DODGES_ATTACK);
				sm.addString(target.getName());
				activeChar.getActingPlayer().sendPacket(sm);
			}
			if (target.isPlayer())
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_C1_ATTACK);
				sm.addString(activeChar.getName());
				target.getActingPlayer().sendPacket(sm);
			}
			return;
		}
		
		int x = 0, y = 0, z = 0;
		int px = target.getX();
		int py = target.getY();
		double ph = Util.convertHeadingToDegree(target.getHeading());
		
		ph += 180;
		if (ph > 360)
		{
			ph -= 360;
		}
		
		ph = (Math.PI * ph) / 180;
		x = (int) (px + (25 * Math.cos(ph)));
		y = (int) (py + (25 * Math.sin(ph)));
		z = target.getZ();
		
		Location loc = new Location(x, y, z);
		
		if (Config.GEODATA > 0)
		{
			loc = GeoData.getInstance().moveCheck(activeChar.getX(), activeChar.getY(), activeChar.getZ(), x, y, z, activeChar.getInstanceId());
		}
		
		activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
		activeChar.broadcastPacket(new FlyToLocation(activeChar, loc.getX(), loc.getY(), loc.getZ(), FlyType.DUMMY));
		activeChar.abortAttack();
		activeChar.abortCast();
		activeChar.setXYZ(loc.getX(), loc.getY(), loc.getZ());
		activeChar.broadcastPacket(new ValidateLocation(activeChar));
		
		if (skill.hasEffects())
		{
			if (Formulas.calcSkillReflect(target, skill) == Formulas.SKILL_REFLECT_SUCCEED)
			{
				activeChar.stopSkillEffects(skill.getId());
				skill.getEffects(target, activeChar);
				
				// SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
				// sm.addSkillName(skill);
				// activeChar.sendPacket(sm);
			}
			else
			{
				// activate attacked effects, if any
				target.stopSkillEffects(skill.getId());
				skill.getEffects(activeChar, target);
			}
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}