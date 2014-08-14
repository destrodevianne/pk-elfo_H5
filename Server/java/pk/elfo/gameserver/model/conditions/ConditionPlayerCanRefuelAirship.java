package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.model.actor.instance.L2ControllableAirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Player Can Refuel Airship condition implementation.
 */

public class ConditionPlayerCanRefuelAirship extends Condition
{
	private final int _val;

	public ConditionPlayerCanRefuelAirship(int val)
	{
		_val = val;
	}

	@Override
	public boolean testImpl(Env env)
	{
		boolean canRefuelAirship = true;
		final L2PcInstance player = env.getPlayer();
		if ((player == null) || (player.getAirShip() == null) || !(player.getAirShip() instanceof L2ControllableAirShipInstance) || ((player.getAirShip().getFuel() + _val) > player.getAirShip().getMaxFuel()))
		{
			canRefuelAirship = false;
		}
		return canRefuelAirship;
	}
}