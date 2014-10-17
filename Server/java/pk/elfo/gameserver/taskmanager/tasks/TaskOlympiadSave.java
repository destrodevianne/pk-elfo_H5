package pk.elfo.gameserver.taskmanager.tasks;

import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;
import pk.elfo.gameserver.taskmanager.TaskTypes;

public class TaskOlympiadSave extends Task
{
	public static final String NAME = "olympiad_save";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		if (Olympiad.getInstance().inCompPeriod())
		{
			Olympiad.getInstance().saveOlympiadStatus();
			_log.info("Olympiad System: Dados atualizados.");
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "900000", "1800000", "");
	}
}