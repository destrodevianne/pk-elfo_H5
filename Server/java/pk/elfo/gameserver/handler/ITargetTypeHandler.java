package pk.elfo.gameserver.handler;

import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.skills.targets.L2TargetType;

public interface ITargetTypeHandler
{
	static final L2Object[] _emptyTargetList = new L2Object[0];
	
	public L2Object[] getTargetList(L2Skill skill, L2Character activeChar, boolean onlyFirst, L2Character target);
	
	public Enum<L2TargetType> getTargetType();
}