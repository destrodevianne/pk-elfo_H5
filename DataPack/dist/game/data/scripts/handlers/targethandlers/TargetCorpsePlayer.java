package handlers.targethandlers;

import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2PetInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;
import javolution.util.FastList;

/**
 * PkElfo
 */

public class TargetCorpsePlayer implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if ((target != null) && target.isDead())
		{
			final L2PcInstance player;
			if (activeChar.isPlayer())
			{
				player = activeChar.getActingPlayer();
			}
			else
			{
				player = null;
			}
			
			final L2PcInstance targetPlayer;
			if (target.isPlayer())
			{
				targetPlayer = target.getActingPlayer();
			}
			else
			{
				targetPlayer = null;
			}
			
			final L2PetInstance targetPet;
			if (target.isPet())
			{
				targetPet = (L2PetInstance) target;
			}
			else
			{
				targetPet = null;
			}
			
			if ((player != null) && ((targetPlayer != null) || (targetPet != null)))
			{
				boolean condGood = true;
				
				if (skill.getSkillType() == L2SkillType.RESURRECT)
				{
					if (targetPlayer != null)
					{
						// check target is not in a active siege zone
						if (targetPlayer.isInsideZone(ZoneId.SIEGE) && !targetPlayer.isInSiege())
						{
							condGood = false;
							activeChar.sendPacket(SystemMessageId.CANNOT_BE_RESURRECTED_DURING_SIEGE);
						}
						
						if (targetPlayer.isFestivalParticipant()) // Check to see if the current player target is in a festival.
						{
							condGood = false;
							activeChar.sendMessage("Voce nao pode ressuscitar os participantes de um festival.");
						}
						if (targetPlayer.isReviveRequested())
						{
							if (targetPlayer.isRevivingPet())
							{
								player.sendPacket(SystemMessageId.MASTER_CANNOT_RES); // While a pet is attempting to resurrect, it cannot help in resurrecting its master.
							}
							else
							{
								player.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED); // Resurrection is already been proposed.
							}
							condGood = false;
						}
					}
					else if (targetPet != null)
					{
						if (targetPet.getOwner() != player)
						{
							if (targetPet.getOwner().isReviveRequested())
							{
								if (targetPet.getOwner().isRevivingPet())
								{
									player.sendPacket(SystemMessageId.RES_HAS_ALREADY_BEEN_PROPOSED); // Resurrection is already been proposed.
								}
								else
								{
									player.sendPacket(SystemMessageId.CANNOT_RES_PET2); // A pet cannot be resurrected while it's owner is in the process of resurrecting.
								}
								condGood = false;
							}
						}
					}
				}
				
				if (condGood)
				{
					if (!onlyFirst)
					{
						targetList.add(target);
						return targetList.toArray(new L2Object[targetList.size()]);
					}
					return new L2Character[]
					{
						target
					};
				}
			}
		}
		activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
		return _emptyTargetList;
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_CORPSE_PLAYER;
	}
}