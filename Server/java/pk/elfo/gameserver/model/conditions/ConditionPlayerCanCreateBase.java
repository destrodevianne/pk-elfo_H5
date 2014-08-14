package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.instancemanager.FortSiegeManager;
import pk.elfo.gameserver.instancemanager.SiegeManager;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.entity.Fort;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Player Can Create Base condition implementation.
 */

public class ConditionPlayerCanCreateBase extends Condition
{
	private final boolean _val;
	
	public ConditionPlayerCanCreateBase(boolean val)
	{
		_val = val;
	}
	
	@Override
	public boolean testImpl(Env env)
	{
		boolean canCreateBase = true;
		if ((env.getPlayer() == null) || env.getPlayer().isAlikeDead() || env.getPlayer().isCursedWeaponEquipped() || (env.getPlayer().getClan() == null))
		{
			canCreateBase = false;
		}
		final Castle castle = CastleManager.getInstance().getCastle(env.getPlayer());
		final Fort fort = FortManager.getInstance().getFort(env.getPlayer());
		final SystemMessage sm;
		L2PcInstance player = env.getPlayer().getActingPlayer();
		if ((castle == null) && (fort == null))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(env.getSkill());
			player.sendPacket(sm);
			canCreateBase = false;
		}
		else if (((castle != null) && !castle.getSiege().getIsInProgress()) || ((fort != null) && !fort.getSiege().getIsInProgress()))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(env.getSkill());
			player.sendPacket(sm);
			canCreateBase = false;
		}
		else if (((castle != null) && (castle.getSiege().getAttackerClan(player.getClan()) == null)) || ((fort != null) && (fort.getSiege().getAttackerClan(player.getClan()) == null)))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(env.getSkill());
			player.sendPacket(sm);
			canCreateBase = false;
		}
		else if (!player.isClanLeader())
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(env.getSkill());
			player.sendPacket(sm);
			canCreateBase = false;
		}
		else if (((castle != null) && (castle.getSiege().getAttackerClan(player.getClan()).getNumFlags() >= SiegeManager.getInstance().getFlagMaxCount())) || ((fort != null) && (fort.getSiege().getAttackerClan(player.getClan()).getNumFlags() >= FortSiegeManager.getInstance().getFlagMaxCount())))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
			sm.addSkillName(env.getSkill());
			player.sendPacket(sm);
			canCreateBase = false;
		}
		else if (!player.isInsideZone(ZoneId.HQ))
		{
			player.sendPacket(SystemMessageId.NOT_SET_UP_BASE_HERE);
			canCreateBase = false;
		}
		return (_val == canCreateBase);
	}
}