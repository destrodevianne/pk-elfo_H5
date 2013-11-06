package king.server.gameserver.datatables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import king.server.Config;
import king.server.gameserver.model.entity.ActionKey;

public class UITable
{
	private static final Logger _log = Logger.getLogger(UITable.class.getName());
	
	private final Map<Integer, List<ActionKey>> _storedKeys = new HashMap<>();
	private final Map<Integer, List<Integer>> _storedCategories = new HashMap<>();
	
	protected UITable()
	{
		parseCatData();
		parseKeyData();
		_log.info(getClass().getSimpleName() + ": " + _storedCategories.size() + " Categorias.");
		_log.info(getClass().getSimpleName() + ": " + _storedKeys.size() + " Chaves.");
	}
	
	private void parseCatData()
	{
		final File uiData = new File(Config.DATAPACK_ROOT, "data/uicats_en.csv");
		try (FileReader fr = new FileReader(uiData);
			BufferedReader br = new BufferedReader(fr);
			LineNumberReader lnr = new LineNumberReader(br))
		{
			String line = null;
			while ((line = lnr.readLine()) != null)
			{
				if (line.trim().isEmpty() || (line.charAt(0) == '#'))
				{
					continue;
				}
				
				StringTokenizer st = new StringTokenizer(line, ";");
				
				int cat = Integer.parseInt(st.nextToken());
				int cmd = Integer.parseInt(st.nextToken());
				
				insertCategory(cat, cmd);
			}
		}
		catch (FileNotFoundException e)
		{
			_log.warning(getClass().getSimpleName() + ": uicats_en.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while creating UI Default Categories table " + e.getMessage(), e);
		}
	}
	
	private void parseKeyData()
	{
		final File uiData = new File(Config.DATAPACK_ROOT, "data/uikeys_en.csv");
		try (FileReader fr = new FileReader(uiData);
			BufferedReader br = new BufferedReader(fr);
			LineNumberReader lnr = new LineNumberReader(br))
		{
			String line = null;
			while ((line = lnr.readLine()) != null)
			{
				if (line.trim().isEmpty() || (line.charAt(0) == '#'))
				{
					continue;
				}
				
				StringTokenizer st = new StringTokenizer(line, ";");
				
				int cat = Integer.parseInt(st.nextToken());
				int cmd = Integer.parseInt(st.nextToken());
				int key = Integer.parseInt(st.nextToken());
				int tk1 = Integer.parseInt(st.nextToken());
				int tk2 = Integer.parseInt(st.nextToken());
				int shw = Integer.parseInt(st.nextToken());
				
				insertKey(cat, cmd, key, tk1, tk2, shw);
			}
		}
		catch (FileNotFoundException e)
		{
			_log.warning(getClass().getSimpleName() + ": uikeys_en.csv is missing in data folder");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while creating UI Default Keys table " + e.getMessage(), e);
		}
	}
	
	private void insertCategory(int cat, int cmd)
	{
		if (_storedCategories.containsKey(cat))
		{
			_storedCategories.get(cat).add(cmd);
		}
		else
		{
			List<Integer> tmp = new ArrayList<>();
			tmp.add(cmd);
			_storedCategories.put(cat, tmp);
		}
	}
	
	private void insertKey(int cat, int cmdId, int key, int tgKey1, int tgKey2, int show)
	{
		ActionKey tmk = new ActionKey(cat, cmdId, key, tgKey1, tgKey2, show);
		if (_storedKeys.containsKey(cat))
		{
			_storedKeys.get(cat).add(tmk);
		}
		else
		{
			List<ActionKey> tmp = new ArrayList<>();
			tmp.add(tmk);
			_storedKeys.put(cat, tmp);
		}
	}
	
	public Map<Integer, List<Integer>> getCategories()
	{
		return _storedCategories;
	}
	
	public Map<Integer, List<ActionKey>> getKeys()
	{
		return _storedKeys;
	}
	
	public static UITable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final UITable _instance = new UITable();
	}
}