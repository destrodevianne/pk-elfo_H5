package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.TaskPriority;
import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.type.L2WeaponType;
import pk.elfo.gameserver.network.serverpackets.ActionFailed;
import pk.elfo.gameserver.network.serverpackets.ExMoveToLocationInAirShip;
import pk.elfo.gameserver.network.serverpackets.StopMoveInVehicle;
import pk.elfo.gameserver.util.Point3D;

/**
 * format: ddddddd X:%d Y:%d Z:%d OriginX:%d OriginY:%d OriginZ:%d
 * @author GodKratos
 */
public class MoveToLocationInAirShip extends L2GameClientPacket
{
    private static final String _C__D0_20_MOVETOLOCATIONINAIRSHIP = "[C] D0:20 MoveToLocationInAirShip";

    private int _shipId;
    private int _targetX;
    private int _targetY;
    private int _targetZ;
    private int _originX;
    private int _originY;
    private int _originZ;

    public TaskPriority getPriority()
    {
        return TaskPriority.PR_HIGH;
    }

    @Override
    protected void readImpl()
    {
        _shipId = readD();
        _targetX = readD();
        _targetY = readD();
        _targetZ = readD();
        _originX = readD();
        _originY = readD();
        _originZ = readD();
    }

    @Override
    protected void runImpl()
    {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null)
        {
            return;
        }

        if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ))
        {
            activeChar.sendPacket(new StopMoveInVehicle(activeChar, _shipId));
            return;
        }

        if (activeChar.isAttackingNow() && (activeChar.getActiveWeaponItem() != null) && (activeChar.getActiveWeaponItem().getItemType() == L2WeaponType.BOW))
        {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (activeChar.isSitting() || activeChar.isMovementDisabled())
        {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (!activeChar.isInAirShip())
        {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        final L2AirShipInstance airShip = activeChar.getAirShip();
        if (airShip.getObjectId() != _shipId)
        {
            activeChar.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        activeChar.setInVehiclePosition(new Point3D(_targetX, _targetY, _targetZ));
        activeChar.broadcastPacket(new ExMoveToLocationInAirShip(activeChar));
    }

    @Override
    public String getType()
    {
        return _C__D0_20_MOVETOLOCATIONINAIRSHIP;
    }
}