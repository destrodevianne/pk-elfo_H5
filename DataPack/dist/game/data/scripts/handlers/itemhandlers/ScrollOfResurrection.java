package handlers.itemhandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.IItemHandler;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PetInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.entity.TvTRoundEvent;
import pk.elfo.gameserver.model.holders.SkillHolder;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class ScrollOfResurrection implements IItemHandler
{
	@Override
	public boolean useItem(L2Playable playable, L2ItemInstance item, boolean forceUse)
	{
		if (!playable.isPlayer())
		{
		playable.sendPacket(SystemMessageId.ITEM_NOT_FOR_PETS);
			return false;
		}
		
		if (!TvTEvent.onScrollUse(playable.getObjectId()))
		{
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (!TvTRoundEvent.onScrollUse(playable.getObjectId()))
		{
			playable.sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		final L2PcInstance activeChar = playable.getActingPlayer();
		if (activeChar.isSitting())
		{
			activeChar.sendPacket(SystemMessageId.CANT_MOVE_SITTING);
			return false;
		}
		
		if (activeChar.isMovementDisabled())
		{
			return false;
		}
		
		final int itemId = item.getItemId();
		final boolean petScroll = (itemId == 6387);
		final SkillHolder[] skills = item.getItem().getSkills();
		
		if (skills == null)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": esta ausente as habilidades!");
			return false;
		}
		
		// SoR Animation section
		final L2Character target = (L2Character) activeChar.getTarget();
		if ((target == null) || !target.isDead())
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return false;
		}
		
		L2PcInstance targetPlayer = null;
		if (target instanceof L2PcInstance)
		{
			targetPlayer = (L2PcInstance) target;
		}
		
		L2PetInstance targetPet = null;
		if (target instanceof L2PetInstance)
		{
			targetPet = (L2PetInstance) target;
		}
		
		if ((targetPlayer != null) || (targetPet != null))
		{
			boolean condGood = true;
			
			// check target is not in a active siege zone
			Castle castle = null;
			
			if (targetPlayer != null)
			{
				castle = CastleManager.getInstance().getCastle(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
			}
			else if (targetPet != null)
			{
				castle = CastleManager.getInstance().getCastle(targetPet.getOwner().getX(), targetPet.getOwner().getY(), targetPet.getOwner().getZ());
			}
			
			if ((castle != null) && castle.getSiege().getIsInProgress())
			{
				condGood = false;
				activeChar.sendPacket(SystemMessageId.CANNOT_BE_RESURRECTED_DURING_SIEGE);
			}
			
			if (targetPet != null)
			{
				if (targetPet.getOwner() != activeChar)
				{
					if (targetPet.getOwner().isReviveRequested())
					{
						if (targetPet.getOwner().isRevivingPet())
						{
							activeChar.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED); // Resurrection is already been proposed.
						}
						else
						{
							activeChar.sendPacket(SystemMessageId.CANNOT_RES_PET2); // A pet cannot be resurrected while it's owner is in the process of resurrecting.
						}
						condGood = false;
					}
				}
			}
			else if (targetPlayer != null)
			{
				if (targetPlayer.isFestivalParticipant()) // Check to see if the current player target is in a festival.
				{
					condGood = false;
					activeChar.sendMessage("Voce nao pode ressuscitar os participantes de um festival.");
				}
				if (targetPlayer.isReviveRequested())
				{
					if (targetPlayer.isRevivingPet())
					{
						activeChar.sendPacket(SystemMessageId.MASTER_CANNOT_RES); // While a pet is attempting to resurrect, it cannot help in resurrecting its master.
					}
					else
					{
						activeChar.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED); // Resurrection is already been proposed.
					}
					condGood = false;
				}
				else if (petScroll)
				{
					condGood = false;
					activeChar.sendMessage("Voce nao tem o pergaminho correto");
				}
			}
			
			if (condGood)
			{
				if (!activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
				{
					return false;
				}
				
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED);
				sm.addItemName(item);
				activeChar.sendPacket(sm);
				
				for (SkillHolder sk : skills)
				{
					activeChar.useMagic(sk.getSkill(), true, true);
				}
				return true;
			}
		}
		return false;
	}
}