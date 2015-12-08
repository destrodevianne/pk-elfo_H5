package pk.elfo.gameserver.communitybbs.Manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.L2DatabaseFactory;

/**
 * PkElfo
 */
public class ItemTableBBSManager
{
	private static Logger _log = Logger.getLogger(ItemTableBBSManager.class.getName());
	
	private static String _icon = null;
	
	public static void main(String[] args)
	{
	}
	
	public static String LoadIconData(int itemid)
	{
		Connection con = null;
		try
		{
			try
			{
				con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT icon FROM item_icons WHERE item_id=?");
				statement.setInt(1, itemid);
				ResultSet recorddata = statement.executeQuery();
				
				while (recorddata.next())
				{
					_icon = recorddata.getString("icon");
				}
				recorddata.close();
				statement.close();
			}
			catch (Exception e)
			{
				_log.log(Level.SEVERE, "error while creating record table " + e);
			}
		}
		finally
		{
			try
			{
				if (con != null)
				{
					con.close();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return _icon;
	}
	
	public static String getIcon(int itemid)
	{
		return LoadIconData(itemid);
	}
}