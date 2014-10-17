package pk.elfo.gameserver.taskmanager.tasks;

import java.io.File;

import javax.script.ScriptException;

import pk.elfo.gameserver.scripting.L2ScriptEngineManager;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;

public class TaskScript extends Task
{
	public static final String NAME = "script";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		final File file = new File(L2ScriptEngineManager.SCRIPT_FOLDER, "cron/" + task.getParams()[2]);
		if (file.isFile())
		{
			try
			{
				L2ScriptEngineManager.getInstance().executeScript(file);
			}
			catch (ScriptException e)
			{
				_log.warning("Falha ao ler: " + task.getParams()[2]);
				L2ScriptEngineManager.getInstance().reportScriptFileError(file, e);
			}
			catch (Exception e)
			{
				_log.warning("Falha ao ler: " + task.getParams()[2]);
			}
		}
		else
		{
			_log.warning("Arquivo nao encontrado: " + task.getParams()[2]);
		}
	}
}