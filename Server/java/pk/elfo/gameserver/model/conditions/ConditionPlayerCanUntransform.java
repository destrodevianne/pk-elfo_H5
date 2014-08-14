package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Player Can Untransform condition implementation.
 */
public class ConditionPlayerCanUntransform extends Condition
{
	private final boolean _val;

	public ConditionPlayerCanUntransform(boolean val)
	{
		_val = val;
	}

	@Override
	public boolean testImpl(Env env)
	{
		boolean canUntransform = true;
		final L2PcInstance player = env.getPlayer();
		if (player == null)
		{
			canUntransform = false;
		}
		else if (player.isAlikeDead() || player.isCursedWeaponEquipped())
		{
			canUntransform = false;
		}
		else if ((player.isTransformed() || player.isInStance()) && player.isFlyingMounted() && player.isInsideZone(ZoneId.LANDING))
		{
			player.sendPacket(SystemMessageId.TOO_HIGH_TO_PERFORM_THIS_ACTION); // TODO: check if message is retail like.
			canUntransform = false;
		}
		return (_val == canUntransform);
	}
}