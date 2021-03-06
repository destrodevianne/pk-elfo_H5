package pk.elfo.gameserver.taskmanager.tasks;

import pk.elfo.gameserver.SevenSigns;
import pk.elfo.gameserver.SevenSignsFestival;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;
import pk.elfo.gameserver.taskmanager.TaskTypes;

public class TaskSevenSignsUpdate extends Task
{
	private static final String NAME = "seven_signs_update";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		try
		{
			SevenSigns.getInstance().saveSevenSignsStatus();
			if (!SevenSigns.getInstance().isSealValidationPeriod())
			{
				SevenSignsFestival.getInstance().saveFestivalData(false);
			}
			_log.info("SevenSigns: Dados atualizados com sucesso.");
		}
		catch (Exception e)
		{
			_log.warning(getClass().getSimpleName() + ": SevenSigns: Falha ao salvar configuracao de Seven Signs: " + e.getMessage());
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_FIXED_SHEDULED, "1800000", "1800000", "");
	}
}