package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2ControllableAirShipInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;

/**
 * Projeto PkElfo
 */
 
public class RefuelAirShip implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.REFUEL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		final L2AirShipInstance ship = activeChar.getActingPlayer().getAirShip();
		if ((ship == null) || !(ship instanceof L2ControllableAirShipInstance) || (ship.getFuel() >= ship.getMaxFuel()))
		{
			return;
		}
		ship.setFuel(ship.getFuel() + (int) skill.getPower());
		ship.updateAbnormalEffect(); // broadcast new fuel
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}