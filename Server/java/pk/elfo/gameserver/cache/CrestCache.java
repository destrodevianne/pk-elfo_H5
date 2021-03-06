package pk.elfo.gameserver.cache;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.idfactory.IdFactory;
import pk.elfo.gameserver.model.L2Clan;
import pk.elfo.util.file.filter.BMPFilter;
import pk.elfo.util.file.filter.OldPledgeFilter;
import javolution.util.FastMap;

/**
 * @author PkElfo
 */
public class CrestCache
{
	private static Logger _log = Logger.getLogger(CrestCache.class.getName());
	private final FastMRUCache<Integer, byte[]> _cachePledge = new FastMRUCache<>();
	private final FastMRUCache<Integer, byte[]> _cachePledgeLarge = new FastMRUCache<>();
	private final FastMRUCache<Integer, byte[]> _cacheAlly = new FastMRUCache<>();
	private int _loadedFiles;
	private long _bytesBuffLen;
	
	public static CrestCache getInstance()
	{
		return SingletonHolder._instance;
	}
	
	protected CrestCache()
	{
		convertOldPedgeFiles();
		reload();
	}
	
	public void reload()
	{
		final FileFilter filter = new BMPFilter();
		final File dir = new File(Config.DATAPACK_ROOT, "data/crests/");
		final File[] files = dir.listFiles(filter);
		byte[] content;
		synchronized (this)
		{
			_loadedFiles = 0;
			_bytesBuffLen = 0;
			
			_cachePledge.clear();
			_cachePledgeLarge.clear();
			_cacheAlly.clear();
		}
		
		FastMap<Integer, byte[]> _mapPledge = _cachePledge.getContentMap();
		FastMap<Integer, byte[]> _mapPledgeLarge = _cachePledgeLarge.getContentMap();
		FastMap<Integer, byte[]> _mapAlly = _cacheAlly.getContentMap();
		
		final L2Clan[] clans = ClanTable.getInstance().getClans();
		for (File file : files)
		{
			synchronized (this)
			{
				try (RandomAccessFile rfa = new RandomAccessFile(file, "r"))
				{
					content = new byte[(int) rfa.length()];
					rfa.readFully(content);
					
					boolean erase = true;
					int crestId = 0;
					
					if (file.getName().startsWith("Crest_Large_"))
					{
						crestId = Integer.parseInt(file.getName().substring(12, file.getName().length() - 4));
						if (Config.CLEAR_CREST_CACHE)
						{
							for (final L2Clan clan : clans)
							{
								if (clan.getCrestLargeId() == crestId)
								{
									erase = false;
									break;
								}
							}
							if (erase)
							{
								file.delete();
								continue;
							}
						}
						_mapPledgeLarge.put(crestId, content);
					}
					else if (file.getName().startsWith("Crest_"))
					{
						crestId = Integer.parseInt(file.getName().substring(6, file.getName().length() - 4));
						if (Config.CLEAR_CREST_CACHE)
						{
							for (final L2Clan clan : clans)
							{
								if (clan.getCrestId() == crestId)
								{
									erase = false;
									break;
								}
							}
							if (erase)
							{
								file.delete();
								continue;
							}
						}
						_mapPledge.put(crestId, content);
					}
					else if (file.getName().startsWith("AllyCrest_"))
					{
						crestId = Integer.parseInt(file.getName().substring(10, file.getName().length() - 4));
						if (Config.CLEAR_CREST_CACHE)
						{
							for (final L2Clan clan : clans)
							{
								if (clan.getAllyCrestId() == crestId)
								{
									erase = false;
									break;
								}
							}
							if (erase)
							{
								file.delete();
								continue;
							}
						}
						_mapAlly.put(crestId, content);
					}
					_loadedFiles++;
					_bytesBuffLen += content.length;
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, "Problem with crest bmp file " + e.getMessage(), e);
				}
			}
		}
		
