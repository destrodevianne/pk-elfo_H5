package pk.elfo.gameserver.taskmanager.tasks;

import java.util.Calendar;
import java.util.Map;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.instancemanager.RaidBossPointsManager;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.gameserver.taskmanager.Task;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.gameserver.taskmanager.TaskManager.ExecutedTask;
import pk.elfo.gameserver.taskmanager.TaskTypes;

public class TaskRaidPointsReset extends Task
{
	public static final String NAME = "raid_points_reset";
	
	@Override
	public String getName()
	{
		return NAME;
	}
	
	@Override
	public void onTimeElapsed(ExecutedTask task)
	{
		Calendar cal = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
		{
			// reward clan reputation points
			Map<Integer, Integer> rankList = RaidBossPointsManager.getInstance().getRankList();
			for (L2Clan c : ClanTable.getInstance().getClans())
			{
				for (Map.Entry<Integer, Integer> entry : rankList.entrySet())
				{
					if ((entry.getValue() <= 100) && c.isMember(entry.getKey()))
					{
						int reputation = 0;
						switch (entry.getValue())
						{
							case 1:
								reputation = Config.RAID_RANKING_1ST;
								break;
							case 2:
								reputation = Config.RAID_RANKING_2ND;
								break;
							case 3:
								reputation = Config.RAID_RANKING_3RD;
								break;
							case 4:
								reputation = Config.RAID_RANKING_4TH;
								break;
							case 5:
								reputation = Config.RAID_RANKING_5TH;
								break;
							case 6:
								reputation = Config.RAID_RANKING_6TH;
								break;
							case 7:
								reputation = Config.RAID_RANKING_7TH;
								break;
							case 8:
								reputation = Config.RAID_RANKING_8TH;
								break;
							case 9:
								reputation = Config.RAID_RANKING_9TH;
								break;
							case 10:
								reputation = Config.RAID_RANKING_10TH;
								break;
							default:
								if (entry.getValue() <= 50)
								{
									reputation = Config.RAID_RANKING_UP_TO_50TH;
								}
								else
								{
									reputation = Config.RAID_RANKING_UP_TO_100TH;
								}
								break;
						}
						c.addReputationScore(reputation, true);
					}
				}
			}
			
			RaidBossPointsManager.getInstance().cleanUp();
			_log.info("Raid Points Redefinir Tarefa Global: iniciado.");
		}
	}
	
	@Override
	public void initializate()
	{
		super.initializate();
		TaskManager.addUniqueTask(NAME, TaskTypes.TYPE_GLOBAL_TASK, "1", "00:10:00", "");
	}
}