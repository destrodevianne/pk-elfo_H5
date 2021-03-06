package pk.elfo.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.logging.Logger;

import javolution.util.FastList;
import javolution.util.FastMap;

import pk.elfo.AIOItem_Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.handler.AIOItemHandler;
import pk.elfo.gameserver.handler.IAIOItemHandler;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.TvTEvent;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * PkElfo
 */

public final class AIOItemTable
{
	protected static final Logger _log = Logger.getLogger(AIOItemTable.class.getName());
	
	private static class SingletonHolder
	{
		static final AIOItemTable _instance = new AIOItemTable();
	}
	
	private static class BufferPageHolder
	{
		static NpcHtmlMessage mainPage = null;
		static FastMap<String, NpcHtmlMessage> categoryPages = new FastMap<>();
	}
	
	private static class TeleportPageHolder
	{
		static NpcHtmlMessage mainPage = null;
		static FastMap<Integer, NpcHtmlMessage> categoryPages = new FastMap<>();
		static FastMap<Integer, FastMap<Integer, Integer[]>> teleInfo = new FastMap<>();
	}
	
	private class TopInfo
	{
		String name = "";
		int amount = 0;
		
		public TopInfo()
		{
		}
	}
	
	// Holds each category and his one-use buffs
	private static FastMap<String, CategoryBuffHolder> _buffs = new FastMap<>();
	// Holds all skills
	static FastMap<Integer, L2Skill> _allBuffs = new FastMap<>();
	// Holds pvp rank
	private static String _pvpList;
	private static int _minPvp;
	private final FastMap<String, Integer> _allPvps = new FastMap<>();
	// Holds pk rank
	private static String _pkList;
	private static int _minPk;
	private final FastMap<String, Integer> _allPks = new FastMap<>();
	
	public AIOItemTable()
	{
	}
	
	public static AIOItemTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	public class SpawnPointInfo
	{
		public String _name;
		public int _x;
		public int _y;
		public int _z;
	}
	
	public class CategoryBuffHolder
	{
		FastMap<Integer, L2Skill> _categoryBuffs;
		String _categoryName;
		
		@SuppressWarnings("unused")
		CategoryBuffHolder(String name)
		{
			if (name != null)
			{
				_categoryName = name;
				_categoryBuffs = new FastMap<Integer, L2Skill>();
			}
		}
		void loadMyData()
		{
				try (Connection con = L2DatabaseFactory.getInstance().getConnection())
				{
					PreparedStatement statement = con.prepareStatement("SELECT buff_id, buff_lvl FROM aio_buffs WHERE category = ?");
					statement.setString(1, _categoryName);
					ResultSet rset = statement.executeQuery();
				
					StringBuilder catSb = new StringBuilder();
					catSb.append("<html><body>");
					catSb.append("<html><body><center>");
					catSb.append("<br><font color=LEVEL>Escolha o que vocE quer!</font><br>");
					catSb.append("<table width = 240 height 32>");
				
					int b = 2;
				
					while (rset.next())
					{
						int id = rset.getInt("buff_id");
						int lvl = rset.getInt("buff_lvl");
					
						L2Skill buff = SkillTable.getInstance().getInfo(id, lvl);
						_categoryBuffs.put(id, buff);
						_allBuffs.put(id, buff);
					
						if ((b % 2) == 0)
						{
							catSb.append("<tr>");
							catSb.append("<td><a action=\"bypass -h Aioitem_buffer_buff " + _categoryName + " " + id + "\">" + buff.getName() + "</a></td>");
						}
						else
						{
							catSb.append("<td><a action=\"bypass -h Aioitem_buffer_buff " + _categoryName + " " + id + "\">" + buff.getName() + "</a></td>");
							catSb.append("</tr>");
						}
						b++;
					}
					catSb.append("</table><br><a action=\"bypass -h Aioitem_buffer_main\"><font color=LEVEL>Inicio</font></a>");
					catSb.append("</center></body></html>");
				
					NpcHtmlMessage msg = new NpcHtmlMessage(5);
					msg.setHtml(catSb.toString());
				
					BufferPageHolder.categoryPages.put(_categoryName, msg);
					rset.close();
					statement.close();
					con.close();
				}
				catch (Exception e)
				{
					_log.severe("Nao foi possivel carregar a tabela de buffs para AIOItem\n" + e.getMessage());
				}
			}
		
