package handlers.targethandlers;

import java.util.Collection;
import java.util.List;

import javolution.util.FastList;

import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.instance.L2SiegeFlagInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.network.SystemMessageId;

public class TargetAlly implements ITargetTypeHandler
{
    @Override
    public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
    {
        List<L2Character> targetList = new FastList<>();
        if (activeChar.isPlayable())
        {
            final L2PcInstance player = activeChar.getActingPlayer();
            
            if ((player == null) || (target == null) || target.isAlikeDead() || target.isDoor() || (target instanceof L2SiegeFlagInstance) || target.isMonster() || target.isNpc())
            {
                activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
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
                    target
                };
            }
            
            /**if ((target == activeChar) || (activeChar.isInParty() && target.isInParty() && (activeChar.getParty().getLeaderObjectId() == target.getParty().getLeaderObjectId())) || (activeChar.isPlayer() && target.isSummon() && (activeChar.getSummon() == target)) || (activeChar.isSummon() && target.isPlayer() && (activeChar == target.getSummon())))
            {
                return new L2Character[]
                {
                    target
                };
            }*/
            
            targetList.add(target);
            
            final int radius = skill.getSkillRadius();
            
            if (L2Skill.addSummon(activeChar, player, radius, false))
            {
                targetList.add(player.getSummon());
            }
            
            if (player.getClan() != null)
            {
                // Get all visible objects in a spherical area near the L2Character
                final Collection<L2PcInstance> objs = activeChar.getKnownList().getKnownPlayersInRadius(radius);
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
            if (activeChar.isInParty())
            {
                // Get a list of Party Members
                for (L2PcInstance partyMember : activeChar.getParty().getMembers())
                {
                    if ((partyMember == null) || (partyMember == player))
                    {
                        continue;
                    }
                    
                    if ((skill.getMaxTargets() > -1) && (targetList.size() >= skill.getMaxTargets()))
                    {
                        break;
                    }
                    
                    if (L2Skill.addCharacter(activeChar, partyMember, radius, false))
                    {
                        targetList.add(partyMember);
                    }
                    
                    if (L2Skill.addSummon(activeChar, partyMember, radius, false))
                    {
                        targetList.add(partyMember.getSummon());
                    }
                }
            }
        }
        return targetList.toArray(new L2Character[targetList.size()]);
    }

    @Override
    public Enum<L2TargetType> getTargetType()
    {
        return L2TargetType.TARGET_ALLY;
    }
}