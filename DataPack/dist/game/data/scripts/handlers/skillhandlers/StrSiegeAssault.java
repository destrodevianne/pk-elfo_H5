package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.stats.Formulas;

/**
 * PkElfo
 */
 
public class StrSiegeAssault implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.STRSIEGEASSAULT
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2PcInstance player = activeChar.getActingPlayer();
		
		if (!player.isRidingStrider())
		{
			return;
		}
		if (!player.getTarget().isDoor())
		{
			return;
		}
		
		Castle castle = CastleManager.getInstance().getCastle(player);
		Fort fort = FortManager.getInstance().getFort(player);
		
		if ((castle == null) && (fort == null))
		{
			return;
		}
		
		if (castle != null)
		{
			if (!player.checkIfOkToUseStriderSiegeAssault(castle))
			{
				return;
			}
		}
		else
		{
			if (!player.checkIfOkToUseStriderSiegeAssault(fort))
			{
				return;
			}
		}
		
		try
		{
			// damage calculation
			int damage = 0;
			boolean ss = skill.useSoulShot() && activeChar.isChargedShot(ShotType.SOULSHOTS);
			
			for (L2Character target : (L2Character[]) targets)
			{
				if (activeChar.isPlayer() && target.isPlayer() && target.getActingPlayer().isFakeDeath())
				{
					target.stopFakeDeath(true);
				}
				else if (target.isDead())
				{
					continue;
				}
				
				boolean dual = activeChar.isUsingDualWeapon();
				byte shld = Formulas.calcShldUse(activeChar, target, skill);
				boolean crit = Formulas.calcCrit(activeChar.getCriticalHit(target, skill), true, target);
				
				if (!crit && ((skill.getCondition() & L2Skill.COND_CRIT) != 0))
				{
					damage = 0;
				}
				else
				{
					damage = skill.isStaticDamage() ? (int) skill.getPower() : (int) Formulas.calcPhysDam(activeChar, target, skill, shld, crit, dual, ss);
				}
				
				if (damage > 0)
				{
					target.reduceCurrentHp(damage, activeChar, skill);
					
					activeChar.sendDamageMessage(target, damage, false, false, false);
				}
				else
				{
					activeChar.sendMessage(skill.getName() + " falhou.");
				}
			}
			activeChar.setChargedShot(ShotType.SOULSHOTS, false);
		}
		catch (Exception e)
		{
			player.sendMessage("Erro usando siege assault:" + e);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	public static void main(String[] args)
	{
		new StrSiegeAssault();
	}
}