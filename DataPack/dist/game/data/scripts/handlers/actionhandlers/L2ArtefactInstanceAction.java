package handlers.actionhandlers;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.handler.IActionHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Object.InstanceType;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.MyTargetSelected;
import pk.elfo.gameserver.network.serverpackets.ValidateLocation;

/**
 * Projeto PkElfo
 */

public class L2ArtefactInstanceAction implements IActionHandler
{
	/**
	 * Manage actions when a player click on the L2ArtefactInstance.<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B><BR>
	 * <BR>
	 * <li>Set the L2NpcInstance as target of the L2PcInstance player (if necessary)</li> <li>Send a Server->Client packet MyTargetSelected to the L2PcInstance player (display the select window)</li> <li>Send a Server->Client packet ValidateLocation to correct the L2NpcInstance position and heading
	 * on the client</li><BR>
	 * <BR>
	 * <B><U> Example of use </U> :</B><BR>
	 * <BR>
	 * <li>Client packet : Action, AttackRequest</li><BR>
	 * <BR>
	 */
	@Override
	public boolean action(L2PcInstance activeChar, L2Object target, boolean interact)
	{
		if (!((L2Npc) target).canTarget(activeChar))
		{
			return false;
		}
		
		if (activeChar.getTarget() != target)
		{
			// Set the target of the L2PcInstance activeChar
			activeChar.setTarget(target);
			
			// Send a Server->Client packet MyTargetSelected to the L2PcInstance activeChar
			MyTargetSelected my = new MyTargetSelected(target.getObjectId(), 0);
			activeChar.sendPacket(my);
			
			// Send a Server->Client packet ValidateLocation to correct the L2ArtefactInstance position and heading on the client
			activeChar.sendPacket(new ValidateLocation((L2Character) target));
		}
		else if (interact)
		{
			// Calculate the distance between the L2PcInstance and the L2NpcInstance
			if (!((L2Npc) target).canInteract(activeChar))
			{
				// Notify the L2PcInstance AI with AI_INTENTION_INTERACT
				activeChar.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, target);
			}
		}
		return true;
	}
	
	@Override
	public InstanceType getInstanceType()
	{
		return InstanceType.L2ArtefactInstance;
	}
}