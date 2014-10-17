package pk.elfo.gameserver.taskmanager;

import static pk.elfo.gameserver.taskmanager.TaskTypes.TYPE_NONE;
import static pk.elfo.gameserver.taskmanager.TaskTypes.TYPE_SHEDULED;
import static pk.elfo.gameserver.taskmanager.TaskTypes.TYPE_TIME;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.taskmanager.tasks.SoIStageUpdater;
import pk.elfo.gameserver.taskmanager.tasks.TaskBirthday;
import pk.elfo.gameserver.taskmanager.tasks.TaskCleanUp;
import pk.elfo.gameserver.taskmanager.tasks.TaskDailySkillReuseClean;
import pk.elfo.gameserver.taskmanager.tasks.TaskGlobalVariablesSave;
import pk.elfo.gameserver.taskmanager.tasks.TaskHappyHourEnd;
import pk.elfo.gameserver.taskmanager.tasks.TaskHappyHourStart;
import pk.elfo.gameserver.taskmanager.tasks.TaskJython;
import pk.elfo.gameserver.taskmanager.tasks.TaskOlympiadSave;
import pk.elfo.gameserver.taskmanager.tasks.TaskRaidPointsReset;
import pk.elfo.gameserver.taskmanager.tasks.TaskRecom;
import pk.elfo.gameserver.taskmanager.tasks.TaskRestart;
import pk.elfo.gameserver.taskmanager.tasks.TaskScript;
import pk.elfo.gameserver.taskmanager.tasks.TaskSevenSignsUpdate;
import pk.elfo.gameserver.taskmanager.tasks.TaskShutdown;
import pk.elfo.util.L2FastList;
import pk.elfo.util.L2FastMap;

public final class TaskManager
{
	protected static final Logger _log = Logger.getLogger(TaskManager.class.getName());
	
	private final Map<Integer, Task> _tasks = new L2FastMap<>(true);
	protected final List<ExecutedTask> _currentTasks = new L2FastList<>(true);
	
	protected static final String[] SQL_STATEMENTS =
	{
		"SELECT id,task,type,last_activation,param1,param2,param3 FROM global_tasks",
		"UPDATE global_tasks SET last_activation=? WHERE id=?",
		"SELECT id FROM global_tasks WHERE task=?",
		"INSERT INTO global_tasks (task,type,last_activation,param1,param2,param3) VALUES(?,?,?,?,?,?)"
	};
	
	protected TaskManager()
	{
		initializate();
		startAllTasks();
		_log.log(Level.INFO, getClass().getSimpleName() + ": " + _tasks.size() + " Tasks");
	}
	
	public class ExecutedTask implements Runnable
	{
		int id;
		long lastActivation;
		Task task;
		TaskTypes type;
		String[] params;
		ScheduledFuture<?> scheduled;
		
		public ExecutedTask(Task ptask, TaskTypes ptype, ResultSet rset) throws SQLException
		{
			task = ptask;
			type = ptype;
			id = rset.getInt("id");
			lastActivation = rset.getLong("last_activation");
			params = new String[]
			{
				rset.getString("param1"),
				rset.getString("param2"),
				rset.getString("param3")
			};
		}
		
		@Override
		public void run()
		{
			task.onTimeElapsed(this);
			lastActivation = System.currentTimeMillis();
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[1]))
			{
				statement.setLong(1, lastActivation);
				statement.setInt(2, id);
				statement.executeUpdate();
			}
			catch (SQLException e)
			{
				_log.log(Level.WARNING, getClass().getSimpleName() + ": Cannot updated the Global Task " + id + ": " + e.getMessage(), e);
			}
			
