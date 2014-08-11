package pk.elfo.gameserver.instancemanager.achievments_engine.conditions;

import pk.elfo.gameserver.instancemanager.achievments_engine.base.Condition;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class Level extends Condition
{
	public Level(Object value)
	{
		super(value);
		setName("Level");
	}
	
	@Override
	public boolean meetConditionRequirements(L2PcInstance player)
	{
		if (getValue() == null)
		{
			return false;
		}
		int val = Integer.parseInt(getValue().toString());
		
		if (player.getLevel() >= val)
		{
			return true;
		}
		return false;
	}
}