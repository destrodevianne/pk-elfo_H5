package pk.elfo.gameserver.datatables;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import pk.elfo.gameserver.engines.DocumentParser;
import pk.elfo.gameserver.model.SiegeScheduleDate;
import pk.elfo.gameserver.model.StatsSet;
import pk.elfo.gameserver.util.Util;

public class SiegeScheduleData extends DocumentParser
{
    private final List<SiegeScheduleDate> _scheduleData = new ArrayList<>();

    protected SiegeScheduleData()
    {
        load();
    }

    @Override
    public synchronized void load()
    {
        _scheduleData.clear();
        parseDatapackFile("config/SiegeSchedule.xml");
        _log.log(Level.INFO, getClass().getSimpleName() + ": Loaded: " + _scheduleData.size() + " siege schedulers.");
        if (_scheduleData.isEmpty())
        {
            _scheduleData.add(new SiegeScheduleDate(new StatsSet()));
            _log.log(Level.INFO, getClass().getSimpleName() + ": Emergency Loaded: " + _scheduleData.size() + " default siege schedulers.");
        }
    }

    @Override
    protected void parseDocument()
    {
        for (Node n = getCurrentDocument().getFirstChild(); n != null; n = n.getNextSibling())
        {
            if ("list".equalsIgnoreCase(n.getNodeName()))
            {
                for (Node cd = n.getFirstChild(); cd != null; cd = cd.getNextSibling())
                {
                    switch (cd.getNodeName())
                    {
                        case "schedule":
                        {
                            final StatsSet set = new StatsSet();
                            final NamedNodeMap attrs = cd.getAttributes();
                            for (int i = 0; i < attrs.getLength(); i++)
                            {
                                Node node = attrs.item(i);
                                String key = node.getNodeName();
                                String val = node.getNodeValue();
                                if ("day".equals(key))
                                {
                                    if (!Util.isDigit(val))
                                    {
                                        val = Integer.toString(getValueForField(val));
                                    }
                                }
                                set.set(key, val);
                            }
                            _scheduleData.add(new SiegeScheduleDate(set));
                            break;
                        }
                    }
                }
            }
        }
    }

    private int getValueForField(String field)
    {
        try
        {
            return Calendar.class.getField(field).getInt(Calendar.class);
        }
        catch (Exception e)
        {
            _log.log(Level.WARNING, "", e);
            return -1;
        }
    }

    public List<SiegeScheduleDate> getScheduleDates()
    {
        return _scheduleData;
    }

    public static final SiegeScheduleData getInstance()
    {
        return SingletonHolder._instance;
    }

    private static class SingletonHolder
    {
        protected static final SiegeScheduleData _instance = new SiegeScheduleData();
    }
}