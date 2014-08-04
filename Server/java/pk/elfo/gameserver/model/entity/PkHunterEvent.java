package pk.elfo.gameserver.model.entity;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.util.Util;

public class PkHunterEvent
{
    private static boolean isActive = false;
    private static L2PcInstance Pk = null;
    private static int[] PkLocation =
    {
        0,
        0,
        0
    };

    public static boolean isActive()
    {
        return isActive;
    }

    public static L2PcInstance getPk()
    {
        return Pk;
    }

    public static void setPk(L2PcInstance player)
    {
        Pk = player;
    }

    public static void setActive(Boolean active)
    {
        isActive = active;
    }

    public static void setPkLocation(int x, int y, int z)
    {
        PkLocation[0] = x;
        PkLocation[1] = y;
        PkLocation[2] = z;
    }

    public static int[] getPkLocation()
    {
        return PkLocation;
    }

    public static boolean isPk(L2Character pk)
    {
        if ((pk != null) && (getPk() != null) && pk.getName().equals(getPk().getName()))
        {
            return true;
        }
        return false;
    }

    public static boolean isPkOnline()
    {
        if ((getPk() != null) && getPk().isOnline())
        {
            return true;
        }
        return false;
    }

    public static void sendLocationMessage(L2PcInstance activeChar)
    {
        if (isPkOnline())
        {
            L2PcInstance target = L2World.getInstance().getPlayer(PkHunterEvent.getPk().getName());
            double angle = activeChar.getHeading() / 182.044444444;
            double angle2 = Util.calculateAngleFrom(activeChar, target);
            String location = "";
            String distance = "";
            double finalAngle = angle - angle2;

            if (finalAngle < 0)
            {
                finalAngle += 360;
            }

            double octamore = 22.5;

            if ((finalAngle >= (octamore * 15)) && (finalAngle < (octamore * 17)))
            {
                location = " infront of you";
            }
            else if ((finalAngle >= (octamore * 1)) && (finalAngle < (octamore * 3)))
            {
                location = " infront of you, on your left";
            }
            else if ((finalAngle >= (octamore * 3)) && (finalAngle < (octamore * 5)))
            {
                location = " on your left";
            }
            else if ((finalAngle >= (octamore * 5)) && (finalAngle < (octamore * 7)))
            {
                location = " behind you, on your left";
            }
            else if ((finalAngle >= (octamore * 7)) && (finalAngle < (octamore * 9)))
            {
                location = " behind you";
            }
            else if ((finalAngle >= (octamore * 9)) && (finalAngle < (octamore * 11)))
            {
                location = " behind you, on your right";
            }
            else if ((finalAngle >= (octamore * 11)) && (finalAngle < (octamore * 13)))
            {
                location = " on your right";
            }
            else if ((finalAngle >= (octamore * 13)) && (finalAngle < (octamore * 15)))
            {
                location = " infront of you, on your right";
            }
            else
            {
                location = " infront of you";
            }

            double dist = Util.calculateDistance(activeChar, target, false);

            if (dist < 400)
            {
                distance = ", very close";
            }
            else if (dist < 1000)
            {
                distance = ", close";
            }
            else if (dist < 4000)
            {
                distance = ", at medium range";
            }
            else if (dist < 12000)
            {
                distance = ", quite some distance away";
            }
            else if (dist < 20000)
            {
                distance = ", far away";
            }
            else
            {
                distance = ", very very far away";
            }
            activeChar.sendMessage(target.getName() + " is" + location + " " + distance + ".");
        }
        else
        {
            activeChar.sendMessage("The PK is Offline now.");
        }
    }
}