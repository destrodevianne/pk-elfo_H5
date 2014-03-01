package handlers.telnethandlers;

import java.io.PrintWriter;
import java.net.Socket;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.handler.ITelnetHandler;

/**
 * PkElfo
 */

public class ThreadHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"purge",
		"performance"
	};
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.equals("performance"))
		{
			for (String line : ThreadPoolManager.getInstance().getStats())
			{
				_print.println(line);
			}
			_print.flush();
		}
		else if (command.equals("purge"))
		{
			ThreadPoolManager.getInstance().purge();
			_print.println("SITUACAO DA THREAD POOLS APOS REMOCAO DE COMANDO:");
			_print.println("");
			for (String line : ThreadPoolManager.getInstance().getStats())
			{
				_print.println(line);
			}
			_print.flush();
		}
		return false;
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}