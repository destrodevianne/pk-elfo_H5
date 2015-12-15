package handlers.itemhandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.L2Weapon;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.items.type.L2ActionType;
import pk.elfo.gameserver.model.items.type.L2WeaponType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.util.Broadcast;

/**
 * Projeto PkElfo
 */
 
public class FishShots implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		final L2ItemInstance weaponInst = activeChar.getActiveWeaponInstance();
		final L2Weapon weaponItem = activeChar.getActiveWeaponItem();
		
		if ((weaponInst == null) || (weaponItem.getItemType() != L2WeaponType.FISHINGROD))
		{
			return false;
		}
		
		if (activeChar.isChargedShot(ShotType.FISH_SOULSHOTS))
		{
			return false;
		}
		
		final long count = item.getCount();
		final SkillHolder[] skills = item.getItem().getSkills();
		
		if (skills == null)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": esta ausente as habilidades!");
			return false;
		}
		
		boolean gradeCheck = item.isEtcItem() && (item.getEtcItem().getDefaultAction() == L2ActionType.fishingshot) && (weaponInst.getItem().getItemGradeSPlus() == item.getItem().getItemGradeSPlus());
		
		if (!gradeCheck)
		{
			activeChar.sendPacket(SystemMessageId.WRONG_FISHINGSHOT_GRADE);
			return false;
		}
		
		if (count < 1)
		{
			return false;
		}
		
		activeChar.setChargedShot(ShotType.FISH_SOULSHOTS, true);
		activeChar.destroyItemWithoutTrace("Consume", item.getObjectId(), 1, null, false);
		L2Object oldTarget = activeChar.getTarget();
		activeChar.setTarget(activeChar);
		Broadcast.toSelfAndKnownPlayers(activeChar, new MagicSkillUse(activeChar, skills[0].getSkillId(), skills[0].getSkillLvl(), 0, 0));
		activeChar.setTarget(oldTarget);
		return true;
	}
}