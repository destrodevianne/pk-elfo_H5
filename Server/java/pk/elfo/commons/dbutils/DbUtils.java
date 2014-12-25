package pk.elfo.commons.dbutils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A collection of JDBC helper methods.  This class is thread safe.
 */
public class DbUtils
{
	/**
	 * Close a <code>Connection</code>, avoid closing if null.
	 *
	 * @param con Connection to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close(Connection con) throws SQLException
	{
		if(con != null)
			con.close();
	}

	/**
	 * Close a <code>ResultSet</code>, avoid closing if null.
	 *
	 * @param rset ResultSet to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close(ResultSet rset) throws SQLException
	{
		if(rset != null)
			rset.close();
	}

	/**
	 * Close a <code>Statement</code>, avoid closing if null.
	 *
	 * @param stmt Statement to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close(Statement stmt) throws SQLException
	{
		if(stmt != null)
			stmt.close();
	}

	/**
	 * Close a <code>Statement</code> and <code>ResultSet</code>, avoid closing if null.
	 *
	 * @param stmt Statement to close.
	 * @param rset ResultSet to close.
	 * @throws SQLException if a database access error occurs
	 */
	public static void close(Statement stmt, ResultSet rset) throws SQLException
	{
		close(stmt);
		close(rset);
	}

	/**
	 * Close a <code>Connection</code>, avoid closing if null and hide
	 * any SQLExceptions that occur.
	 *
	 * @param con Connection to close.
	 */
	public static void closeQuietly(Connection con)
	{
		try
		{
			close(con);
		}
		catch(SQLException e)
		{
			// quiet
		}
	}

	/**
	 * Close a <code>Connection</code> and <code>Statement</code>.
	 * Avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param con Connection to close.
	 * @param stmt Statement to close.
	 */
	public static void closeQuietly(Connection con, Statement stmt)
	{
		try
		{
			closeQuietly(stmt);
		}
		finally
		{
			closeQuietly(con);
		}
	}

	/**
	 * Close a <code>Statement</code> and <code>ResultSet</code>.
	 * Avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param stmt Statement to close.
	 * @param rset ResultSet to close.
	 */
	public static void closeQuietly(Statement stmt, ResultSet rset)
	{
		try
		{
			closeQuietly(stmt);
		}
		finally
		{
			closeQuietly(rset);
		}
	}

	/**
	 * Close a <code>Connection</code>, <code>Statement</code> and
	 * <code>ResultSet</code>.  Avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param con Connection to close.
	 * @param stmt Statement to close.
	 * @param rset ResultSet to close.
	 */
	public static void closeQuietly(Connection con, Statement stmt, ResultSet rset)
	{

		try
		{
			closeQuietly(rset);
		}
		finally
		{
			try
			{
				closeQuietly(stmt);
			}
			finally
			{
				closeQuietly(con);
			}
		}
	}

	/**
	 * Close a <code>ResultSet</code>, avoid closing if null and hide any
	 * SQLExceptions that occur.
	 *
	 * @param rset ResultSet to close.
	 */
	public static void closeQuietly(ResultSet rset)
	{
		try
		{
			close(rset);
		}
		catch(SQLException e)
		{
			// quiet
		}
	}

	/**
	 * Close a <code>Statement</code>, avoid closing if null and hide
	 * any SQLExceptions that occur.
	 *
	 * @param stmt Statement to close.
	 */
	public static void closeQuietly(Statement stmt)
	{
		try
		{
			close(stmt);
		}
		catch(SQLException e)
		{
			// quiet
		}
	}
}