		public FastMap<Integer, L2Skill> getCategoryBuffs()
		{
			return _categoryBuffs;
		}
	}
	
	public void loadAioItemData()
	{
		if (!AIOItem_Config.AIOITEM_ENABLEME)
		{
			_log.config("AIOItem: estou desativado");
			return;
		}
		
		_log.config("Lendo dados de AIOItem...");
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			try
			{
				PreparedStatement statement = con.prepareStatement("SELECT category_id, category FROM aio_teleports_categories");
				ResultSet rset = statement.executeQuery();
				
				StringBuilder sbMain = new StringBuilder();
				sbMain.append("<html><body><br><center>Escolha qualquer categoria de teleporte:<br>");
				while (rset.next())
				{
					final int id = rset.getInt("category_id");
					final String name = rset.getString("category");
					
					sbMain.append("<a action=\"bypass -h Aioitem_teleport_categorypage " + id + "\">" + name + "</a><br1>");
					
					PreparedStatement categoryStatement = con.prepareStatement("SELECT * FROM aio_teleports WHERE category = ?");
					categoryStatement.setString(1, name);
					ResultSet catSet = categoryStatement.executeQuery();
					
					StringBuilder sbCategory = new StringBuilder();
					FastMap<Integer, Integer[]> spawnInfo = new FastMap<>();
					sbCategory.append("<html><body><br><center>Escolha entre os meus teleportes!:<br>");
					
					while (catSet.next())
					{
						final int teleId = catSet.getInt("id");
						final String teleName = catSet.getString("tpname");
						final int x = catSet.getInt("x");
						final int y = catSet.getInt("y");
						final int z = catSet.getInt("z");
						
						final Integer[] array =
						{
							x,
							y,
							z
						};
						spawnInfo.put(teleId, array);
						sbCategory.append("<a action=\"bypass -h Aioitem_teleport_goto " + id + " " + teleId + "\">" + teleName + "</a><br1>");
					}
					sbCategory.append("</center></body></html>");
					NpcHtmlMessage msg = new NpcHtmlMessage(5);
					msg.setHtml(sbCategory.toString());
					TeleportPageHolder.categoryPages.put(id, msg);
					TeleportPageHolder.teleInfo.put(id, spawnInfo);
					catSet.close();
					categoryStatement.close();
				}
				sbMain.append("</center></body></html>");
				NpcHtmlMessage main = new NpcHtmlMessage(5);
				main.setHtml(sbMain.toString());
				TeleportPageHolder.mainPage = main;
				rset.close();
				statement.close();
				_log.config("AIOItemTable: Lido " + TeleportPageHolder.categoryPages.size() + " categorias de teleporte!");
			}
			catch (Exception e)
			{
				_log.warning("AIOItemTable: Nao foi possivel carregar o AIO Item teleportes: " + e.getMessage());
				e.printStackTrace();
			}
			
			try
			{
				PreparedStatement buffStatement = con.prepareStatement("SELECT category FROM aio_buffs");
				ResultSet buffSet = buffStatement.executeQuery();
				StringBuilder mainSb = new StringBuilder();
				mainSb.append("<html><body><br><center>Escolha uma categoria de Buffs:</center><br>");
				FastList<String> alredyGathered = new FastList<>();
				while (buffSet.next())
				{
					final String name = buffSet.getString("category");
					if (alredyGathered.contains(name))
					{
						continue;
					}
					alredyGathered.add(name);
					mainSb.append("<a action=\"bypass -h Aioitem_buffer_category " + name + "\">" + name + "</a><br1>");
					
					CategoryBuffHolder holder = new CategoryBuffHolder(name);
					holder.loadMyData();
					_buffs.put(name, holder);
				}
				mainSb.append("</body></html>");
				NpcHtmlMessage msg = new NpcHtmlMessage(5);
				msg.setHtml(mainSb.toString());
				BufferPageHolder.mainPage = msg;
				buffSet.close();
				buffStatement.close();
				_log.config("Lido " + _buffs.size() + " da categoria de buffs para o AIOItem");
			}
			catch (Exception e)
			{
				_log.warning("AIOItem: Nao foi possivel carregar o AIO Item buffs: " + e.getMessage());
				e.printStackTrace();
			}
			// PvP Rank
			loadPvps();
			// Pk Rank
			loadPks();
			con.close();
		}
		catch (Exception e)
		{
			_log.severe("Nao foi possivel carregar os dados de AIOItem\n" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void loadPvps()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT char_name,pvpkills FROM characters WHERE pvpkills>0 and accesslevel=0");
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				final String name = rset.getString("char_name");
				final int pvp = rset.getInt("pvpkills");
				
				_allPvps.put(name, pvp);
			}
			rset.close();
			statement.close();
			con.close();
		}
		catch (Exception e)
		{
			_log.warning("AIOItemTable: Nao foi possivel recolher informacoes necessarias do banco de dados para o PvP Top:");
			e.printStackTrace();
		}
		buildPvpRank(getTop25Pvps());
	}
	
	private FastMap<String, Integer> getTop25Pvps()
	{
		Map<String, Integer> pvps = new FastMap<>();
		pvps.putAll(_allPvps);
		FastMap<String, Integer> toReturn = new FastMap<>();
		for (int i = 0; i < 25; i++)
		{
			final TopInfo info = getBest(pvps);
			final int amount = info.amount;
			if (amount == 0)
			{
				continue;
			}
			toReturn.put(info.name, amount);
			pvps.remove(info.name);
			_minPvp = amount;
		}
		return toReturn;
	}
	
	private TopInfo getBest(Map<String, Integer> pvps)
	{
		TopInfo info = new TopInfo();
		for (Map.Entry<String, Integer> entry : pvps.entrySet())
		{
			if (entry.getValue() > info.amount)
			{
				info.amount = entry.getValue();
				info.name = entry.getKey();
			}
		}
		return info;
	}
	
	private void buildPvpRank(final FastMap<String, Integer> data)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("");
		final String colored = "<table width=270 bgcolor=\"66CCFF\"><tr>";
		final String nonColored = "<table width=270><tr>";
		int counter = 1;
		
		for (Map.Entry<String, Integer> entry : getTop25Pvps().entrySet())
		{
			if ((counter % 2) == 0)
			{
				sb.append(colored);
			}
			else
			{
				sb.append(nonColored);
			}
			
			sb.append("<td width=90>" + counter + "</td>");
			sb.append("<td width=90>" + entry.getKey() + "</td>");
			sb.append("<td width=90>" + entry.getValue() + "</td>");
			sb.append("</tr></table>");
			++counter;
		}
		_pvpList = sb.toString();
	}
	
	private void loadPks()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection())
		{
			PreparedStatement statement = con.prepareStatement("SELECT char_name,pkkills FROM characters WHERE pkkills>0 and accesslevel=0");
			ResultSet rset = statement.executeQuery();
			while (rset.next())
			{
				final String name = rset.getString("char_name");
				final int pk = rset.getInt("pkkills");
				
				_allPks.put(name, pk);
			}
			rset.close();
			statement.close();
		}
		catch (Exception e)
		{
			_log.warning("AIOItemTable: Nao foi possivel recolher informacoes necessarias do banco de dados para o Pk Top:");
			e.printStackTrace();
		}
		buildPkRank(getTop25Pks());
	}
	
	private FastMap<String, Integer> getTop25Pks()
	{
		Map<String, Integer> pks = new FastMap<>();
		pks.putAll(_allPks);
		FastMap<String, Integer> toReturn = new FastMap<>();
		for (int i = 0; i < 25; i++)
		{
			final TopInfo info = getBest(pks);
			final int amount = info.amount;
			if (amount == 0)
			{
				continue;
			}
			toReturn.put(info.name, amount);
			pks.remove(info.name);
			_minPk = amount;
		}
		return toReturn;
	}
	
	private void buildPkRank(final FastMap<String, Integer> data)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("");
		final String colored = "<table width=270 bgcolor=\"FF9999\"><tr>";
		final String nonColored = "<table width=270><tr>";
		int counter = 1;
		
		for (Map.Entry<String, Integer> entry : getTop25Pks().entrySet())
		{
			if ((counter % 2) == 0)
			{
				sb.append(colored);
			}
			else
			{
				sb.append(nonColored);
			}
			
			sb.append("<td width=90>" + counter + "</td>");
			sb.append("<td width=90>" + entry.getKey() + "</td>");
			sb.append("<td width=90>" + entry.getValue() + "</td>");
			sb.append("</tr></table>");
			++counter;
		}
		_pkList = sb.toString();
	}
	
	public String getPvpRank()
	{
		return _pvpList;
	}
	
	public String getPkRank()
	{
		return _pkList;
	}
	
	public void onPvpIncrease(L2PcInstance pc, int pvps)
	{
		_allPvps.put(pc.getName(), pvps);
		if (pvps > _minPvp)
		{
			buildPvpRank(getTop25Pvps());
		}
	}
	
	public void onPkIncrease(L2PcInstance pc, int pks)
	{
		_allPks.put(pc.getName(), pks);
		if (pks > _minPk)
		{
			buildPkRank(getTop25Pks());
		}
	}
	
	public NpcHtmlMessage getTeleportMain()
	{
		return TeleportPageHolder.mainPage;
	}
	
	public NpcHtmlMessage getTeleportCategoryPage(int category)
	{
		return TeleportPageHolder.categoryPages.get(category);
	}
	
	public Integer[] getTelportCoordinates(int category, int teleId)
	{
		try
		{
			return TeleportPageHolder.teleInfo.get(category).get(teleId);
		}
		catch (Exception e)
		{
			_log.warning("AIOItemTable: Categoria de Teleporte [" + category + "] ou ponto de spawn [" + teleId + "] nao existe");
		}
		return null;
	}
	
	public NpcHtmlMessage getBufferMain()
	{
		return BufferPageHolder.mainPage;
	}
	
	public NpcHtmlMessage getBufferCategoryPage(String category)
	{
		return BufferPageHolder.categoryPages.get(category);
	}
	
	public FastMap<String, CategoryBuffHolder> getBuffs()
	{
		return _buffs;
	}
	
	/**
	 * Will return the holder of buffs of a given category
	 * @param cat [String]
	 * @return CategoryBuffHolder
	 */
	public CategoryBuffHolder getBuffCategory(String cat)
	{
		if (!_buffs.containsKey(cat))
		{
			return null;
		}
		return _buffs.get(cat);
	}
	
	/**
	 * Will return a skill which must be contained in the given category with the given id
	 * @param category
	 * @param id
	 * @return L2Skill
	 */
	public L2Skill getBuff(String category, int id)
	{
		if (getBuffCategory(category) != null)
		{
			L2Skill buff = null;
			if ((buff = getBuffCategory(category).getCategoryBuffs().get(id)) != null)
			{
				return buff;
			}
		}
		return null;
	}
	
	/**
	 * Return the skill linked to the given id
	 * @param id
	 * @return L2Skill
	 */
	public L2Skill getBuff(int id)
	{
		return _allBuffs.get(id);
	}
	
	/**
	 * Check the general requirements for the player to be able to send a aio item bypass and use it
	 * @param player [L2PcInstance]
	 * @return boolean
	 */
	public boolean checkPlayerConditions(L2PcInstance player)
	{
		if (player.getPvpFlag() > 0)
		{
			player.sendMessage("Nao e possivel usar o AIOItem enquanto estiver flagged!");
			return false;
		}
		if ((player.getKarma() > 0) || player.isCursedWeaponEquipped())
		{
			player.sendMessage("Nao e possivel usar o AIOItem enquanto estiver chaotic!");
			return false;
		}
		if (player.isInOlympiadMode() || TvTEvent.isPlayerParticipant(player.getObjectId()))
		{
			player.sendMessage("Nao e possivel usar estando em events!");
			return false;
		}
		if (player.isEnchanting())
		{
			player.sendMessage("Nao e possivel usar enquanto enchanta!");
			return false;
		}
		if (player.isInJail())
		{
			player.sendMessage("Nao e possivel usar estando na Jaula!");
			return false;
		}
		return true;
	}
	
	public void handleBypass(L2PcInstance activeChar, String command)
	{
		if (!AIOItem_Config.AIOITEM_ENABLEME)
		{
			return;
		}
		
		if (!checkPlayerConditions(activeChar))
		{
			return;
		}
		
		activeChar.setTarget(activeChar);
		String[] subCmd = command.split("_");
		IAIOItemHandler handler = AIOItemHandler.getInstance().getAIOHandler(subCmd[1]);
		
		if (handler != null)
		{
			handler.onBypassUse(activeChar, subCmd[2]);
		}
		else
		{
			_log.warning("AIOItemTable: Nao e possivel encontrar o comando AIOItem nos handlers: " + subCmd[1]);
		}
	}
}