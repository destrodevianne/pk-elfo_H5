package handlers.skillhandlers;

import pk.elfo.gameserver.handler.ISkillHandler;
import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.instancezone.InstanceWorld;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.L2SkillType;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Projeto PkElfo
 */
 
public class NornilsPower implements ISkillHandler
{
	private static final L2SkillType[] SKILL_IDS =
	{
		L2SkillType.NORNILS_POWER
	};
	
	@Override
	public void useSkill(L2Character activeChar, L2Skill skill, L2Object[] targets)
	{
		if (!activeChar.isPlayer())
		{
			return;
		}
		
		InstanceWorld world = null;
		final int instanceId = activeChar.getInstanceId();
		if (instanceId > 0)
		{
			world = InstanceManager.getInstance().getPlayerWorld(activeChar.getActingPlayer());
		}
		
		if ((world != null) && (world.getInstanceId() == instanceId) && (world.getTemplateId() == 11))
		{
			if (activeChar.isInsideRadius(-107393, 83677, 100, true))
			{
				activeChar.destroyItemByItemId("NornilsPower", 9713, 1, activeChar, true);
				L2DoorInstance door = InstanceManager.getInstance().getInstance(world.getInstanceId()).getDoor(16200010);
				if (door != null)
				{
					door.setMeshIndex(1);
					door.setTargetable(true);
					door.broadcastStatusUpdate();
				}
			}
			else
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
				sm.addSkillName(skill);
				activeChar.sendPacket(sm);
			}
		}
		else
		{
			activeChar.sendPacket(SystemMessageId.NOTHING_HAPPENED);
		}
	}
	
	@Override
	public L2SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}