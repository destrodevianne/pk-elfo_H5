package handlers.admincommandhandlers;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.handler.IAdminCommandHandler;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

/**
 * This class handles following admin commands: - delete = deletes target
 * Projeto PkElfo
 */
public class AdminDelete implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_delete"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (command.equals("admin_delete"))
		{
			handleDelete(activeChar);
		}
		return true;
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	// TODO: add possibility to delete any L2Object (except L2PcInstance)
	private void handleDelete(L2PcInstance activeChar)
	{
		L2Object obj = activeChar.getTarget();
		if (obj instanceof L2Npc)
		{
			L2Npc target = (L2Npc) obj;
			target.deleteMe();
			
			L2Spawn spawn = target.getSpawn();
			if (spawn != null)
			{
				spawn.stopRespawn();
				
				if (RaidBossSpawnManager.getInstance().isDefined(spawn.getNpcid()))
				{
					RaidBossSpawnManager.getInstance().deleteSpawn(spawn, true);
				}
				else
				{
					SpawnTable.getInstance().deleteSpawn(spawn, true);
				}
			}
			
			activeChar.sendMessage("Excluido " + target.getName() + " a partir de " + target.getObjectId() + ".");
		}
		else
		{
			activeChar.sendMessage("Alvo incorreto.");
		}
	}
}