package pk.elfo.tools.dbinstaller.util.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Formatter;

import javax.swing.JOptionPane;

/**
 * PkElfo
 */
 
public class MySqlConnect
{
	Connection con = null;
	
	public MySqlConnect(String host, String port, String user, String password, String db, boolean console)
	{
		try (Formatter form = new Formatter())
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			final String formattedText = form.format("jdbc:mysql://%1$s:%2$s", host, port).toString();
			con = DriverManager.getConnection(formattedText, user, password);
			
			try (Statement s = con.createStatement())
			{
				s.execute("CREATE DATABASE IF NOT EXISTS `" + db + "`");
				s.execute("USE `" + db + "`");
			}
		}
		catch (SQLException e)
		{
			if (console)
			{
				e.printStackTrace();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "MySQL erro: " + e.getMessage(), "erro na conexao", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (InstantiationException e)
		{
			if (console)
			{
				e.printStackTrace();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Excecao instanciacao: " + e.getMessage(), "erro na conexao", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (IllegalAccessException e)
		{
			if (console)
			{
				e.printStackTrace();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Acesso ilegal: " + e.getMessage(), "erro na conexao", JOptionPane.ERROR_MESSAGE);
			}
		}
		catch (ClassNotFoundException e)
		{
			if (console)
			{
				e.printStackTrace();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Nao foi possivel encontrar MySQL Connector: " + e.getMessage(), "erro na conexao", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public Connection getConnection()
	{
		return con;
	}
	
	public Statement getStatement()
	{
		try
		{
			return con.createStatement();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			System.out.println("Statement Null");
			return null;
		}
	}
}