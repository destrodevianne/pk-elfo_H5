package handlers.voicedcommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import king.server.gameserver.cache.HtmCache;
import king.server.L2DatabaseFactory;
import king.server.gameserver.handler.IVoicedCommandHandler;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */
public class Repair implements IVoicedCommandHandler
{
	static final Logger _log = Logger.getLogger(Repair.class.getName());
	
	private static final String[]	 _voicedCommands	=
		{ 
		"repair", 
		"startrepair"
		};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{		
		if (activeChar==null)
			return false;
		
		String repairChar=null;
		
		try		
		{
			if(target != null)
				if(target.length() > 1)
				  {
				   String[] cmdParams = target.split(" ");
				   repairChar=cmdParams[0];
				  }
		}
		catch (Exception e)
		{
			repairChar = null;
		}		
				
		// Send activeChar HTML page
		if (command.startsWith("repair"))
		{
			String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/mods/repair/repair.htm");
			NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
			npcHtmlMessage.setHtml(htmContent);		
			npcHtmlMessage.replace("%acc_chars%", getCharList(activeChar));
			activeChar.sendPacket(npcHtmlMessage);	
			return true;
		}
		// Command for enter repairFunction from html
		if (command.startsWith("startrepair") && (repairChar != null))
		{
			//_log.warning("Repair Attempt: Character " + repairChar);
				if (checkAcc(activeChar,repairChar))
				{
					if (checkChar(activeChar,repairChar))
					{
						String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/mods/repair/repair-self.htm");
						NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
						npcHtmlMessage.setHtml(htmContent);
						activeChar.sendPacket(npcHtmlMessage);
						return false;
					}
					else if (checkJail(activeChar,repairChar))
					{
						String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/mods/repair/repair-jail.htm");
						NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
						npcHtmlMessage.setHtml(htmContent);
						activeChar.sendPacket(npcHtmlMessage);	
						return false;
					}
					else
					{
						repairBadCharacter(repairChar);
						String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/mods/repair/repair-done.htm");
						NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
						npcHtmlMessage.setHtml(htmContent);
						activeChar.sendPacket(npcHtmlMessage);
						return true;
					}
				}
				String htmContent = HtmCache.getInstance().getHtm(activeChar.getHtmlPrefix(), "data/html/mods/repair/repair-error.htm");
				NpcHtmlMessage npcHtmlMessage = new NpcHtmlMessage(5);
				npcHtmlMessage.setHtml(htmContent);
				activeChar.sendPacket(npcHtmlMessage);
				return false;
		}
		//_log.warning("Repair Attempt: Failed. ");
		return false;
	}
	
	private String getCharList(L2PcInstance activeChar)
	{
		String result="";
		String repCharAcc=activeChar.getAccountName();
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT char_name FROM characters WHERE account_name=?");
			statement.setString(1, repCharAcc);
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				if (activeChar.getName().compareTo(rset.getString(1)) != 0)
					result += rset.getString(1)+";";
			}
			//_log.warning("Repair Attempt: Output Result for searching characters on account:"+result);
			rset.close();
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return result;
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		return result;	
	}
	
	private boolean checkAcc(L2PcInstance activeChar,String repairChar)
	{
		boolean result=false;
		String repCharAcc="";
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT account_name FROM characters WHERE char_name=?");
			statement.setString(1, repairChar);
			ResultSet rset = statement.executeQuery();
			if (rset.next())
			{
				repCharAcc = rset.getString(1);
			}
			rset.close();
			statement.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return result;
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (activeChar.getAccountName().compareTo(repCharAcc)==0)
			result=true;
		return result;
	}

	private boolean checkJail(L2PcInstance activeChar,String repairChar)
	{
		boolean result=false;
		int repCharJail = 0;
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT punish_level FROM characters WHERE char_name=?");
			statement.setString(1, repairChar);
			ResultSet rset = statement.executeQuery();
			if (rset.next())
			{
				repCharJail = rset.getInt(1);
			}
			rset.close();
			statement.close();

		}
		catch (SQLException e)
		{
			e.printStackTrace();
			return result;
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		if (repCharJail > 1) // 0 norm, 1 chat ban, 2 jail, 3....
			result=true;
		return result;
	}

	private boolean checkChar(L2PcInstance activeChar,String repairChar)
	{
		boolean result=false;
		if (activeChar.getName().compareTo(repairChar)==0)
			result=true;
		return result;
	}

	private void repairBadCharacter(String charName)
	{
		Connection con = null;
		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();

			PreparedStatement statement;
			statement = con.prepareStatement("SELECT charId FROM characters WHERE char_name=?");
			statement.setString(1, charName);
			ResultSet rset = statement.executeQuery();

			int objId = 0;
			if (rset.next())
			{
				objId = rset.getInt(1);
			}
			rset.close();
			statement.close();
			if (objId == 0)
			{
				con.close();
				return;
			}
			statement = con.prepareStatement("UPDATE characters SET x=17867, y=170259, z=-3503 WHERE charId=?");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
			statement = con.prepareStatement("DELETE FROM character_shortcuts WHERE charId=?");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
			statement = con.prepareStatement("UPDATE items SET loc=\"WAREHOUSE\" WHERE owner_id=? AND loc=\"PAPERDOLL\"");
			statement.setInt(1, objId);
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("GameServer: could not repair character:" + e);
		}
		finally
		{
			try
			{
				if (con != null)
					con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}