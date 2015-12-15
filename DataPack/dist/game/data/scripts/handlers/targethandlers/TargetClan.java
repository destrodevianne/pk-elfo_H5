package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.model.L2ClanMember;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

/**
 * Projeto PkElfo
 */

public class TargetClan implements ITargetTypeHandler
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
			
			if (onlyFirst)
			{
				return new L2Character[]
				{
					player
				};
			}
			
			targetList.add(player);
			
			final int radius = skill.getSkillRadius();
			final L2Clan clan = player.getClan();
			
			if (L2Skill.addSummon(activeChar, player, radius, false))
			{
				targetList.add(player.getSummon());
			}
			
			if (clan != null)
			{
				L2PcInstance obj;
				for (L2ClanMember member : clan.getMembers())
				{
					obj = member.getPlayerInstance();
					
					if ((obj == null) || (obj == player))
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
					
					// Don't add this target if this is a Pc->Pc pvp casting and pvp condition not met
					if (!player.checkPvpSkill(obj, skill))
					{
						continue;
					}
					
					if (!TvTEvent.checkForTvTSkill(player, obj, skill))
					{
						continue;
					}
					
					if (!onlyFirst && L2Skill.addSummon(activeChar, obj, radius, false))
					{
						targetList.add(obj.getSummon());
					}
					
					if (!L2Skill.addCharacter(activeChar, obj, radius, false))
					{
						continue;
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
		else if (activeChar.isNpc())
		{
			// for buff purposes, returns friendly mobs nearby and mob itself
			final L2Npc npc = (L2Npc) activeChar;
			if ((npc.getFactionId() == null) || npc.getFactionId().isEmpty())
			{
				return new L2Character[]
				{
					activeChar
				};
			}
			
			targetList.add(activeChar);
			
			final Collection<L2Object> objs = activeChar.getKnownList().getKnownObjects().values();
			
			for (L2Object newTarget : objs)
			{
				if (newTarget.isNpc() && npc.getFactionId().equals(((L2Npc) newTarget).getFactionId()))
				{
					if (!Util.checkIfInRange(skill.getCastRange(), activeChar, newTarget, true))
					{
						continue;
					}
					
					if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
					{
						break;
					}
					targetList.add((L2Npc) newTarget);
				}
			}
		}
		return targetList.toArray(new L2Character[targetList.size()]);
	}
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_CLAN;
	}
}