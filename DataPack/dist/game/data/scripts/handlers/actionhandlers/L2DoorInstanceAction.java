package handlers.actionhandlers;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.handler.IActionHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.clanhall.SiegableHall;
import pk.elfo.gameserver.network.serverpackets.ConfirmDlg;
import pk.elfo.gameserver.network.serverpackets.MyTargetSelected;
import pk.elfo.gameserver.network.serverpackets.StaticObject;

/**
 * Projeto PkElfo
 */

public class L2DoorInstanceAction implements IActionHandler
{
	@Override
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact)
	{
		// Check if the L2PcInstance already target the L2NpcInstance
		if (activeChar.getTarget() != target)
		{
			// Set the target of the L2PcInstance activeChar
			activeChar.setTarget(target);
			
			// Send a Server->Client packet MyTargetSelected to the L2PcInstance activeChar
			activeChar.sendPacket(new MyTargetSelected(target.getObjectId(), 0));
			
			StaticObject su = new StaticObject((L2DoorInstance) target, activeChar.isGM());
			activeChar.sendPacket(su);
		}
		else if (interact)
		{
			L2DoorInstance door = (L2DoorInstance) target;
			// MyTargetSelected my = new MyTargetSelected(getObjectId(), activeChar.getLevel());
			// activeChar.sendPacket(my);
			if (target.isAutoAttackable(activeChar))
			{
				if (Math.abs(activeChar.getZ() - target.getZ()) < 400)
				{
					activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
				}
			}
			else if ((activeChar.getClan() != null) && (door.getClanHall() != null) && (activeChar.getClanId() == door.getClanHall().getOwnerId()))
			{
				if (!door.isInsideRadius(activeChar, L2Npc.INTERACTION_DISTANCE, false, false))
				{
					activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
				}
				else if (!door.getClanHall().isSiegableHall() || !((SiegableHall) door.getClanHall()).isInSiege())
				{
					activeChar.gatesRequest(door);
					if (!door.getOpen())
					{
						activeChar.sendPacket(new ConfirmDlg(1140));
					}
					else
					{
						activeChar.sendPacket(new ConfirmDlg(1141));
					}
				}
			}
			else if ((activeChar.getClan() != null) && (((L2DoorInstance) target).getFort() != null) && (activeChar.getClan() == ((L2DoorInstance) target).getFort().getOwnerClan()) && ((L2DoorInstance) target).isOpenableBySkill() && !((L2DoorInstance) target).getFort().getSiege().getIsInProgress())
			{
				if (!((L2Character) target).isInsideRadius(activeChar, L2Npc.INTERACTION_DISTANCE, false, false))
				{
					activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
				}
				else
				{
					activeChar.gatesRequest((L2DoorInstance) target);
					if (!((L2DoorInstance) target).getOpen())
					{
						activeChar.sendPacket(new ConfirmDlg(1140));
					}
					else
					{
						activeChar.sendPacket(new ConfirmDlg(1141));
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2DoorInstance;
	}
}