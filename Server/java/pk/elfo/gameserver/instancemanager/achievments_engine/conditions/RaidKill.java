package pk.elfo.gameserver.instancemanager.achievments_engine.conditions;

import java.util.Map;

import pk.elfo.gameserver.instancemanager.RaidBossPointsManager;
import pk.elfo.gameserver.instancemanager.achievments_engine.base.Condition;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * PkElfo
 */

public class RaidKill extends Condition
{
	public RaidKill(Object value)
	{
		super(value);
		setName("Raid Kill");
	}

	@Override
	public boolean meetConditionRequirements(L2PcInstance player)
	{
		if (getValue() == null)
		{
			return false;
		}

		int val = Integer.parseInt(getValue().toString());

		Map<Integer, Integer> list = RaidBossPointsManager.getInstance().getList(player);
		if (list != null)
		{
			for (int bid : list.keySet())
			{
				if (bid == val)
				{
					if (RaidBossPointsManager.getInstance().getList(player).get(bid) > 0)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}