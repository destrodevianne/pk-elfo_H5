package pk.elfo.status;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.loginserver.GameServerTable;
import pk.elfo.loginserver.L2LoginServer;
import pk.elfo.loginserver.LoginController;

public class LoginStatusThread extends Thread
{
	private static final Logger _log = Logger.getLogger(LoginStatusThread.class.getName());
	private final Socket _cSocket;
	private final PrintWriter _print;
	private final BufferedReader _read;
	private boolean _redirectLogger;
	private void telnetOutput(int type, String text)
	{
		if (type == 1)
		{
			System.out.println("TELNET | " + text);
		}
		else if (type == 2)
		{
			System.out.print("TELNET | " + text);
		}
		else if (type == 3)
		{
			System.out.print(text);
		}
		else if (type == 4)
		{
			System.out.println(text);
		}
		else
		{
			System.out.println("TELNET | " + text);
		}
	}
	
	private boolean isValidIP(Socket client)
	{
		boolean result = false;
		InetAddress ClientIP = client.getInetAddress();
		
		// convert IP to String, and compare with list
		String clientStringIP = ClientIP.getHostAddress();
		
		telnetOutput(1, "Connection from: " + clientStringIP);
		
		// read and loop thru list of IPs, compare with newIP
		if (Config.DEVELOPER)
		{
			telnetOutput(2, "");
		}
		
		final File file = new File(Config.TELNET_FILE);
		try (InputStream telnetIS = new FileInputStream(file))
		{
			Properties telnetSettings = new Properties();
			telnetSettings.load(telnetIS);
			
			String HostList = telnetSettings.getProperty("ListOfHosts", "127.0.0.1,localhost,::1");
			
			if (Config.DEVELOPER)
			{
				telnetOutput(3, "Comparing ip to list...");
			}
			
			// compare
			String ipToCompare = null;
			for (String ip : HostList.split(","))
			{
				if (!result)
				{
					ipToCompare = InetAddress.getByName(ip).getHostAddress();
					if (clientStringIP.equals(ipToCompare))
					{
						result = true;
					}
					if (Config.DEVELOPER)
					{
						telnetOutput(3, clientStringIP + " = " + ipToCompare + "(" + ip + ") = " + result);
					}
				}
			}
		}
		catch (IOException e)
		{
			if (Config.DEVELOPER)
			{
				telnetOutput(4, "");
			}
			telnetOutput(1, "Error: " + e);
		}
		
		if (Config.DEVELOPER)
		{
			telnetOutput(4, "Allow IP: " + result);
		}
		return result;
	}
	
	public LoginStatusThread(Socket client, int uptime, String StatusPW) throws IOException
	{
		_cSocket = client;
		
		_print = new PrintWriter(_cSocket.getOutputStream());
		_read = new BufferedReader(new InputStreamReader(_cSocket.getInputStream()));
		
		if (isValidIP(client))
		{
			telnetOutput(1, client.getInetAddress().getHostAddress() + " accepted.");
			_print.println("Bem Vindo a sessao do L2PkElfo Telnet.");
			_print.println("Por favor insira a senha!");
			_print.print("Senha: ");
			_print.flush();
			String tmpLine = _read.readLine();
			if (tmpLine == null)
			{
				_print.println("Erro.");
				_print.println("Desconectado...");
				_print.flush();
				_cSocket.close();
			}
			else
			{
				if (!tmpLine.equals(StatusPW))
				{
					_print.println("Senha incorreta!");
					_print.println("Desconectado...");
					_print.flush();
					_cSocket.close();
				}
				else
				{
					_print.println("Senha Correta!");
					_print.println("[L2PkElfo Login Server]");
					_print.print("");
					_print.flush();
					start();
				}
			}
		}
		else
		{
			telnetOutput(5, "Connection attempt from " + client.getInetAddress().getHostAddress() + " rejected.");
			_cSocket.close();
		}
	}
	
	@Override
	public void run()
	{
		String _usrCommand = "";
		try
		{
			while ((_usrCommand.compareTo("quit") != 0) && (_usrCommand.compareTo("exit") != 0))
			{
				_usrCommand = _read.readLine();
				if (_usrCommand == null)
				{
					_cSocket.close();
					break;
				}
				if (_usrCommand.equals("help"))
				{
					_print.println("O que se segue e uma lista de todos os comandos disponiveis: ");
					_print.println("help                - mostra esta ajuda.");
					_print.println("status              - exibe estatisticas basicas do servidor.");
					_print.println("unblock <ip>        - remove <ip> da banlist.");
					_print.println("shutdown			- desliga servidor.");
					_print.println("restart				- reinicia o servidor.");
					_print.println("RedirectLogger		- Telnet vai lhe dar algumas informacoes sobre o servidor em tempo real.");
					_print.println("quit                - fecha sessao telnet.");
					_print.println("");
				}
				else if (_usrCommand.equals("status"))
				{
					// TODO enhance the output
					_print.println("Servdor registrado: " + GameServerTable.getInstance().getRegisteredGameServers().size());
				}
				else if (_usrCommand.startsWith("unblock"))
				{
					try
					{
						_usrCommand = _usrCommand.substring(8);
						if (LoginController.getInstance().removeBanForAddress(_usrCommand))
						{
							_log.warning("IP removed via TELNET by host: " + _cSocket.getInetAddress().getHostAddress());
							_print.println("O IP " + _usrCommand + " foi removido da lista de protecao!");
						}
						else
						{
							_print.println("IP nao foi encontrado na lista de protecao...");
						}
					}
					catch (StringIndexOutOfBoundsException e)
					{
						_print.println("Digite o IP para Desbloquear!");
					}
				}
				else if (_usrCommand.startsWith("shutdown"))
				{
					L2LoginServer.getInstance().shutdown(false);
					_print.println("Bye Bye!");
					_print.flush();
					_cSocket.close();
				}
				else if (_usrCommand.startsWith("restart"))
				{
					L2LoginServer.getInstance().shutdown(true);
					_print.println("Bye Bye!");
					_print.flush();
					_cSocket.close();
				}
				else if (_usrCommand.equals("RedirectLogger"))
				{
					_redirectLogger = true;
				}
				else if (_usrCommand.equals("quit"))
				{ /* Do Nothing :p - Just here to save us from the "Command Not Understood" Text */
				}
				else if (_usrCommand.isEmpty())
				{ /* Do Nothing Again - Same reason as the quit part */
				}
				else
				{
					_print.println("Comando invalido");
				}
				_print.print("");
				_print.flush();
			}
			if (!_cSocket.isClosed())
			{
				_print.println("Bye Bye!");
				_print.flush();
				_cSocket.close();
			}
			telnetOutput(1, "Connection from " + _cSocket.getInetAddress().getHostAddress() + " was closed by client.");
		}
		catch (IOException e)
		{
			_log.warning(getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
	
	public void printToTelnet(String msg)
	{
		synchronized (_print)
		{
			_print.println(msg);
			_print.flush();
		}
	}
	
	/**
	 * @return Returns the redirectLogger.
	 */
	public boolean isRedirectLogger()
	{
		return _redirectLogger;
	}
}