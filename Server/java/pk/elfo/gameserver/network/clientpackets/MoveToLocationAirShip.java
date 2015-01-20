package pk.elfo.gameserver.network.clientpackets;

import pk.elfo.gameserver.TaskPriority;
import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.instancemanager.AirShipManager;
import pk.elfo.gameserver.model.L2CharPosition;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.VehiclePathPoint;
import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.SystemMessageId;

public class MoveToLocationAirShip extends L2GameClientPacket
{
    private static final String _C__D0_38_MOVETOLOCATIONAIRSHIP = "[C] D0:38 MoveToLocationAirShip";

    public static final int MIN_Z = -895;
    public static final int MAX_Z = 6105;
    public static final int STEP = 300;

    private int _command;
    private int _param1;
    private int _param2 = 0;

    public TaskPriority getPriority()
    {
        return TaskPriority.PR_HIGH;
    }

    @Override
    protected void readImpl()
    {
        _command = readD();
        _param1 = readD();
        if (_buf.remaining() > 0)
        {
            _param2 = readD();
        }
    }

    @Override
    protected void runImpl()
    {
        final L2PcInstance activeChar = getClient().getActiveChar();
        if (activeChar == null)
        {
            return;
        }

        if (!activeChar.isInAirShip())
        {
            return;
        }

        final L2AirShipInstance ship = activeChar.getAirShip();
        if (!ship.isCaptain(activeChar))
        {
            return;
        }

        int z = ship.getZ();

        switch (_command)
        {
            case 0:
                if (!ship.canBeControlled())
                {
                    return;
                }
                if (_param1 < L2World.GRACIA_MAX_X)
                {
                    ship.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(_param1, _param2, z, 0));
                }
                break;
            case 1:
                if (!ship.canBeControlled())
                {
                    return;
                }
                ship.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
                break;
            case 2:
                if (!ship.canBeControlled())
                {
                    return;
                }
                if (z < L2World.GRACIA_MAX_Z)
                {
                    z = Math.min(z + STEP, L2World.GRACIA_MAX_Z);
                    ship.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(ship.getX(), ship.getY(), z, 0));
                }
                break;
            case 3:
                if (!ship.canBeControlled())
                {
                    return;
                }
                if (z > L2World.GRACIA_MIN_Z)
                {
                    z = Math.max(z - STEP, L2World.GRACIA_MIN_Z);
                    ship.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(ship.getX(), ship.getY(), z, 0));
                }
                break;
            case 4:
                if (!ship.isInDock() || ship.isMoving())
                {
                    return;
                }

                final VehiclePathPoint[] dst = AirShipManager.getInstance().getTeleportDestination(ship.getDockId(), _param1);
                if (dst == null)
                {
                    return;
                }

                // Consume fuel, if needed
                final int fuelConsumption = AirShipManager.getInstance().getFuelConsumption(ship.getDockId(), _param1);
                if (fuelConsumption > 0)
                {
                    if (fuelConsumption > ship.getFuel())
                    {
                        activeChar.sendPacket(SystemMessageId.THE_AIRSHIP_CANNOT_TELEPORT);
                        return;
                    }
                    ship.setFuel(ship.getFuel() - fuelConsumption);
                }

                ship.executePath(dst);
                break;
        }
    }

    @Override
    public String getType()
    {
        return _C__D0_38_MOVETOLOCATIONAIRSHIP;
    }
}