package pk.elfo.util.lib;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.L2DatabaseFactory;

/**
 * PkElfo
 */

public class SqlUtils
{
	private static Logger _log = Logger.getLogger(SqlUtils.class.getName());
	
	public static SqlUtils getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public static Integer getIntValue(String resultField, String tableName, String whereClause)
	{
		String query = "";
		Integer res = null;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			query = L2DatabaseFactory.getInstance().prepQuerySelect(new String[]
			{
				resultField
			}, tableName, whereClause, true);
			
			try (PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					res = rs.getInt(1);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "erro na consulta '" + query + "':", e);
		}
		return res;
	}
	
	public static Integer[] getIntArray(String resultField, String tableName, String whereClause)
	{
		String query = "";
		Integer[] res = null;
		try
		{
			query = L2DatabaseFactory.getInstance().prepQuerySelect(new String[]
			{
				resultField
			}, tableName, whereClause, false);
			
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery())
			{
				int rows = 0;
				while (rs.next())
				{
					rows++;
				}
				
				if (rows == 0)
				{
					return new Integer[0];
				}
				
				res = new Integer[rows - 1];
				
				rs.first();
				
				int row = 0;
				while (rs.next())
				{
					res[row] = rs.getInt(1);
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "mSGI: Erro na consulta '" + query + "':", e);
		}
		return res;
	}
	
	public static Integer[][] get2DIntArray(String[] resultFields, String usedTables, String whereClause)
	{
		long start = System.currentTimeMillis();
		String query = "";
		Integer res[][] = null;
		try
		{
			query = L2DatabaseFactory.getInstance().prepQuerySelect(resultFields, usedTables, whereClause, false);
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(query);
				ResultSet rs = ps.executeQuery())
			{
				int rows = 0;
				while (rs.next())
				{
					rows++;
				}
				
				res = new Integer[rows - 1][resultFields.length];
				
				rs.first();
				
				int row = 0;
				while (rs.next())
				{
					for (int i = 0; i < resultFields.length; i++)
					{
						res[row][i] = rs.getInt(i + 1);
					}
					row++;
				}
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Erro na consulta '" + query + "':", e);
		}
		_log.fine("Obter todas as linhas da consulta '" + query + "' em " + (System.currentTimeMillis() - start) + "ms");
		return res;
	}
	
	private static class SingletonHolder
	{
		protected static final SqlUtils _instance = new SqlUtils();
	}
}
