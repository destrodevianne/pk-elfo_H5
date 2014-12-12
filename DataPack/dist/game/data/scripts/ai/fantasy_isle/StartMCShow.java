package ai.fantasy_isle;

import pk.elfo.gameserver.instancemanager.QuestManager;
 
/**
 * Projeto PkElfo
 */

public class StartMCShow implements Runnable
{
	@Override
	public void run()
	{
		QuestManager.getInstance().getQuest("MC_Show").notifyEvent("Start", null, null);
	}
}