			if ((type == TYPE_SHEDULED) || (type == TYPE_TIME))
			{
				stopTask();
			}
		}
		
		@Override
		public boolean equals(Object object)
		{
			if (this == object)
			{
				return true;
			}
			if (!(object instanceof ExecutedTask))
			{
				return false;
			}
			return id == ((ExecutedTask) object).id;
		}
		
		@Override
		public int hashCode()
		{
			return id;
		}
		
		public Task getTask()
		{
			return task;
		}
		
		public TaskTypes getType()
		{
			return type;
		}
		
		public int getId()
		{
			return id;
		}
		
		public String[] getParams()
		{
			return params;
		}
		
		public long getLastActivation()
		{
			return lastActivation;
		}
		
		public void stopTask()
		{
			task.onDestroy();
			
			if (scheduled != null)
			{
				scheduled.cancel(true);
			}
			
			_currentTasks.remove(this);
		}
	}
	
	private void initializate()
	{
		registerTask(new SoIStageUpdater());
		registerTask(new TaskBirthday());
		registerTask(new TaskCleanUp());
		registerTask(new TaskDailySkillReuseClean());
		registerTask(new TaskGlobalVariablesSave());
		registerTask(new TaskJython());
		registerTask(new TaskOlympiadSave());
		registerTask(new TaskRaidPointsReset());
		registerTask(new TaskRecom());
		registerTask(new TaskRestart());
		registerTask(new TaskScript());
		registerTask(new TaskSevenSignsUpdate());
		registerTask(new TaskShutdown());
		registerTask(new TaskHappyHourStart());
		registerTask(new TaskHappyHourEnd());
	}
	
	public void registerTask(Task task)
	{
		int key = task.getName().hashCode();
		if (!_tasks.containsKey(key))
		{
			_tasks.put(key, task);
			task.initializate();
		}
	}
	
	private void startAllTasks()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[0]);
			ResultSet rset = statement.executeQuery())
		{
			while (rset.next())
			{
				Task task = _tasks.get(rset.getString("task").trim().toLowerCase().hashCode());
				if (task == null)
				{
					continue;
				}
				
				final TaskTypes type = TaskTypes.valueOf(rset.getString("type"));
				if (type != TYPE_NONE)
				{
					ExecutedTask current = new ExecutedTask(task, type, rset);
					if (launchTask(current))
					{
						_currentTasks.add(current);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": Error while loading Global Task table: " + e.getMessage(), e);
		}
	}
	
	private boolean launchTask(ExecutedTask task)
	{
		final ThreadPoolManager scheduler = ThreadPoolManager.getInstance();
		final TaskTypes type = task.getType();
		long delay, interval;
		switch (type)
		{
			case TYPE_STARTUP:
				task.run();
				return false;
			case TYPE_SHEDULED:
				delay = Long.valueOf(task.getParams()[0]);
				task.scheduled = scheduler.scheduleGeneral(task, delay);
				return true;
				//--------------------comeco do codigo do evento ------------------------------------------//
            case TYPE_SCHEDULED_PER_DAY:
                    String[] hour_spd = task.getParams()[1].split(":");
                    if (hour_spd.length != 3)
                    {
                            _log.warning("Task " + task.getId() + " no esta correctamente formateado.");
                            return false;
                    }
                   
                    // Converte o tempo programado para um objeto de calendario
                    Calendar scheduledTime = Calendar.getInstance();
                    scheduledTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_spd[0]));
                    scheduledTime.set(Calendar.MINUTE, Integer.parseInt(hour_spd[1]));
                    scheduledTime.set(Calendar.SECOND, Integer.parseInt(hour_spd[2]));
                   
                    long timeLeft = scheduledTime.getTimeInMillis() - System.currentTimeMillis();
                   
                    _log.info("Evento programado " + scheduledTime.get(Calendar.HOUR_OF_DAY) + ":" + scheduledTime.get(Calendar.MINUTE));
                    if (timeLeft > 0)
                    {
                            task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, timeLeft, 86400000L); // Executado uma vez por dia
                            _log.info("Quedan " + (timeLeft / 1000) + " segundos.");
                    }
                    else
                    {
                            // Se o acontecimento termina no dia seguinte
                            _log.info("Evento pasado al siguiente dia, ID: " + task.getId());
                            task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, timeLeft + 86400000L, 86400000L);
                            _log.info("Quedan " + (timeLeft + (86400000L / 3600000)) + " horas.");
                    }
                    return true;
                    //--------------------fim do codigo do evento ------------------------------------------//
			case TYPE_FIXED_SHEDULED:
				delay = Long.valueOf(task.getParams()[0]);
				interval = Long.valueOf(task.getParams()[1]);
				task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);
				return true;
			case TYPE_TIME:
				try
				{
					Date desired = DateFormat.getInstance().parse(task.getParams()[0]);
					long diff = desired.getTime() - System.currentTimeMillis();
					if (diff >= 0)
					{
						task.scheduled = scheduler.scheduleGeneral(task, diff);
						return true;
					}
					_log.info(getClass().getSimpleName() + ": Task " + task.getId() + " is obsoleted.");
				}
				catch (Exception e) { }
				break;
			case TYPE_SPECIAL:
				ScheduledFuture<?> result = task.getTask().launchSpecial(task);
				if (result != null)
				{
					task.scheduled = result;
					return true;
				}
				break;
			case TYPE_GLOBAL_TASK:
				interval = Long.valueOf(task.getParams()[0]) * 86400000L;
				String[] hour = task.getParams()[1].split(":");
				
				if (hour.length != 3)
				{
					_log.warning(getClass().getSimpleName() + ": Task " + task.getId() + " has incorrect parameters");
					return false;
				}
				
				Calendar check = Calendar.getInstance();
				check.setTimeInMillis(task.getLastActivation() + interval);
				
				Calendar min = Calendar.getInstance();
				try
				{
					min.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour[0]));
					min.set(Calendar.MINUTE, Integer.parseInt(hour[1]));
					min.set(Calendar.SECOND, Integer.parseInt(hour[2]));
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, getClass().getSimpleName() + ": Bad parameter on task " + task.getId() + ": " + e.getMessage(), e);
					return false;
				}
				
				delay = min.getTimeInMillis() - System.currentTimeMillis();
				
				if (check.after(min) || (delay < 0))
				{
					delay += interval;
				}
				task.scheduled = scheduler.scheduleGeneralAtFixedRate(task, delay, interval);
				return true;
			default:
				return false;
		}
		return false;
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addUniqueTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addUniqueTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps1 = con.prepareStatement(SQL_STATEMENTS[2]))
		{
			ps1.setString(1, task);
			try (ResultSet rs = ps1.executeQuery())
			{
				if (!rs.next())
				{
					try (PreparedStatement ps2 = con.prepareStatement(SQL_STATEMENTS[3]))
					{
						ps2.setString(1, task);
						ps2.setString(2, type.toString());
						ps2.setLong(3, lastActivation);
						ps2.setString(4, param1);
						ps2.setString(5, param2);
						ps2.setString(6, param3);
						ps2.execute();
					}
				}
			}
			return true;
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, TaskManager.class.getSimpleName() + ": Cannot add the unique task: " + e.getMessage(), e);
		}
		return false;
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3)
	{
		return addTask(task, type, param1, param2, param3, 0);
	}
	
	public static boolean addTask(String task, TaskTypes type, String param1, String param2, String param3, long lastActivation)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(SQL_STATEMENTS[3]))
		{
			statement.setString(1, task);
			statement.setString(2, type.toString());
			statement.setLong(3, lastActivation);
			statement.setString(4, param1);
			statement.setString(5, param2);
			statement.setString(6, param3);
			statement.execute();
			return true;
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, TaskManager.class.getSimpleName() + ": Cannot add the task:  " + e.getMessage(), e);
		}
		return false;
	}
	
	public static TaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final TaskManager _instance = new TaskManager();
	}
}