package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.SevenSigns;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;

/**
 * Player Can Summon Siege Golem implementation.
 * 
 */
public class ConditionPlayerCanSummonSiegeGolem extends Condition
{
	private final boolean _val;

	public ConditionPlayerCanSummonSiegeGolem(boolean val)
	{
		_val = val;
	}

	@Override
	public boolean testImpl(Env env)
	{
		boolean canSummonSiegeGolem = true;
		if ((env.getPlayer() == null) || env.getPlayer().isAlikeDead() || env.getPlayer().isCursedWeaponEquipped() || (env.getPlayer().getClan() == null))
		{
			canSummonSiegeGolem = false;
		}

		final Castle castle = CastleManager.getInstance().getCastle(env.getPlayer());
		final Fort fort = FortManager.getInstance().getFort(env.getPlayer());
		if ((castle == null) && (fort == null))
		{
			canSummonSiegeGolem = false;
		}

		L2PcInstance player = env.getPlayer().getActingPlayer();
		if (((fort != null) && (fort.getFortId() == 0)) || ((castle != null) && (castle.getCastleId() == 0)))
		{
			player.sendPacket(SystemMessageId.INCORRECT_TARGET);
			canSummonSiegeGolem = false;
		}
		else if (((castle != null) && !castle.getSiege().getIsInProgress()) || ((fort != null) && !fort.getSiege().getIsInProgress()))
		{
			player.sendPacket(SystemMessageId.INCORRECT_TARGET);
			canSummonSiegeGolem = false;
		}
		else if ((player.getClanId() != 0) && (((castle != null) && (castle.getSiege().getAttackerClan(player.getClanId()) == null)) || ((fort != null) && (fort.getSiege().getAttackerClan(player.getClanId()) == null))))
		{
			player.sendPacket(SystemMessageId.INCORRECT_TARGET);
			canSummonSiegeGolem = false;
		}
		else if ((SevenSigns.getInstance().checkSummonConditions(env.getPlayer())))
		{
			canSummonSiegeGolem = false;
		}
		return (_val == canSummonSiegeGolem);
	}
}