package handlers.telnethandlers;

import java.io.File;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.script.ScriptException;

import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.datatables.MultiSell;
import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.datatables.TeleportLocationTable;
import pk.elfo.gameserver.handler.ITelnetHandler;
import pk.elfo.gameserver.instancemanager.DayNightSpawnManager;
import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.instancemanager.ZoneManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.scripting.L2ScriptEngineManager;

/**
 * PkElfo
 */

public class ReloadHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"reload"
	};
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.startsWith("reload"))
		{
			StringTokenizer st = new StringTokenizer(command.substring(7));
			try
			{
				String type = st.nextToken();
				
				if (type.equals("multisell"))
				{
					_print.print("Recarregando multisell... ");
					MultiSell.getInstance().reload();
					_print.println("done");
				}
				else if (type.equals("skill"))
				{
					_print.print("Recarregando skills... ");
					SkillTable.getInstance().reload();
					_print.println("done");
				}
				else if (type.equals("npc"))
				{
					_print.print("Recarregando npc templates... ");
					NpcTable.getInstance().reloadAllNpc();
					QuestManager.getInstance().reloadAllQuests();
					_print.println("done");
				}
				else if (type.equals("html"))
				{
					_print.print("Recarregando html cache... ");
					HtmCache.getInstance().reload();
					_print.println("done");
				}
				else if (type.equals("item"))
				{
					_print.print("Recarregando item templates... ");
					ItemTable.getInstance().reload();
					_print.println("done");
				}
				else if (type.equals("zone"))
				{
					_print.print("Recarregando zone tables... ");
					ZoneManager.getInstance().reload();
					_print.println("done");
				}
				else if (type.equals("teleports"))
				{
					_print.print("Recarregando telport location table... ");
					TeleportLocationTable.getInstance().reloadAll();
					_print.println("done");
				}
				else if (type.equals("spawns"))
				{
					_print.print("Recarregando spawns... ");
					RaidBossSpawnManager.getInstance().cleanUp();
					DayNightSpawnManager.getInstance().cleanUp();
					L2World.getInstance().deleteVisibleNpcSpawns();
					NpcTable.getInstance().reloadAllNpc();
					SpawnTable.getInstance().load();
					RaidBossSpawnManager.getInstance().load();
					_print.println("done\n");
				}
				else if (type.equalsIgnoreCase("script"))
				{
					try
					{
						String questPath = st.hasMoreTokens() ? st.nextToken() : "";
						
						File file = new File(L2ScriptEngineManager.SCRIPT_FOLDER, questPath);
						if (file.isFile())
						{
							try
							{
								L2ScriptEngineManager.getInstance().executeScript(file);
								_print.println(file.getName() + " foi carregado com exito!\n");
							}
							catch (ScriptException e)
							{
								_print.println("Falha ao ler: " + questPath);
								L2ScriptEngineManager.getInstance().reportScriptFileError(file, e);
							}
							catch (Exception e)
							{
								_print.println("Falha ao ler: " + questPath);
							}
						}
						else
						{
							_print.println(file.getName() + " nao e um arquivo em: " + questPath);
						}
					}
					catch (StringIndexOutOfBoundsException e)
					{
						_print.println("Por favor defina um texto!");
					}
				}
			}
			catch (Exception e)
			{
			}
		}
		return false;
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}