		for (L2Clan clan : clans)
		{
			if (clan.getCrestId() != 0)
			{
				if (getPledgeCrest(clan.getCrestId()) == null)
				{
					_log.log(Level.INFO, "Removing non-existent crest for clan " + clan.getName() + " [" + clan.getClanId() + "], crestId:" + clan.getCrestId());
					clan.setCrestId(0);
					clan.changeClanCrest(0);
				}
			}
			if (clan.getCrestLargeId() != 0)
			{
				if (getPledgeCrestLarge(clan.getCrestLargeId()) == null)
				{
					_log.log(Level.INFO, "Removing non-existent large crest for clan " + clan.getName() + " [" + clan.getClanId() + "], crestLargeId:" + clan.getCrestLargeId());
					clan.setCrestLargeId(0);
					clan.changeLargeCrest(0);
				}
			}
			if (clan.getAllyCrestId() != 0)
			{
				if (getAllyCrest(clan.getAllyCrestId()) == null)
				{
					_log.log(Level.INFO, "Removing non-existent ally crest for clan " + clan.getName() + " [" + clan.getClanId() + "], allyCrestId:" + clan.getAllyCrestId());
					clan.setAllyCrestId(0);
					clan.changeAllyCrest(0, true);
				}
			}
		}
		_log.info("Cache[Crest]: " + String.format("%.3f", getMemoryUsage()) + "MB on " + getLoadedFiles() + " files loaded. (Forget Time: " + (_cachePledge.getForgetTime() / 1000) + "s , Capacity: " + _cachePledge.capacity() + ")");
	}
	
	public void convertOldPedgeFiles()
	{
		final File dir = new File(Config.DATAPACK_ROOT, "data/crests/");
		final File[] files = dir.listFiles(new OldPledgeFilter());
		for (File file : files)
		{
			int clanId = Integer.parseInt(file.getName().substring(7, file.getName().length() - 4));
			_log.info("Found old crest file \"" + file.getName() + "\" for clanId " + clanId);
			final L2Clan clan = ClanTable.getInstance().getClan(clanId);
			if (clan != null)
			{
				removeOldPledgeCrest(clan.getCrestId());
				int newId = IdFactory.getInstance().getNextId();
				file.renameTo(new File(Config.DATAPACK_ROOT, "data/crests/Crest_" + newId + ".bmp"));
				_log.info("Renamed Clan crest to new format: Crest_" + newId + ".bmp");
				
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement("UPDATE clan_data SET crest_id = ? WHERE clan_id = ?"))
				{
					ps.setInt(1, newId);
					ps.setInt(2, clan.getClanId());
					ps.executeUpdate();
				}
				catch (SQLException e)
				{
					_log.log(Level.WARNING, "Could not update the crest id:" + e.getMessage(), e);
				}
				clan.setCrestId(newId);
			}
			else
			{
				_log.info("Clan Id: " + clanId + " does not exist in table.. deleting.");
				file.delete();
			}
		}
	}
	
	public float getMemoryUsage()
	{
		return ((float) _bytesBuffLen / 1048576);
	}
	
	public int getLoadedFiles()
	{
		return _loadedFiles;
	}
	
	public byte[] getPledgeCrest(int id)
	{
		return _cachePledge.get(id);
	}
	
	public byte[] getPledgeCrestLarge(int id)
	{
		return _cachePledgeLarge.get(id);
	}
	
	public byte[] getAllyCrest(int id)
	{
		return _cacheAlly.get(id);
	}
	
	public void removePledgeCrest(int id)
	{
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/Crest_" + id + ".bmp");
		_cachePledge.remove(id);
		try
		{
			crestFile.delete();
		}
		catch (Exception e)
		{
		}
	}
	
	public void removePledgeCrestLarge(int id)
	{
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/Crest_Large_" + id + ".bmp");
		_cachePledgeLarge.remove(id);
		try
		{
			crestFile.delete();
		}
		catch (Exception e)
		{
		}
	}
	
	public void removeOldPledgeCrest(int id)
	{
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/Pledge_" + id + ".bmp");
		try
		{
			crestFile.delete();
		}
		catch (Exception e)
		{
		}
	}
	
	public void removeAllyCrest(int id)
	{
		File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/AllyCrest_" + id + ".bmp");
		_cacheAlly.remove(id);
		try
		{
			crestFile.delete();
		}
		catch (Exception e)
		{
		}
	}
	
	public boolean savePledgeCrest(int newId, byte[] data)
	{
		final File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/Crest_" + newId + ".bmp");
		try (FileOutputStream out = new FileOutputStream(crestFile))
		{
			out.write(data);
			_cachePledge.getContentMap().put(newId, data);
			return true;
		}
		catch (IOException e)
		{
			_log.log(Level.INFO, "Error saving pledge crest" + crestFile + ":", e);
			return false;
		}
	}
	
	public boolean savePledgeCrestLarge(int newId, byte[] data)
	{
		final File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/Crest_Large_" + newId + ".bmp");
		try (FileOutputStream out = new FileOutputStream(crestFile))
		{
			out.write(data);
			_cachePledgeLarge.getContentMap().put(newId, data);
			return true;
		}
		catch (IOException e)
		{
			_log.log(Level.INFO, "Error saving Large pledge crest" + crestFile + ":", e);
			return false;
		}
	}
	
	public boolean saveAllyCrest(int newId, byte[] data)
	{
		final File crestFile = new File(Config.DATAPACK_ROOT, "data/crests/AllyCrest_" + newId + ".bmp");
		try (FileOutputStream out = new FileOutputStream(crestFile))
		{
			out.write(data);
			_cacheAlly.getContentMap().put(newId, data);
			return true;
		}
		catch (IOException e)
		{
			_log.log(Level.INFO, "Error saving ally crest" + crestFile + ":", e);
			return false;
		}
	}
	
	private static class SingletonHolder
	{
		protected static final CrestCache _instance = new CrestCache();
	}
}