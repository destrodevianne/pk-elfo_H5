package pk.elfo.gameserver.instancemanager.achievments_engine.base;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public abstract class Condition
{
	private final Object _value;
	private String _name;

	public Condition(Object value)
	{
		_value = value;
	}

	public abstract boolean meetConditionRequirements(L2PcInstance player);

	public Object getValue()
	{
		return _value;
	}

	public void setName(String s)
	{
		_name = s;
	}

	public String getName()
	{
		return _name;
	}
}