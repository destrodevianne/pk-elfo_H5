package handlers.targethandlers;

import java.util.List;

import pk.elfo.Config;
import pk.elfo.gameserver.handler.ITargetTypeHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2ServitorInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;
import pk.elfo.gameserver.network.SystemMessageId;
import javolution.util.FastList;

/**
 * PkElfo
 */

public class TargetCorpseMob implements ITargetTypeHandler
{
	@Override
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target)
	{
		List<L2Character> targetList = new FastList<>();
		final boolean isSummon = target.isServitor();
		if (!(isSummon || target.isL2Attackable()) || !target.isDead())
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return _emptyTargetList;
		}
		
		// Corpse mob only available for half time
		switch (skill.getSkillType())
		{
			case SUMMON:
			{
				if (isSummon && (((L2ServitorInstance) target).getOwner() != null) && (((L2ServitorInstance) target).getOwner().getObjectId() == activeChar.getObjectId()))
				{
					return _emptyTargetList;
				}
				
				break;
			}
			case DRAIN:
			{
				if (!((L2Attackable) target).checkCorpseTime(activeChar.getActingPlayer(), (Config.NPC_DECAY_TIME / 2), true))
				{
					return _emptyTargetList;
				}
			}
			default:
				break;
		}
		
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
	
	@Override
	public Enum<L2TargetType> getTargetType()
	{
		return L2TargetType.TARGET_CORPSE_MOB;
	}
}