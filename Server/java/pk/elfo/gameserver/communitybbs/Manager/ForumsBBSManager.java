package pk.elfo.gameserver.communitybbs.Manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.communitybbs.BB.Forum;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import javolution.util.FastList;

public class ForumsBBSManager extends BaseBBSManager
{
	private static Logger _log = Logger.getLogger(ForumsBBSManager.class.getName());
	private final List<Forum> _table;
	private int _lastid = 1;
	
	protected ForumsBBSManager()
	{
		_table = new FastList<>();
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT forum_id FROM forums WHERE forum_type = 0"))
		{
			while (rs.next())
			{
				int forumId = rs.getInt("forum_id");
				Forum f = new Forum(forumId, null);
				addForum(f);
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Data error on Forum (root): " + e.getMessage(), e);
		}
	}
	
	public void initRoot()
	{
		for (Forum f : _table)
		{
			f.vload();
		}
		_log.info("Loaded " + _table.size() + " forums. Last forum id used: " + _lastid);
	}
	
	public void addForum(Forum ff)
	{
		if (ff == null)
		{
			return;
		}
		
		_table.add(ff);
		
		if (ff.getID() > _lastid)
		{
			_lastid = ff.getID();
		}
	}
	
	@Override
	public void parsecmd(String command, L2PcInstance activeChar)
	{
	}
	
	public Forum getForumByName(String name)
	{
		for (Forum f : _table)
		{
			if (f.getName().equals(name))
			{
				return f;
			}
		}
		return null;
	}
	
	public Forum createNewForum(String name, Forum parent, int type, int perm, int oid)
	{
		Forum forum = new Forum(name, parent, type, perm, oid);
		forum.insertIntoDb();
		return forum;
	}
	
	public int getANewID()
	{
		return ++_lastid;
	}
	
	public Forum getForumByID(int idf)
	{
		for (Forum f : _table)
		{
			if (f.getID() == idf)
			{
				return f;
			}
		}
		return null;
	}
	
	@Override
	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar)
	{
		
	}
	
	public static ForumsBBSManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final ForumsBBSManager _instance = new ForumsBBSManager();
	}
}