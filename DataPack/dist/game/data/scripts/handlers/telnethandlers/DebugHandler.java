package handlers.telnethandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import pk.elfo.Config;
import pk.elfo.gameserver.GameTimeController;
import pk.elfo.gameserver.LoginServerThread;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.handler.ITelnetHandler;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2DoorInstance;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.network.serverpackets.AdminForgePacket;
import pk.elfo.gameserver.taskmanager.DecayTaskManager;
import javolution.util.FastComparator;
import javolution.util.FastTable;

/**
 * Projeto PkElfo
 */

public class DebugHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"debug"
	};
	
	private int uptime = 0;
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.startsWith("debug") && (command.length() > 6))
		{
			StringTokenizer st = new StringTokenizer(command.substring(6));
			// TODO: Rewrite to use ARM.
			FileOutputStream fos = null;
			OutputStreamWriter out = null;
			try
			{
				String dbg = st.nextToken();
				
				if (dbg.equals("decay"))
				{
					_print.print(DecayTaskManager.getInstance().toString());
				}
				else if (dbg.equals("packetsend"))
				{
					if (st.countTokens() < 2)
					{
						_print.println("Use: debug packetsend <charName> <packetData>");
						return false;
					}
					String charName = st.nextToken();
					L2PcInstance targetPlayer = L2World.getInstance().getPlayer(charName);
					
					if (targetPlayer == null)
					{
						_print.println("O Jogador " + charName + " nao esta online");
						return false;
					}
					
					AdminForgePacket sp = new AdminForgePacket();
					while (st.hasMoreTokens())
					{
						String b = st.nextToken();
						if (!b.isEmpty())
						{
							sp.addPart("C".getBytes()[0], "0x" + b);
						}
					}
					targetPlayer.sendPacket(sp);
					_print.println("Packet enviado para o jogador " + charName);
				}
				else if (dbg.equals("PacketTP"))
				{
					String str = ThreadPoolManager.getInstance().getPacketStats();
					_print.println(str);
					int i = 0;
					File f = new File("./log/StackTrace-PacketTP-" + i + ".txt");
					while (f.exists())
					{
						i++;
						f = new File("./log/StackTrace-PacketTP-" + i + ".txt");
					}
					f.getParentFile().mkdirs();
					fos = new FileOutputStream(f);
					out = new OutputStreamWriter(fos, "UTF-8");
					out.write(str);
				}
				else if (dbg.equals("IOPacketTP"))
				{
					String str = ThreadPoolManager.getInstance().getIOPacketStats();
					_print.println(str);
					int i = 0;
					File f = new File("./log/StackTrace-IOPacketTP-" + i + ".txt");
					while (f.exists())
					{
						i++;
						f = new File("./log/StackTrace-IOPacketTP-" + i + ".txt");
					}
					f.getParentFile().mkdirs();
					fos = new FileOutputStream(f);
					out = new OutputStreamWriter(fos, "UTF-8");
					out.write(str);
				}
				else if (dbg.equals("GeneralTP"))
				{
					String str = ThreadPoolManager.getInstance().getGeneralStats();
					_print.println(str);
					int i = 0;
					File f = new File("./log/StackTrace-GeneralTP-" + i + ".txt");
					while (f.exists())
					{
						i++;
						f = new File("./log/StackTrace-GeneralTP-" + i + ".txt");
					}
					f.getParentFile().mkdirs();
					fos = new FileOutputStream(f);
					out = new OutputStreamWriter(fos, "UTF-8");
					out.write(str);
				}
				else if (dbg.equals("full"))
				{
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
					
					StringBuilder sb = new StringBuilder();
					sb.append(sdf.format(cal.getTime()));
					sb.append("\n\nL2J Core Versao: " + Config.SERVER_VERSION);
					sb.append("\nDP Revisao: " + Config.DATAPACK_VERSION);
					sb.append("\n\n");
					uptime = _uptime;
					sb.append(getServerStatus());
					sb.append("\n\n");
					sb.append("\n## Java Platforma Informacao ##");
					sb.append("\nJava Runtime Nome: " + System.getProperty("java.runtime.name"));
					sb.append("\nJava Versao: " + System.getProperty("java.version"));
					sb.append("\nJava Class Versao: " + System.getProperty("java.class.version"));
					sb.append('\n');
					sb.append("\n## Maquina Virtual Informacao ##");
					sb.append("\nVM Nome: " + System.getProperty("java.vm.name"));
					sb.append("\nVM Versao: " + System.getProperty("java.vm.version"));
					sb.append("\nVM Vendedor: " + System.getProperty("java.vm.vendor"));
					sb.append("\nVM Info: " + System.getProperty("java.vm.info"));
					sb.append('\n');
					sb.append("\n## OS Informacao ##");
					sb.append("\nNome: " + System.getProperty("os.name"));
					sb.append("\nArquitetura: " + System.getProperty("os.arch"));
					sb.append("\nVersao: " + System.getProperty("os.version"));
					sb.append('\n');
					sb.append("\n## Runtime Informacao ##");
					sb.append("\nCPU Count: " + Runtime.getRuntime().availableProcessors());
					sb.append("\nCorrnte Free Heap Size: " + (Runtime.getRuntime().freeMemory() / 1024 / 1024) + " mb");
					sb.append("\nCorrnte Heap Size: " + (Runtime.getRuntime().totalMemory() / 1024 / 1024) + " mb");
					sb.append("\nMaximo Heap Size: " + (Runtime.getRuntime().maxMemory() / 1024 / 1024) + " mb");
					sb.append('\n');
					sb.append("\n## Class Path Informacao ##\n");
					String cp = System.getProperty("java.class.path");
					String[] libs = cp.split(File.pathSeparator);
					for (String lib : libs)
					{
						sb.append(lib);
						sb.append('\n');
					}
					sb.append('\n');
					sb.append("## Threads Informacao ##\n");
					Map<Thread, StackTraceElement[]> allThread = Thread.getAllStackTraces();
					
					FastTable<Entry<Thread, StackTraceElement[]>> entries = new FastTable<>();
					entries.setValueComparator(new FastComparator<Entry<Thread, StackTraceElement[]>>()
					{
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
						
						@Override
						public boolean areEqual(Entry<Thread, StackTraceElement[]> e1, Entry<Thread, StackTraceElement[]> e2)
						{
							return e1.getKey().getName().equals(e2.getKey().getName());
						}
						
						@Override
						public int compare(Entry<Thread, StackTraceElement[]> e1, Entry<Thread, StackTraceElement[]> e2)
						{
							return e1.getKey().getName().compareTo(e2.getKey().getName());
						}
						
						@Override
						public int hashCodeOf(Entry<Thread, StackTraceElement[]> e)
						{
							return e.hashCode();
						}
					});
					entries.addAll(allThread.entrySet());
					entries.sort();
					for (Entry<Thread, StackTraceElement[]> entry : entries)
					{
						StackTraceElement[] stes = entry.getValue();
						Thread t = entry.getKey();
						sb.append("--------------\n");
						sb.append(t.toString() + " (" + t.getId() + ")\n");
						sb.append("State: " + t.getState() + '\n');
						sb.append("isAlive: " + t.isAlive() + " | isDaemon: " + t.isDaemon() + " | isInterrupted: " + t.isInterrupted() + '\n');
						sb.append('\n');
						for (StackTraceElement ste : stes)
						{
							sb.append(ste.toString());
							sb.append('\n');
						}
						sb.append('\n');
					}
					
					sb.append('\n');
					ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
					long[] ids = findDeadlockedThreads(mbean);
					if ((ids != null) && (ids.length > 0))
					{
						Thread[] threads = new Thread[ids.length];
						for (int i = 0; i < threads.length; i++)
						{
							threads[i] = findMatchingThread(mbean.getThreadInfo(ids[i]));
						}
						sb.append("Deadlocked Threads:\n");
						sb.append("-------------------\n");
						for (Thread thread : threads)
						{
							System.err.println(thread);
							for (StackTraceElement ste : thread.getStackTrace())
							{
								sb.append("\t" + ste);
								sb.append('\n');
							}
						}
					}
					
					sb.append("\n\n## Thread Pool Estatisticas do Gerenciador ##\n");
					for (String line : ThreadPoolManager.getInstance().getStats())
					{
						sb.append(line);
						sb.append('\n');
					}
					
					int i = 0;
					File f = new File("./log/Debug-" + i + ".txt");
					while (f.exists())
					{
						i++;
						f = new File("./log/Debug-" + i + ".txt");
					}
					f.getParentFile().mkdirs();
					fos = new FileOutputStream(f);
					out = new OutputStreamWriter(fos, "UTF-8");
					out.write(sb.toString());
					out.flush();
					out.close();
					fos.close();
					
					_print.println("Saida de depuracao salvar a sessao/" + f.getName());
					_print.flush();
				}
			}
			catch (Exception e)
			{
			}
			finally
			{
				try
				{
					if (out != null)
					{
						out.close();
					}
				}
				catch (Exception e)
				{
				}
				
				try
				{
					if (fos != null)
					{
						fos.close();
					}
				}
				catch (Exception e)
				{
				}
			}
			
		}
		return false;
	}
	
	private long[] findDeadlockedThreads(ThreadMXBean mbean)
	{
		// JDK 1.5 only supports the findMonitorDeadlockedThreads()
		// method, so you need to comment out the following three lines
		if (mbean.isSynchronizerUsageSupported())
		{
			return mbean.findDeadlockedThreads();
		}
		return mbean.findMonitorDeadlockedThreads();
	}
	
	private Thread findMatchingThread(ThreadInfo inf)
	{
		for (Thread thread : Thread.getAllStackTraces().keySet())
		{
			if (thread.getId() == inf.getThreadId())
			{
				return thread;
			}
		}
		throw new IllegalStateException("Deadlocked Thread nao encontrado");
	}
	
	public String getServerStatus()
	{
		int playerCount = 0, objectCount = 0;
		int max = LoginServerThread.getInstance().getMaxPlayer();
		
		playerCount = L2World.getInstance().getAllPlayersCount();
		objectCount = L2World.getInstance().getAllVisibleObjectsCount();
		
		int itemCount = 0;
		int itemVoidCount = 0;
		int monsterCount = 0;
		int minionCount = 0;
		int minionsGroupCount = 0;
		int npcCount = 0;
		int charCount = 0;
		int pcCount = 0;
		int detachedCount = 0;
		int doorCount = 0;
		int summonCount = 0;
		int AICount = 0;
		
		L2Object[] objs = L2World.getInstance().getAllVisibleObjectsArray();
		for (L2Object obj : objs)
		{
			if (obj == null)
			{
				continue;
			}
			if (obj instanceof L2Character)
			{
				if (((L2Character) obj).hasAI())
				{
					AICount++;
				}
			}
			if (obj instanceof L2ItemInstance)
			{
				if (((L2ItemInstance) obj).getLocation() == L2ItemInstance.ItemLocation.VOID)
				{
					itemVoidCount++;
				}
				else
				{
					itemCount++;
				}
			}
			else if (obj instanceof L2MonsterInstance)
			{
				monsterCount++;
				if (((L2MonsterInstance) obj).hasMinions())
				{
					minionCount += ((L2MonsterInstance) obj).getMinionList().countSpawnedMinions();
					minionsGroupCount += ((L2MonsterInstance) obj).getMinionList().lazyCountSpawnedMinionsGroups();
				}
			}
			else if (obj instanceof L2Npc)
			{
				npcCount++;
			}
			else if (obj instanceof L2PcInstance)
			{
				pcCount++;
				if ((((L2PcInstance) obj).getClient() != null) && ((L2PcInstance) obj).getClient().isDetached())
				{
					detachedCount++;
				}
			}
			else if (obj instanceof L2Summon)
			{
				summonCount++;
			}
			else if (obj instanceof L2DoorInstance)
			{
				doorCount++;
			}
			else if (obj instanceof L2Character)
			{
				charCount++;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Server Status: ");
		sb.append("\r\n  --->  Player Count: " + playerCount + "/" + max);
		sb.append("\r\n  ---> Offline Count: " + detachedCount + "/" + playerCount);
		sb.append("\r\n  +-->  Object Count: " + objectCount);
		sb.append("\r\n  +-->      AI Count: " + AICount);
		sb.append("\r\n  +.... L2Item(Void): " + itemVoidCount);
		sb.append("\r\n  +.......... L2Item: " + itemCount);
		sb.append("\r\n  +....... L2Monster: " + monsterCount);
		sb.append("\r\n  +......... Minions: " + minionCount);
		sb.append("\r\n  +.. Minions Groups: " + minionsGroupCount);
		sb.append("\r\n  +........... L2Npc: " + npcCount);
		sb.append("\r\n  +............ L2Pc: " + pcCount);
		sb.append("\r\n  +........ L2Summon: " + summonCount);
		sb.append("\r\n  +.......... L2Door: " + doorCount);
		sb.append("\r\n  +.......... L2Char: " + charCount);
		sb.append("\r\n  --->   Ingame Time: " + gameTime());
		sb.append("\r\n  ---> Server Uptime: " + getUptime(uptime));
		sb.append("\r\n  --->      GM Count: " + getOnlineGMS());
		sb.append("\r\n  --->       Threads: " + Thread.activeCount());
		sb.append("\r\n  RAM Useda: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576)); // 1024 * 1024 = 1048576
		sb.append("\r\n");
		
		return sb.toString();
	}
	
	private int getOnlineGMS()
	{
		return AdminTable.getInstance().getAllGms(true).size();
	}
	
	private String getUptime(int time)
	{
		int uptime = (int) System.currentTimeMillis() - time;
		uptime = uptime / 1000;
		int h = uptime / 3600;
		int m = (uptime - (h * 3600)) / 60;
		int s = ((uptime - (h * 3600)) - (m * 60));
		return h + "hrs " + m + "mins " + s + "secs";
	}
	
	private String gameTime()
	{
		int t = GameTimeController.getInstance().getGameTime();
		int h = t / 60;
		int m = t % 60;
		SimpleDateFormat format = new SimpleDateFormat("H:mm");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, h);
		cal.set(Calendar.MINUTE, m);
		return format.format(cal.getTime());
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}