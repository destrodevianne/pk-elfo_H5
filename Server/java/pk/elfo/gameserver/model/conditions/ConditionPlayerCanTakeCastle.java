package pk.elfo.gameserver.model.conditions;

import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.model.entity.Castle;
import pk.elfo.gameserver.model.stats.Env;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.gameserver.util.Util;

/**
 * Player Can Take Castle condition implementation.
 * PkElfo
 */
public class ConditionPlayerCanTakeCastle extends Condition
{
    private final boolean _val;

    public ConditionPlayerCanTakeCastle(boolean val)
    {
        _val = val;
    }

    @Override
    public boolean testImpl(Env env)
    {
        boolean canTakeCastle = true;
        if ((env.getPlayer() == null) || env.getPlayer().isAlikeDead() || env.getPlayer().isCursedWeaponEquipped())
        {
            canTakeCastle = false;
        }
        else if ((env.getPlayer().getClan() == null) || (env.getPlayer().getClan().getLeaderId() != env.getPlayer().getObjectId()))
        {
            canTakeCastle = false;
        }

        Castle castle = CastleManager.getInstance().getCastle(env.getPlayer());
        SystemMessage sm;
        if ((castle == null) || (castle.getCastleId() <= 0) || !castle.getSiege().getIsInProgress() || (castle.getSiege().getAttackerClan(env.getPlayer().getClan()) == null))
        {
            sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED);
            sm.addSkillName(env.getSkill());
            env.getPlayer().sendPacket(sm);
            canTakeCastle = false;
        }
        else if (!castle.getArtefacts().contains(env.getTarget()))
        {
            env.getPlayer().sendPacket(SystemMessageId.INCORRECT_TARGET);
            canTakeCastle = false;
        }
        else if (!Util.checkIfInRange(env.getSkill().getCastRange(), env.getPlayer(), env.getTarget(), true))
        {
            env.getPlayer().sendPacket(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED);
            canTakeCastle = false;
        }
        return (_val == canTakeCastle);
    }
}