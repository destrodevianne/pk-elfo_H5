package handlers.skillhandlers;

import java.util.logging.Level;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Party;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.ConfirmDlg;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.util.Util;

/**
 * Projeto PkElfo
 */
 
public class SummonFriend implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.SUMMON_FRIEND
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		final boolean isMastersCall = skill.getId() == 23249;
		final L2PcInstance activePlayer = activeChar.getActingPlayer();
		if (!isMastersCall && !L2PcInstance.checkSummonerStatus(activePlayer))
		{
			return;
		}
		
		try
		{
			for (L2Character target : (L2Character[]) targets)
			{
				if ((target == null) || (activeChar == target))
				{
					continue;
				}
				
				if (target.isPlayer())
				{
					if (isMastersCall) // Master's Call
					{
						final L2Party party = target.getParty();
						if (party != null)
						{
							for (L2PcInstance partyMember : party.getMembers())
							{
								if (target != partyMember)
								{
									partyMember.teleToLocation(target.getX(), target.getY(), target.getZ(), true);
								}
							}
						}
						else
						{
							activePlayer.sendMessage(target.getName() + " nao tem uma party.");
						}
						continue;
					}
					
					final L2PcInstance targetPlayer = target.getActingPlayer();
					if (!L2PcInstance.checkSummonTargetStatus(targetPlayer, activePlayer))
					{
						continue;
					}
					
					if (!Util.checkIfInRange(0, activeChar, target, false))
					{
						if (!targetPlayer.teleportRequest(activePlayer, skill))
						{
							final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_ALREADY_SUMMONED);
							sm.addString(target.getName());
							activePlayer.sendPacket(sm);
							continue;
						}
						
						if (skill.getId() == 1403) // Summon Friend
						{
							// Send message
							final ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.C1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId());
							confirm.addCharName(activeChar);
							confirm.addZoneName(activeChar.getX(), activeChar.getY(), activeChar.getZ());
							confirm.addTime(30000);
							confirm.addRequesterId(activePlayer.getObjectId());
							target.sendPacket(confirm);
						}
						else
						{
							L2PcInstance.teleToTarget(targetPlayer, activePlayer, skill);
							targetPlayer.teleportRequest(null, null);
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "", e);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}