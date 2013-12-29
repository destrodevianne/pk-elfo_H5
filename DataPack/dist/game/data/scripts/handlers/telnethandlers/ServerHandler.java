package handlers.telnethandlers;

import java.io.PrintWriter;
import java.net.Socket;

import pk.elfo.gameserver.Shutdown;
import pk.elfo.gameserver.handler.ITelnetHandler;

/**
 * PkElfo
 */
public class ServerHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"shutdown",
		"restart",
		"abort"
	};
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.startsWith("shutdown"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(9));
				Shutdown.getInstance().startTelnetShutdown(_cSocket.getInetAddress().getHostAddress(), val, false);
				_print.println("Server Will Shutdown In " + val + " Seconds!");
				_print.println("Type \"abort\" To Abort Shutdown!");
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Please Enter * amount of seconds to shutdown!");
			}
			catch (Exception NumberFormatException)
			{
				_print.println("Numbers Only!");
			}
		}
		else if (command.startsWith("restart"))
		{
			try
			{
				int val = Integer.parseInt(command.substring(8));
				Shutdown.getInstance().startTelnetShutdown(_cSocket.getInetAddress().getHostAddress(), val, true);
				_print.println("Server Will Restart In " + val + " Seconds!");
				_print.println("Type \"abort\" To Abort Restart!");
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Please Enter * amount of seconds to restart!");
			}
			catch (Exception NumberFormatException)
			{
				_print.println("Numbers Only!");
			}
		}
		else if (command.startsWith("abort"))
		{
			Shutdown.getInstance().telnetAbort(_cSocket.getInetAddress().getHostAddress());
			_print.println("OK! - Shutdown/Restart Aborted.");
		}
		return false;
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}
