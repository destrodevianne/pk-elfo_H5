package pk.elfo.gameserver.instancemanager.achievments_engine.conditions;

import pk.elfo.gameserver.instancemanager.achievments_engine.base.Condition;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class eventKills extends Condition
{
	public eventKills(Object value)
	{
		super(value);
		setName("Event Kills");
	}
	
	@Override
	public boolean meetConditionRequirements(L2PcInstance player)
	{
		if (getValue() == null)
		{
			return false;
		}
		
		@SuppressWarnings("unused")
		int val = Integer.parseInt(getValue().toString());
		
		return false;
	}
}