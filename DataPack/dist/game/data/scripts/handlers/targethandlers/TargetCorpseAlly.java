package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.model.zone.ZoneId;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetCorpseAlly implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		if (activeChar.isPlayable())
		{
			final L2PcInstance player = activeChar.getActingPlayer();
			
			if (player == null)
			{
				return _emptyTargetList;
			}
			
			if (player.isInOlympiadMode())
			{
				return new L2Character[]
				{
					player
				};
			}
			
			final int radius = skill.getSkillRadius();
			
			if (L2Skill.addSummon(activeChar, player, radius, true))
			{
				targetList.add(player.getSummon());
			}
			
			if (player.getClan() != null)
			{
				// Get all visible objects in a spherical area near the L2Character
				final Collection<L2PcInstance> objs = activeChar.getKnownList().getKnownPlayersInRadius(radius);
				{
					for (L2PcInstance obj : objs)
					{
						if (obj == null)
						{
							continue;
						}
						if (((obj.getAllyId() == 0) || (obj.getAllyId() != player.getAllyId())) && ((obj.getClan() == null) || (obj.getClanId() != player.getClanId())))
						{
							continue;
						}
						
						if (player.isInDuel())
						{
							if (player.getDuelId() != obj.getDuelId())
							{
								continue;
							}
							if (player.isInParty() && obj.isInParty() && (player.getParty().getLeaderObjectId() != obj.getParty().getLeaderObjectId()))
							{
								continue;
							}
						}
						
						// Don't add this target if this is a Pc->Pc pvp
						// casting and pvp condition not met
						if (!player.checkPvpSkill(obj, skill))
						{
							continue;
						}
						
						if (!TvTEvent.checkForTvTSkill(player, obj, skill))
						{
							continue;
						}
						
						if (!onlyFirst && L2Skill.addSummon(activeChar, obj, radius, true))
						{
							targetList.add(obj.getSummon());
						}
						
						if (!L2Skill.addCharacter(activeChar, obj, radius, true))
						{
							continue;
						}
						
						// Siege battlefield resurrect has been made possible for participants
						if (skill.getSkillType() == L2SkillType.RESURRECT)
						{
							if (obj.isInsideZone(ZoneId.SIEGE) && !obj.isInSiege())
							{
								continue;
							}
						}
						
						if (onlyFirst)
						{
							return new L2Character[]
							{
								obj
							};
						}
						
						if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
						{
							break;
						}
						targetList.add(obj);
					}
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_CORPSE_ALLY;
	}
}