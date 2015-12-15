package handlers.itemhandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.model.ShotType;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.gameserver.util.Broadcast;

/**
 * Projeto PkElfo
 */
 
public class BeastSoulShot implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
			playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		final L2PcInstance activeOwner = playable.getActingPlayer();
		if (!activeOwner.hasSummon())
		{
			activeOwner.sendPacket(SystemMessageId.PETS_ARE_NOT_AVAILABLE_AT_THIS_TIME);
			return false;
		}
		
		if (activeOwner.getSummon().isDead())
		{
			activeOwner.sendPacket(SystemMessageId.SOULSHOTS_AND_SPIRITSHOTS_ARE_NOT_AVAILABLE_FOR_A_DEAD_PET);
			return false;
		}
		
		final int itemId = item.getItemId();
		final short shotConsumption = activeOwner.getSummon().getSoulShotsPerHit();
		final long shotCount = item.getCount();
		final SkillHolder[] skills = item.getItem().getSkills();
		
		if (skills == null)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": esta ausente as habilidades!");
			return false;
		}
		
		if (shotCount < shotConsumption)
		{
			// Not enough Soulshots to use.
			if (!activeOwner.disableAutoShot(itemId))
			{
				activeOwner.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS_FOR_PET);
			}
			return false;
		}
		
		if (activeOwner.getSummon().isChargedShot(ShotType.SOULSHOTS))
		{
			// SoulShots are already active.
			return false;
		}
		
		// If the player doesn't have enough beast soulshot remaining, remove any auto soulshot task.
		if (!activeOwner.destroyItemWithoutTrace("Consume", item.getObjectId(), shotConsumption, null, false))
		{
			if (!activeOwner.disableAutoShot(itemId))
			{
				activeOwner.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS_FOR_PET);
			}
			return false;
		}
		
		// Pet uses the power of spirit.
		activeOwner.sendPacket(SystemMessageId.PET_USE_SPIRITSHOT);
		activeOwner.getSummon().setChargedShot(ShotType.SOULSHOTS, true);
		Broadcast.toSelfAndKnownPlayersInRadius(activeOwner, new MagicSkillUse(activeOwner.getSummon(), activeOwner.getSummon(), skills[0].getSkillId(), skills[0].getSkillLvl(), 0, 0), 600);
		return true;
	}
}