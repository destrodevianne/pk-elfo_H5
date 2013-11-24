package king.server.gameserver.taskmanager.tasks;

import king.server.gameserver.taskmanager.Task;
import king.server.gameserver.taskmanager.TaskManager.ExecutedTask;

import org.python.util.PythonInterpreter;

public class TaskJython extends Task
{
	public static final String NAME = "jython";
	private final PythonInterpreter _python = new PythonInterpreter();
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		_python.cleanup();
		_python.exec("import sys");
		_python.execfile("data/scripts/cron/" + task.getParams()[2]);
	}
}