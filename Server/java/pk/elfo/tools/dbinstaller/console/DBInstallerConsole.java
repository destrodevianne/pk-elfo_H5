package pk.elfo.tools.dbinstaller.console;

import java.sql.Connection;
import java.util.Scanner;
import java.util.prefs.Preferences;

import pk.elfo.tools.dbinstaller.DBOutputInterface;
import pk.elfo.tools.dbinstaller.RunTasks;
import pk.elfo.tools.dbinstaller.util.mysql.MySqlConnect;
import pk.elfo.util.CloseShieldedInputStream;

/**
 * PkElfo
 */
 
public class DBInstallerConsole implements DBOutputInterface
{
	Connection _con;
	
	public DBInstallerConsole(String db, String dir, String cleanUp)
	{
		System.out.println("Bem Vindo ao instalador L2PkElfo DataBase");
		Preferences prop = Preferences.userRoot();
		RunTasks rt = null;
		try (Scanner scn = new Scanner(new CloseShieldedInputStream(System.in)))
		{
			while (_con == null)
			{
				System.out.printf("%s (%s): ", "Host", prop.get("dbHost_" + db, "localhost"));
				String dbHost = scn.nextLine();
				System.out.printf("%s (%s): ", "Port", prop.get("dbPort_" + db, "3306"));
				String dbPort = scn.nextLine();
				System.out.printf("%s (%s): ", "Username", prop.get("dbUser_" + db, "root"));
				String dbUser = scn.nextLine();
				System.out.printf("%s (%s): ", "Password", "");
				String dbPass = scn.nextLine();
				System.out.printf("%s (%s): ", "Database", prop.get("dbDbse_" + db, db));
				String dbDbse = scn.nextLine();
				
				dbHost = dbHost.isEmpty() ? prop.get("dbHost_" + db, "localhost") : dbHost;
				dbPort = dbPort.isEmpty() ? prop.get("dbPort_" + db, "3306") : dbPort;
				dbUser = dbUser.isEmpty() ? prop.get("dbUser_" + db, "root") : dbUser;
				dbDbse = dbDbse.isEmpty() ? prop.get("dbDbse_" + db, db) : dbDbse;
				
				MySqlConnect connector = new MySqlConnect(dbHost, dbPort, dbUser, dbPass, dbDbse, true);
				
				_con = connector.getConnection();
			}
			
			System.out.print("(C)lean install, (U)pdate or (E)xit? ");
			String resp = scn.next();
			if (resp.equalsIgnoreCase("c"))
			{
				System.out.print("Voce realmente quer deletar a sua db (Y/N)?");
				if (scn.next().equalsIgnoreCase("y"))
				{
					rt = new RunTasks(this, db, dir, cleanUp, true);
				}
			}
			else if (resp.equalsIgnoreCase("u"))
			{
				rt = new RunTasks(this, db, dir, cleanUp, false);
			}
		}
		
		if (rt != null)
		{
			rt.run();
		}
		else
		{
			System.exit(0);
		}
	}
	
	@Override
	public void appendToProgressArea(String text)
	{
		System.out.println(text);
	}
	
	@Override
	public Connection getConnection()
	{
		return _con;
	}
	
	@Override
	public void setProgressIndeterminate(boolean value)
	{
	}
	
	@Override
	public void setProgressMaximum(int maxValue)
	{
	}
	
	@Override
	public void setProgressValue(int value)
	{
	}
	
	@Override
	public void setFrameVisible(boolean value)
	{
	}
	
	@Override
	public int requestConfirm(String title, String message, int type)
	{
		System.out.print(message);
		String res = "";
		try (Scanner scn = new Scanner(new CloseShieldedInputStream(System.in)))
		{
			res = scn.next();
		}
		return res.equalsIgnoreCase("y") ? 0 : 1;
	}
	
	@Override
	public void showMessage(String title, String message, int type)
	{
		System.out.println(message);
	}
}