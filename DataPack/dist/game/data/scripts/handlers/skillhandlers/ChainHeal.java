package handlers.skillhandlers;

import java.util.List;
import java.util.Map;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.handler.SkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.StatusUpdate;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.ValueSortMap;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * Projeto PkElfo
 */
 
public class ChainHeal implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.CHAIN_HEAL
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		// check for other effects
		ISkillHandler handler = SkillHandler.getInstance().getHandler(L2SkillType.BUFF);
		
		if (handler != null)
		{
			handler.useSkill(activeChar, skill, targets);
		}
		
		SystemMessage sm;
		double amount = 0;
		
		L2Character[] characters = getTargetsToHeal((L2Character[]) targets);
		double power = skill.getPower();
		
		// Get top 10 most damaged and iterate the heal over them
		for (L2Character character : characters)
		{
			// 1505 - sublime self sacrifice
			if ((character.isDead() || character.isInvul()) && (skill.getId() != 1505))
			{
				continue;
			}
			
			// Cursed weapon owner can't heal or be healed
			if (character != activeChar)
			{
				if (character.isPlayer() && character.getActingPlayer().isCursedWeaponEquipped())
				{
					continue;
				}
			}
			
			if (power == 100.)
			{
				amount = character.getMaxHp();
			}
			else
			{
				amount = (character.getMaxHp() * power) / 100.0;
			}
			
			amount = Math.min(amount, character.getMaxRecoverableHp() - character.getCurrentHp());
			
			if (amount < 0)
			{
				amount = 0;
			}
			
			character.setCurrentHp(amount + character.getCurrentHp());
			
			if (activeChar != character)
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_C1);
				sm.addCharName(activeChar);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED);
			}
			sm.addNumber((int) amount);
			character.sendPacket(sm);
			
			StatusUpdate su = new StatusUpdate(character);
			su.addAttribute(StatusUpdate.CUR_HP, (int) character.getCurrentHp());
			character.sendPacket(su);
			
			power -= 3;
		}
	}
	
	private L2Character[] getTargetsToHeal(L2Character[] targets)
	{
		Map<L2Character, Double> tmpTargets = new FastMap<>();
		List<L2Character> sortedListToReturn = new FastList<>();
		int curTargets = 0;
		
		for (L2Character target : targets)
		{
			// 1505 - sublime self sacrifice
			if (((target == null) || target.isDead() || target.isInvul()))
			{
				continue;
			}
			
			if (target.getMaxHp() == target.getCurrentHp())
			{
				continue;
			}
			
			double hpPercent = target.getCurrentHp() / target.getMaxHp();
			tmpTargets.put(target, hpPercent);
			
			curTargets++;
			if (curTargets >= 10)
			{
				break;
			}
		}
		
		// Sort in ascending order then add the values to the list
		ValueSortMap.sortMapByValue(tmpTargets, true);
		sortedListToReturn.addAll(tmpTargets.keySet());
		
		return sortedListToReturn.toArray(new L2Character[sortedListToReturn.size()]);
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}