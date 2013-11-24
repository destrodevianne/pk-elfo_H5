package king.server.gameserver.model.entity;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import king.server.Config;
import king.server.gameserver.Announcements;
import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.instancemanager.TownManager;
import king.server.gameserver.model.L2World;
import king.server.gameserver.model.actor.instance.L2PcInstance;

public class TownWarManager
{
	protected static final Logger _log = Logger.getLogger(TownWarManager.class.getName());
	private TownWarStartTask _task;
	private static final int ALL_TOWNS_INT = 17;
	public boolean isInactive = false;
	public boolean isStarting = false;
	public boolean isStarted = false;
	
	private TownWarManager()
	{
		if (Config.TW_AUTO_EVENT)
		{
			isInactive = true;
			scheduleEventStart();
			_log.info("TownWar Engine: Iniciado.");
		}
		else
		{
			_log.info("TownWar Engine: Desativado.");
		}
	}
	
	public static TownWarManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			for (String timeOfDay : Config.TW_INTERVAL)
			{
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				if ((nextStartTime == null) || (testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis()))
				{
					nextStartTime = testStartTime;
				}
			}
			_task = new TownWarStartTask(nextStartTime.getTimeInMillis());
			ThreadPoolManager.getInstance().executeTask(_task);
		}
		catch (Exception e)
		{
			_log.warning("TownWar Engine: Error figuring out a start time. Check TownWarEventInterval in config file.");
		}
	}
	
	public void starting()
	{
		isInactive = false;
		isStarting = true;
		_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TW_TIME_BEFORE_START));
		ThreadPoolManager.getInstance().executeTask(_task);
	}
	
	public void startEvent()
	{
		isStarting = false;
		isStarted = true;
		if (Config.TW_ALL_TOWNS)
		{
			for (int i = 1; i <= ALL_TOWNS_INT; i++)
			{
				TownManager.getTown(i).setIsTWZone(true);
				TownManager.getTown(i).updateForCharactersInside();
			}
			TownManager.getTown(20).setIsTWZone(true);
			TownManager.getTown(20).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: All towns are war zone.");
		}
		else
		{
			TownManager.getTown(Config.TW_TOWN_ID).setIsTWZone(true);
			TownManager.getTown(Config.TW_TOWN_ID).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: " + Config.TW_TOWN_NAME + " is a war zone.");
		}
		
		_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TW_RUNNING_TIME));
		ThreadPoolManager.getInstance().executeTask(_task);
	}
	
	public void endEvent()
	{
		isStarted = false;
		isInactive = true;
		if (Config.TW_ALL_TOWNS)
		{
			for (int i = 1; i <= ALL_TOWNS_INT; i++)
			{
				TownManager.getTown(i).setIsTWZone(false);
				TownManager.getTown(i).updateForCharactersInside();
			}
			TownManager.getTown(20).setIsTWZone(false);
			TownManager.getTown(20).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: All towns are returned normal.");
		}
		else
		{
			TownManager.getTown(Config.TW_TOWN_ID).setIsTWZone(false);
			TownManager.getTown(Config.TW_TOWN_ID).updateForCharactersInside();
			Announcements.getInstance().announceToAll("Town War: " + Config.TW_TOWN_NAME + " is returned normal.");
		}
		
		scheduleEventStart();
	}
	
	class TownWarStartTask implements Runnable
	{
		private long _startTime;
		public ScheduledFuture<?> nextRun;
		
		public TownWarStartTask(long startTime)
		{
			_startTime = startTime;
		}
		
		public void setStartTime(long startTime)
		{
			_startTime = startTime;
		}
		
		/**
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run()
		{
			int delay = (int) Math.round((_startTime - System.currentTimeMillis()) / 1000.0);
			
			if (delay > 0)
			{
				announce(delay);
			}
			
			int nextMsg = 0;
			if (delay > 3600)
			{
				nextMsg = delay - 3600;
			}
			else if (delay > 1800)
			{
				nextMsg = delay - 1800;
			}
			else if (delay > 900)
			{
				nextMsg = delay - 900;
			}
			else if (delay > 600)
			{
				nextMsg = delay - 600;
			}
			else if (delay > 300)
			{
				nextMsg = delay - 300;
			}
			else if (delay > 60)
			{
				nextMsg = delay - 60;
			}
			else if (delay > 5)
			{
				nextMsg = delay - 5;
			}
			else if (delay > 0)
			{
				nextMsg = delay;
			}
			else
			{
				if (isInactive)
				{
					starting();
				}
				else if (isStarting)
				{
					startEvent();
				}
				else
				{
					endEvent();
				}
			}
			
			if (delay > 0)
			{
				nextRun = ThreadPoolManager.getInstance().scheduleGeneral(this, nextMsg * 1000);
			}
		}
		
		private void announce(long time)
		{
			if ((time >= 3600) && ((time % 3600) == 0))
			{
				if (isStarting)
				{
					Announcements.getInstance().announceToAll("Town War Event: " + (time / 60 / 60) + " hour(s) until event starts!");
				}
				else if (isStarted)
				{
					for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
					{
						if ((onlinePlayer != null) && onlinePlayer.isOnline())
						{
							onlinePlayer.sendMessage("Town War Event: " + (time / 60 / 60) + " hour(s) until event is finished!");
						}
					}
				}
			}
			else if (time >= 60)
			{
				if (isStarting)
				{
					Announcements.getInstance().announceToAll("Town War Event: " + (time / 60) + " minute(s) until event starts!");
				}
				else if (isStarted)
				{
					for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
					{
						if ((onlinePlayer != null) && onlinePlayer.isOnline())
						{
							onlinePlayer.sendMessage("Town War Event: " + (time / 60) + " minute(s) until the event is finished!");
						}
					}
				}
			}
			else
			{
				if (isStarting)
				{
					Announcements.getInstance().announceToAll("Town War Event: " + time + " second(s) until event starts!");
				}
				else if (isStarted)
				{
					for (L2PcInstance onlinePlayer : L2World.getInstance().getAllPlayersArray())
					{
						if ((onlinePlayer != null) && onlinePlayer.isOnline())
						{
							onlinePlayer.sendMessage("Town War Event: " + time + " second(s) until the event is finished!");
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TownWarManager _instance = new TownWarManager();
	}
}