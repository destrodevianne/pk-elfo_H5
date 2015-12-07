package pk.elfo.gameserver.taskmanager.tasks;

import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;

public final class TaskCleanUp extends Task
{
	public static final String NAME = "clean_up";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		System.runFinalization();
		System.gc();
	}
}