package pk.elfo.gameserver.taskmanager.tasks;

import pk.elfo.gameserver.instancemanager.GlobalVariablesManager;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskTypes;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;

public class TaskGlobalVariablesSave extends Task
{
	public static final String NAME = "global_varibales_save";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		GlobalVariablesManager.getInstance().saveVars();
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "500000", "1800000", "");
	}
}