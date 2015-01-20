package pk.elfo.gameserver.network.serverpackets;

import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;

public class ExAirShipInfo extends L2GameServerPacket
{
    // store some parameters, because they can be changed during broadcast
    private final L2AirShipInstance _ship;
    private final int _x, _y, _z, _heading, _moveSpeed, _rotationSpeed, _captain, _helm;

    public ExAirShipInfo(L2AirShipInstance ship)
    {
        _ship = ship;
        _x = ship.getX();
        _y = ship.getY();
        _z = ship.getZ();
        _heading = ship.getHeading();
        _moveSpeed = (int) ship.getStat().getMoveSpeed();
        _rotationSpeed = ship.getStat().getRotationSpeed();
        _captain = ship.getCaptainId();
        _helm = ship.getHelmObjectId();
    }

    @Override
    protected void writeImpl()
    {
        writeC(0xfe);
        writeH(0x60);

        writeD(_ship.getObjectId());
        writeD(_x);
        writeD(_y);
        writeD(_z);
        writeD(_heading);

        writeD(_captain);
        writeD(_moveSpeed);
        writeD(_rotationSpeed);
        writeD(_helm);
        if (_helm != 0)
        {
            // TODO: unhardcode these!
            writeD(0x16e); // Controller X
            writeD(0x00); // Controller Y
            writeD(0x6b); // Controller Z
            writeD(0x15c); // Captain X
            writeD(0x00); // Captain Y
            writeD(0x69); // Captain Z
        }
        else
        {
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
            writeD(0x00);
        }
        writeD(_ship.getFuel());
        writeD(_ship.getMaxFuel());
    }
}