package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Player Call Pc condition implementation.
 */

public class ConditionPlayerCallPc extends Condition
{
	private final boolean _val;

	public ConditionPlayerCallPc(boolean val)
	{
		_val = val;
	}

	@Override
	public boolean testImpl(Env env)
	{
		boolean canCallPlayer = true;
		final L2PcInstance player = env.getPlayer();
		if (player == null)
		{
			canCallPlayer = false;
		}
		else if (player.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
			canCallPlayer = false;
		}
		else if (player.inObserverMode())
		{
			canCallPlayer = false;
		}
		else if (!TvTEvent.onEscapeUse(player.getObjectId()))
		{
			player.sendPacket(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING);
			canCallPlayer = false;
		}
		else if (player.isInsideZone(ZoneId.NO_SUMMON_FRIEND) || player.isInsideZone(ZoneId.JAIL) || player.isFlyingMounted())
		{
			player.sendPacket(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING);
			canCallPlayer = false;
		}
		return (_val == canCallPlayer);
	}
}