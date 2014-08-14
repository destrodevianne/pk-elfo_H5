package pk.elfo.gameserver.model;

import java.util.Calendar;

public class SiegeScheduleDate
{
    private final int _day;
    private final int _hour;
    private final int _maxConcurrent;

    public SiegeScheduleDate(StatsSet set)
    {
        _day = set.getInteger("day", Calendar.SUNDAY);
        _hour = set.getInteger("hour", 16);
        _maxConcurrent = set.getInteger("maxConcurrent", 5);
    }

    public int getDay()
    {
        return _day;
    }

    public int getHour()
    {
        return _hour;
    }

    public int getMaxConcurrent()
    {
        return _maxConcurrent;
    }
}