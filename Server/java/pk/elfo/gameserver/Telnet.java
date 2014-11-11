package pk.elfo.gameserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import pk.elfo.Config;
import pk.elfo.gameserver.instancemanager.PunishmentManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.punishment.PunishmentAffect;
import pk.elfo.gameserver.model.punishment.PunishmentTask;
import pk.elfo.gameserver.model.punishment.PunishmentType;

public class Telnet extends Thread
{
	private DatagramSocket socket;
	String pass = Config.TELNET_PASSWORD;
	int port = Config.TELNET_LOGIN;
	boolean connected = false;

	public Telnet()
	{
		try
		{
			this.socket = new DatagramSocket(port);
			System.out.println("Servidor Telnet iniciado.");
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try
			{
				socket.receive(packet);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			String mess = new String(packet.getData());
			if (mess.contains("pass"))
			{
				String[] password = mess.split(":");
				if (pass.equals(password[1].trim()))
				{
					sendData("passright".getBytes(), packet.getAddress(), packet.getPort());
					connected = true;
				}
				else
				{
					System.out.println("Senha errada.");
					sendData("passwrong".getBytes(), packet.getAddress(), packet.getPort());
				}
			}
			if (connected)
			{
				if (mess.trim().equals("shutdown"))
				{
					System.out.println("Comando de desligamento");
					System.out.println("Desligando em 60 seg");

					Shutdown.getInstance().shutDown(60, false);
				}
				if (mess.trim().equals("restart"))
				{
					System.out.println("Comando de reinicio");
					System.out.println("Reiniciando em 60 secs");

					Shutdown.getInstance().shutDown(60, true);
				}
				if (mess.contains("announce"))
				{
					String[] split = mess.split(":");

					System.out.println("Announced " + split[1]);
					Announcements.getInstance().announceToAll(split[1]);
				}
				else if (mess.contains("jail"))
				{
					String[] split = mess.split(":");
					L2PcInstance pl = L2World.getInstance().getPlayer(split[1]);
					PunishmentManager.getInstance().startPunishment(new PunishmentTask(pl.getObjectId(), PunishmentAffect.CHARACTER, PunishmentType.JAIL, 10000, "jailed", "Admin"));
				}
				if (mess.contains("ban"))
				{
					String[] split = mess.split(":");
					L2PcInstance pl = L2World.getInstance().getPlayer(split[1]);
					pl.setAccessLevel(-1);
				}
				if (mess.trim().equals("online"))
				{
					String online = null;
					for (L2PcInstance onlines : L2World.getInstance().getAllPlayersArray())
					{
						online = onlines.getName() + ",";
					}
				}
				if (mess.trim().equals("closing"))
				{
					connected = false;
					System.out.println("Telnet fechado");
				}
			}
			else
			{
				System.out.println("Alguem tentando enviar comandos sem estar conectado");
			}
		}
	}
	public void sendData(byte[] data, InetAddress ipAddress, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try
		{
			socket.send(packet);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println("enviado : " + new String(data));
	}
}