package king.server.gameserver.datatables;

import java.util.Date;
import java.util.List;

import javolution.util.FastList;
import king.server.gameserver.script.DateRange;
import king.server.gameserver.script.EventDrop;

public class EventDroplist
{
	/**
	 * The table containing all DataDrop object
	 */
	private static final List<DateDrop> _allNpcDateDrops = new FastList<>();
	
	public static class DateDrop
	{
		protected final DateRange _dateRange;
		private final EventDrop _eventDrop;
		
		public DateDrop(DateRange dateRange, EventDrop eventDrop)
		{
			_dateRange = dateRange;
			_eventDrop = eventDrop;
		}
		
		/**
		 * @return the _eventDrop
		 */
		public EventDrop getEventDrop()
		{
			return _eventDrop;
		}
		
		/**
		 * @return the _dateRange
		 */
		public DateRange getDateRange()
		{
			return _dateRange;
		}
	}
	
	/**
	 * Create and Init a new DateDrop then add it to the allNpcDateDrops of EventDroplist .
	 * @param itemIdList The table containing all item identifier of this DateDrop
	 * @param count The table containing min and max value of this DateDrop
	 * @param chance The chance to obtain this drop
	 * @param dateRange The DateRange object to add to this DateDrop
	 */
	public void addGlobalDrop(int[] itemIdList, int[] count, int chance, DateRange dateRange)
	{
		_allNpcDateDrops.add(new DateDrop(dateRange, new EventDrop(itemIdList, count[0], count[1], chance)));
	}
	
	/**
	 * @param itemId the item Id for the drop
	 * @param minCount the minimum drop count
	 * @param maxCount the maximum drop count
	 * @param chance the drop chance
	 * @param dateRange the event drop rate range
	 */
	public void addGlobalDrop(int itemId, int minCount, int maxCount, int chance, DateRange dateRange)
	{
		_allNpcDateDrops.add(new DateDrop(dateRange, new EventDrop(itemId, minCount, maxCount, chance)));
	}
	
	/**
	 * Adds an event drop for a given date range.
	 * @param dateRange the date range.
	 * @param eventDrop the event drop.
	 */
	public void addGlobalDrop(DateRange dateRange, EventDrop eventDrop)
	{
		_allNpcDateDrops.add(new DateDrop(dateRange, eventDrop));
	}
	
	/**
	 * @return all DateDrop of EventDroplist allNpcDateDrops within the date range.
	 */
	public List<DateDrop> getAllDrops()
	{
		final List<DateDrop> list = new FastList<>();
		final Date currentDate = new Date();
		for (DateDrop drop : _allNpcDateDrops)
		{
			if (drop._dateRange.isWithinRange(currentDate))
			{
				list.add(drop);
			}
		}
		return list;
	}
	
	public static EventDroplist getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final EventDroplist _instance = new EventDroplist();
	}
}