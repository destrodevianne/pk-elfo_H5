package pk.elfo.gameserver.model.entity;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.ThreadPoolManager;

public class TvTRoundManager
{
	protected static final Logger _log = Logger.getLogger(TvTRoundManager.class.getName());
	
	/** Task for event cycles<br> */
	private TvTRoundStartTask _task;
	
	/**
	 * New instance only by getInstance()<br>
	 */
	private TvTRoundManager()
	{
		if (Config.TVT_ROUND_EVENT_ENABLED)
		{
			TvTRoundEvent.init();
			
			scheduleEventStart();
			_log.info("TvTRound Event Engine: Iniciado.");
		}
		else
		{
			_log.info("TvTRound Event Engine: Desativado.");
		}
	}
	
	/**
	 * Initialize new/Returns the one and only instance<br>
	 * <br>
	 * @return TvTRoundManager<br>
	 */
	public static TvTRoundManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	/**
	 * Starts TvTRoundStartTask
	 */
	public void scheduleEventStart()
	{
		try
		{
			Calendar currentTime = Calendar.getInstance();
			Calendar nextStartTime = null;
			Calendar testStartTime = null;
			for (String timeOfDay : Config.TVT_ROUND_EVENT_INTERVAL)
			{
				// Creating a Calendar object from the specified interval value
				testStartTime = Calendar.getInstance();
				testStartTime.setLenient(true);
				String[] splitTimeOfDay = timeOfDay.split(":");
				testStartTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeOfDay[0]));
				testStartTime.set(Calendar.MINUTE, Integer.parseInt(splitTimeOfDay[1]));
				// If the date is in the past, make it the next day (Example: Checking for "1:00", when the time is 23:57.)
				if (testStartTime.getTimeInMillis() < currentTime.getTimeInMillis())
				{
					testStartTime.add(Calendar.DAY_OF_MONTH, 1);
				}
				// Check for the test date to be the minimum (smallest in the specified list)
				if ((nextStartTime == null) || (testStartTime.getTimeInMillis() < nextStartTime.getTimeInMillis()))
				{
					nextStartTime = testStartTime;
				}
			}
			_task = new TvTRoundStartTask(nextStartTime.getTimeInMillis());
			ThreadPoolManager.getInstance().executeTask(_task);
		}
		catch (Exception e)
		{
			_log.warning("TvTRound Event Engine : Error figuring out a start time. Check TvTRoundEventInterval in config file.");
		}
	}
	
	/**
	 * Method to start participation
	 */
	public void startReg()
	{
		if (!TvTRoundEvent.startParticipation())
		{
			Announcements.getInstance().announceToAll("TvT Round Event: Event was cancelled.");
			_log.warning("TvTRound Event Engine: Error spawning event npc for participation.");
			
			scheduleEventStart();
		}
		else
		{
			Announcements.getInstance().announceToAll("TvT Round Event: Registration opened for " + Config.TVT_ROUND_EVENT_PARTICIPATION_TIME + " minute(s).");
			
			// schedule registration end
			_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TVT_ROUND_EVENT_PARTICIPATION_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to start the first round
	 */
	public void startFirstRound()
	{
		if (!TvTRoundEvent.startEvent())
		{
			Announcements.getInstance().announceToAll("TvT Round Event: Event cancelled due to lack of Participation.");
			_log.info("TvTRound Event Engine: Lack of registration, abort event.");
			
			scheduleEventStart();
		}
		else
		{
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Starting first round...");
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Teleporting participants to the anteroom in " + Config.TVT_ROUND_EVENT_START_RESPAWN_LEAVE_TELEPORT_DELAY + " second(s).");
			_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TVT_ROUND_EVENT_FIRST_FIGHT_RUNNING_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to start the second round
	 */
	public void startSecondRound()
	{
		if (!TvTRoundEvent.startFights())
		{
			Announcements.getInstance().announceToAll("TvT Round Event: Second Round cancelled, there aren't enough participants.");
			_log.info("TvTRound Event Engine: There aren't enough participants, abort round.");
			TvTRoundEvent.setSecondRoundFinished();
			
			ThreadPoolManager.getInstance().executeTask(_task);
		}
		else
		{
			TvTRoundEvent.setInSecondRound();
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Starting second round...");
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Teleporting participants to the anteroom in " + Config.TVT_ROUND_EVENT_START_RESPAWN_LEAVE_TELEPORT_DELAY + " second(s).");
			_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TVT_ROUND_EVENT_SECOND_FIGHT_RUNNING_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to start the first round
	 */
	public void startThirdRound()
	{
		if (!TvTRoundEvent.startFights())
		{
			Announcements.getInstance().announceToAll("TvT Round Event: Third Round cancelled, there aren't enough participants.");
			Announcements.getInstance().announceToAll("TvT Round Event: Event finished without winners.");
			_log.info("TvTRound Event Engine: There aren't enough participants, abort event.");
			TvTRoundEvent.setIsWithoutWinners();
			
			ThreadPoolManager.getInstance().executeTask(_task);
		}
		else
		{
			TvTRoundEvent.setInThirdRound();
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Starting third round...");
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Teleporting participants to the anteroom in " + Config.TVT_ROUND_EVENT_START_RESPAWN_LEAVE_TELEPORT_DELAY + " second(s).");
			_task.setStartTime(System.currentTimeMillis() + (60000L * Config.TVT_ROUND_EVENT_THIRD_FIGHT_RUNNING_TIME));
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to end the first round
	 */
	public void endFirstRound()
	{
		Announcements.getInstance().announceToAll(TvTRoundEvent.calculatePoints());
		if (Config.TVT_ROUND_EVENT_STOP_ON_TIE && (TvTRoundEvent.getRoundTie() >= Config.TVT_ROUND_EVENT_MINIMUM_TIE))
		{
			TvTRoundEvent.stopEvent();
			Announcements.getInstance().announceToAll("TvT Round Event: Event cancelled due to inactivity!");
			
			scheduleEventStart();
		}
		else
		{
			TvTRoundEvent.cleanTeamsPoints();
			TvTRoundEvent.setFirstRoundFinished();
			
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to end the second round
	 */
	public void endSecondRound()
	{
		Announcements.getInstance().announceToAll(TvTRoundEvent.calculatePoints());
		if (Config.TVT_ROUND_EVENT_STOP_ON_TIE && (TvTRoundEvent.getRoundTie() >= Config.TVT_ROUND_EVENT_MINIMUM_TIE))
		{
			TvTRoundEvent.stopEvent();
			Announcements.getInstance().announceToAll("TvT Round Event: Event cancelled due to inactivity!");
			
			scheduleEventStart();
		}
		else if (Config.TVT_ROUND_EVENT_REWARD_ON_SECOND_FIGHT_END && TvTRoundEvent.checkForPossibleWinner())
		{
			Announcements.getInstance().announceToAll(TvTRoundEvent.calculateRewards());
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Teleporting back to the registration npc in " + Config.TVT_ROUND_EVENT_START_RESPAWN_LEAVE_TELEPORT_DELAY + " second(s).");
			TvTRoundEvent.stopEvent();
			
			scheduleEventStart();
		}
		else
		{
			TvTRoundEvent.cleanTeamsPoints();
			TvTRoundEvent.setSecondRoundFinished();
			
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to end the third round
	 */
	public void endThirdRound()
	{
		Announcements.getInstance().announceToAll(TvTRoundEvent.calculatePoints());
		if (Config.TVT_ROUND_EVENT_STOP_ON_TIE && (TvTRoundEvent.getRoundTie() >= Config.TVT_ROUND_EVENT_MINIMUM_TIE))
		{
			TvTRoundEvent.stopEvent();
			Announcements.getInstance().announceToAll("TvT Round Event: Event cancelled due to inactivity!");
			
			scheduleEventStart();
		}
		else
		{
			TvTRoundEvent.cleanTeamsPoints();
			TvTRoundEvent.setThirdRoundFinished();
			
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Method to end the event and reward
	 */
	public void endEvent()
	{
		if (TvTRoundEvent.isWithoutWinners())
		{
			TvTRoundEvent.stopEvent();
			scheduleEventStart();
		}
		else
		{
			Announcements.getInstance().announceToAll(TvTRoundEvent.calculateRewards());
			TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: Teleporting back to the registration npc in " + Config.TVT_ROUND_EVENT_START_RESPAWN_LEAVE_TELEPORT_DELAY + " second(s).");
			TvTRoundEvent.stopEvent();
			
			scheduleEventStart();
		}
	}
	
	public void skipDelay()
	{
		if (_task.nextRun.cancel(false))
		{
			_task.setStartTime(System.currentTimeMillis());
			ThreadPoolManager.getInstance().executeTask(_task);
		}
	}
	
	/**
	 * Class for TvT Round cycles
	 */
	class TvTRoundStartTask implements Runnable
	{
		private long _startTime;
		public ScheduledFuture<?> nextRun;
		
		public TvTRoundStartTask(long startTime)
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
				// start
				if (TvTRoundEvent.isInactive())
				{
					startReg();
				}
				else if (TvTRoundEvent.isParticipating())
				{
					startFirstRound();
				}
				else if (TvTRoundEvent.isInFirstRound())
				{
					endFirstRound();
				}
				else if (TvTRoundEvent.isFRoundFinished())
				{
					startSecondRound();
				}
				else if (TvTRoundEvent.isInSecondRound())
				{
					endSecondRound();
				}
				else if (TvTRoundEvent.isSRoundFinished())
				{
					startThirdRound();
				}
				else if (TvTRoundEvent.isInThirdRound())
				{
					endThirdRound();
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
				if (TvTRoundEvent.isParticipating())
				{
					Announcements.getInstance().announceToAll("TvT Round Event: " + (time / 60 / 60) + " hour(s) until registration is closed!");
				}
				else if (TvTRoundEvent.isStarted())
				{
					TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: " + (time / 60 / 60) + " hour(s) until event is finished!");
				}
			}
			else if (time >= 60)
			{
				if (TvTRoundEvent.isParticipating())
				{
					Announcements.getInstance().announceToAll("TvT Round Event: " + (time / 60) + " minute(s) until registration is closed!");
				}
				else if (TvTRoundEvent.isStarted())
				{
					TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: " + (time / 60) + " minute(s) until the round is finished!");
				}
			}
			else
			{
				if (TvTRoundEvent.isParticipating())
				{
					Announcements.getInstance().announceToAll("TvT Round Event: " + time + " second(s) until registration is closed!");
				}
				else if (TvTRoundEvent.isStarted())
				{
					TvTRoundEvent.sysMsgToAllParticipants("TvT Round Event: " + time + " second(s) until the round is finished!");
				}
			}
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final TvTRoundManager _instance = new TvTRoundManager();
	}
}