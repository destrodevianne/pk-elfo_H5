package pk.elfo.gameserver.custom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class AutoVoteRewardManager
{
	private static Logger _log = Logger.getLogger(AutoVoteRewardManager.class.getName());
	
	private static final int initialCheck = Config.VOTE_SYSTEM_START_TIME * 1000;
	private static final int delayForCheck = Config.VOTE_SYSTEM_CHECK_TIME * 1000;
	private int votesneed;
	
	private static List<String> _ips = new ArrayList<>();
	private static int lastVoteCount = 0;
	
	@SuppressWarnings("synthetic-access")
	private AutoVoteRewardManager()
	{
		_log.info("AutoVoteRewardManager: Vote reward system iniciado.");
		if (Config.VOTE_SYSTEM_DATABASE_SAVE)
		{
			load();
		}
		ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoReward(), initialCheck, delayForCheck);
	}
	
	private class AutoReward implements Runnable
	{
		@SuppressWarnings("synthetic-access")
		@Override
		public void run()
		{
			int votes = getVotes();
			_log.info("AutoVoteRewardManager: Current Votes: " + getVotes());
			_log.info("AutoVoteRewardManager: Votes needed: " + (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT));
			_log.info("AutoVoteRewardManager: Next Check in: " + (delayForCheck / 1000) + " sec.");
			Announcements.getInstance().announceVote("Vote for us in  all Site banners!");
			
			if (votes >= (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT))
			{
				Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().valueCollection();
				{
					for (L2PcInstance onlinePlayer : pls)
					{
						if (onlinePlayer.isOnline() && !onlinePlayer.getClient().isDetached() && !_ips.contains(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress()))
						{
							String[] parase = Config.VOTE_SYSTEM_ITEM_ID.split(",");
							String[] parase3 = Config.VOTE_SYSTEM_ITEM_COUNT.split(",");
							for (int o = 0; o < parase.length; o++)
							{
								int parase2 = Integer.parseInt(parase[o]);
								int parase4 = Integer.parseInt(parase3[o]);
								for (String element : parase)
								{
									onlinePlayer.addItem("vote_reward", parase2, parase4, onlinePlayer, true);
								}
							}
							_ips.add(onlinePlayer.getClient().getConnection().getInetAddress().getHostAddress());
						}
					}
				}
				_log.info("AutoVoteRewardManager: All players has been rewared!");
				Announcements.getInstance().announceVote("Thanks for vote, you has been rewarded!");
				setLastVoteCount(getLastVoteCount() + Config.VOTE_SYSTEM_COUNT);
			}
			
			if (getLastVoteCount() == 0)
			{
				setLastVoteCount(votes);
			}
			else if ((((getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votes) > Config.VOTE_SYSTEM_COUNT) || (votes > (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT)))
			{
				setLastVoteCount(votes);
			}
			votesneed = (getLastVoteCount() + Config.VOTE_SYSTEM_COUNT) - votes;
			if (votesneed == 0)
			{
				votesneed = Config.VOTE_SYSTEM_COUNT;
			}
			Announcements.getInstance().announceVote("Need " + votesneed + " votes more to reward all players.");
			_ips.clear();
		}
	}
	
	@SuppressWarnings("null")
	private int getVotes()
	{
		URL url = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		try
		{
			url = new URL(Config.VOTE_SYSTEM_PAGE);
			URLConnection con = url.openConnection();
			con.addRequestProperty("User-Agent", "Mozilla/4.76");
			isr = new InputStreamReader(con.getInputStream());
			in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				if (Config.VOTE_SYSTEM_HOPZONE == false)
				{
					// TopZone
					if (inputLine.contains("<tr><td><div align=\"center\"><b><font style=\"font-size:14px;color:#018BC1;\">"))
					{
						String i = inputLine.replace("<tr><td><div align=\"center\"><b><font style=\"font-size:14px;color:#018BC1;\">", "");
						i = i.replace("</font></b></div></td></tr>", "");
						i = i.trim();
						int o = Integer.parseInt(i);
						return Integer.valueOf(o);
					}
				}
				else
				{
					// for hopzone
					if (inputLine.contains("Anonymous User Votes"))
					{
						return Integer.valueOf(inputLine.split(">")[2].replace("</span", ""));
					}
				}
			}
		}
		catch (IOException e)
		{
			_log.warning("AutoVoteRewardHandler: " + e);
		}
		finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
			}
			try
			{
				isr.close();
			}
			catch (IOException e)
			{
			}
		}
		return 0;
	}
	
	private void setLastVoteCount(int voteCount)
	{
		lastVoteCount = voteCount;
	}
	
	private int getLastVoteCount()
	{
		return lastVoteCount;
	}
	
	private void load()
	{
		int votes = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT vote FROM votes LIMIT 1");
			ResultSet rset = statement.executeQuery();
			
			while (rset.next())
			{
				votes = rset.getInt("vote");
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "data error on vote: ", e);
		}
		setLastVoteCount(votes);
	}
	
	public void save()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("UPDATE votes SET vote = ? WHERE id=1");
			statement.setInt(1, getLastVoteCount());
			statement.execute();
			statement.close();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "data error on vote: ", e);
		}
	}
	
	public static AutoVoteRewardManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AutoVoteRewardManager _instance = new AutoVoteRewardManager();
	}
}