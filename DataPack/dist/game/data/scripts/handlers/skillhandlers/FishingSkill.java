package handlers.skillhandlers;

import pk.elfo.gameserver.datatables.FishingRodsData;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.fishing.L2Fishing;
import pk.elfo.gameserver.model.fishing.L2FishingRod;
import pk.elfo.gameserver.model.items.L2Weapon;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;

/**
 * Projeto PkElfo
 */
 
public class FishingSkill implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.PUMPING,
		L2SkillType.REELING
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		L2PcInstance player = activeChar.getActingPlayer();
		L2Fishing fish = player.getFishCombat();
		if (fish == null)
		{
			if (skill.getSkillType() == L2SkillType.PUMPING)
			{
				// Pumping skill is available only while fishing
				player.sendPacket(SystemMessageId.CAN_USE_PUMPING_ONLY_WHILE_FISHING);
			}
			else if (skill.getSkillType() == L2SkillType.REELING)
			{
				// Reeling skill is available only while fishing
				player.sendPacket(SystemMessageId.CAN_USE_REELING_ONLY_WHILE_FISHING);
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		L2Weapon weaponItem = player.getActiveWeaponItem();
		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		if ((weaponInst == null) || (weaponItem == null))
		{
			return;
		}
		int SS = 1;
		int pen = 0;
		if (activeChar.isChargedShot(ShotType.FISH_SOULSHOTS))
		{
			SS = 2;
		}
		L2FishingRod fishingRod = FishingRodsData.getInstance().getFishingRod(weaponItem.getItemId());
		double gradeBonus = fishingRod.getFishingRodLevel() * 0.1; // TODO: Check this formula (is guessed)
		final L2Skill expertiseSkill = SkillTable.getInstance().getInfo(1315, player.getSkillLevel(1315));
		int dmg = (int) ((fishingRod.getFishingRodDamage() + expertiseSkill.getPower() + skill.getPower()) * gradeBonus * SS);
		// Penalty 5% less damage dealt
		if (player.getSkillLevel(1315) <= (skill.getLevel() - 2)) // 1315 - Fish Expertise
		{
			player.sendPacket(SystemMessageId.REELING_PUMPING_3_LEVELS_HIGHER_THAN_FISHING_PENALTY);
			pen = (int) (dmg * 0.05);
			dmg = dmg - pen;
		}
		if (SS > 1)
		{
			weaponInst.setChargedShot(ShotType.FISH_SOULSHOTS, false);
		}
		if (skill.getSkillType() == L2SkillType.REELING)
		{
			fish.useReeling(dmg, pen);
		}
		else
		{
			fish.usePumping(dmg, pen);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}