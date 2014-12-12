package handlers.telnethandlers;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.handler.ITelnetHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;

/**
 * Projeto PkElfo
 */

public class ChatsHandler implements ITelnetHandler
{
	private final String[] _commands =
	{
		"announce",
		"msg",
		"gmchat"
	};
	
	@Override
	public boolean useCommand(String command, PrintWriter _print, Socket _cSocket, int _uptime)
	{
		if (command.startsWith("announce"))
		{
			try
			{
				command = command.substring(9);
				Announcements.getInstance().announceToAll(command);
				_print.println("Anuncio enviado!");
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Por favor defina um texto para anunciar!");
			}
		}
		else if (command.startsWith("msg"))
		{
			try
			{
				String val = command.substring(4);
				StringTokenizer st = new StringTokenizer(val);
				String name = st.nextToken();
				String message = val.substring(name.length() + 1);
				L2PcInstance reciever = L2World.getInstance().getPlayer(name);
				CreatureSay cs = new CreatureSay(0, Say2.TELL, "Telnet Priv", message);
				if (reciever != null)
				{
					reciever.sendPacket(cs);
					_print.println("Telnet Priv->" + name + ": " + message);
					_print.println("Mensagem enviada!");
				}
				else
				{
					_print.println("Nao foi possivel encontrar o Usuario: " + name);
				}
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Por favor defina um texto!");
			}
		}
		else if (command.startsWith("gmchat"))
		{
			try
			{
				command = command.substring(7);
				CreatureSay cs = new CreatureSay(0, Say2.ALLIANCE, "Telnet GM Broadcast from " + _cSocket.getInetAddress().getHostAddress(), command);
				AdminTable.getInstance().broadcastToGMs(cs);
				_print.println("Sua mensagem foi enviada " + getOnlineGMS() + " GM(s).");
			}
			catch (StringIndexOutOfBoundsException e)
			{
				_print.println("Por favor defina um texto para anunciar!");
			}
		}
		return false;
	}
	
	private int getOnlineGMS()
	{
		return AdminTable.getInstance().getAllGms(true).size();
	}
	
	@Override
	public String[] getCommandList()
	{
		return _commands;
	}
}