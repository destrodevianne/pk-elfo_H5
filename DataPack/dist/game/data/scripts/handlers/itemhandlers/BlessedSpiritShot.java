package handlers.itemhandlers;

import java.util.logging.Level;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.L2Weapon;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.items.type.L2ActionType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.util.Broadcast;

/**
 * PkElfo
 */
 
public class BlessedSpiritShot implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		L2PcInstance activeChar = (L2PcInstance) playable;
		L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		L2Weapon weaponItem = activeChar.getActiveWeaponItem();
		final SkillHolder[] skills = item.getItem().getSkills();
		
		int itemId = item.getItemId();
		
		if (skills == null)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": esta ausente as habilidades!");
			return false;
		}
		
		// Check if Blessed SpiritShot can be used
		if ((weaponInst == null) || (weaponItem == null) || (weaponItem.getSpiritShotCount() == 0))
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(SystemMessageId.CANNOT_USE_SPIRITSHOTS);
			}
			return false;
		}
		
		// Check if Blessed SpiritShot is already active (it can be charged over SpiritShot)
		if (activeChar.isChargedShot(ShotType.BLESSED_SPIRITSHOTS))
		{
			return false;
		}
		
		// Check for correct grade
		boolean gradeCheck = item.isEtcItem() && (item.getEtcItem().getDefaultAction() == L2ActionType.spiritshot) && (weaponInst.getItem().getItemGradeSPlus() == item.getItem().getItemGradeSPlus());
		
		if (!gradeCheck)
		{
			if (!activeChar.getAutoSoulShot().contains(itemId))
			{
				activeChar.sendPacket(SystemMessageId.SPIRITSHOTS_GRADE_MISMATCH);
			}
			
			return false;
		}
		
		// Consume Blessed SpiritShot if player has enough of them
		if (!Config.INFINITE_BLESSED_SPIRIT_SHOT && !activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), weaponItem.getSpiritShotCount(), null, false))
		{
			if (!activeChar.disableAutoShot(itemId))
			{
				activeChar.sendPacket(SystemMessageId.NOT_ENOUGH_SPIRITSHOTS);
			}
			return false;
		}
		
		// Send message to client
		activeChar.sendPacket(SystemMessageId.ENABLED_SPIRITSHOT);
		activeChar.setChargedShot(ShotType.BLESSED_SPIRITSHOTS, true);
		Broadcast.toSelfAndKnownPlayersInRadius(activeChar, new MagicSkillUse(activeChar, activeChar, skills[0].getSkillId(), skills[0].getSkillLvl(), 0, 0), 600);
		return true;
	}
}