package pk.elfo.gameserver.util;

import java.util.Comparator;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * Static comparators.
 * @author Zoey76
 */
public class Comparators
{
    /** Compares its two arguments for order by it's name. */
    public static final Comparator<L2PcInstance> PLAYER_NAME_COMPARATOR = new Comparator<L2PcInstance>()
    {
        @Override
        public int compare(L2PcInstance p1, L2PcInstance p2)
        {
            return p1.getName().compareToIgnoreCase(p2.getName());
        }
    };
    /** Compares its two arguments for order by it's up-time, the one that logged first. */
    public static final Comparator<L2PcInstance> PLAYER_UPTIME_COMPARATOR = new Comparator<L2PcInstance>()
    {
        @Override
        public int compare(L2PcInstance p1, L2PcInstance p2)
        {
            return Long.compare(p1.getUptime(), p2.getUptime());
        }
    };
    /** Compares its two arguments for order by it's PVP kills. */
    public static final Comparator<L2PcInstance> PLAYER_PVP_COMPARATOR = new Comparator<L2PcInstance>()
    {
        @Override
        public int compare(L2PcInstance p1, L2PcInstance p2)
        {
            return Integer.compare(p1.getPvpKills(), p2.getPvpKills());
        }
    };
    /** Compares its two arguments for order by it's PK kills. */
    public static final Comparator<L2PcInstance> PLAYER_PK_COMPARATOR = new Comparator<L2PcInstance>()
    {
        @Override
        public int compare(L2PcInstance p1, L2PcInstance p2)
        {
            return Integer.compare(p1.getPkKills(), p2.getPkKills());
        }
    };
}