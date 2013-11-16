/*
 * Copyright (C) 2004-2013 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package king.server.tools.dbinstaller.util.mysql;

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
