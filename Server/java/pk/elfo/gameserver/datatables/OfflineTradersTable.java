package pk.elfo.gameserver.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.gameserver.LoginServerThread;
import pk.elfo.gameserver.model.L2ManufactureItem;
import pk.elfo.gameserver.model.L2ManufactureList;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.TradeItem;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.AbnormalEffect;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.L2GameClient.GameClientState;

public class OfflineTradersTable
{
	private static Logger _log = Logger.getLogger(OfflineTradersTable.class.getName());
	
	// SQL DEFINITIONS
	private static final String SAVE_OFFLINE_STATUS = "INSERT INTO character_offline_trade (`charId`,`time`,`type`,`title`) VALUES (?,?,?,?)";
	private static final String SAVE_ITEMS = "INSERT INTO character_offline_trade_items (`charId`,`item`,`count`,`price`) VALUES (?,?,?,?)";
	private static final String CLEAR_OFFLINE_TABLE = "DELETE FROM character_offline_trade";
	private static final String CLEAR_OFFLINE_TABLE_ITEMS = "DELETE FROM character_offline_trade_items";
	private static final String LOAD_OFFLINE_STATUS = "SELECT * FROM character_offline_trade";
	private static final String LOAD_OFFLINE_ITEMS = "SELECT * FROM character_offline_trade_items WHERE charId = ?";
	
	public void storeOffliners()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement stm1 = con.prepareStatement(CLEAR_OFFLINE_TABLE);
			PreparedStatement stm2 = con.prepareStatement(CLEAR_OFFLINE_TABLE_ITEMS);
			PreparedStatement stm3 = con.prepareStatement(SAVE_OFFLINE_STATUS);
			PreparedStatement stm_items = con.prepareStatement(SAVE_ITEMS))
		{
			stm1.execute();
			stm2.execute();
			con.setAutoCommit(false); // avoid halfway done
			
			// TextBuilder items = TextBuilder.newInstance();
			for (L2PcInstance pc : L2World.getInstance().getAllPlayersArray())
			{
				try
				{
					if ((pc.getPrivateStoreType() != L2PcInstance.STORE_PRIVATE_NONE) && ((pc.getClient() == null) || pc.getClient().isDetached()))
					{
						stm3.setInt(1, pc.getObjectId()); // Char Id
						stm3.setLong(2, pc.getOfflineStartTime());
						stm3.setInt(3, pc.getPrivateStoreType()); // store type
						String title = null;
						
						switch (pc.getPrivateStoreType())
						{
							case L2PcInstance.STORE_PRIVATE_BUY:
								if (!Config.OFFLINE_TRADE_ENABLE)
								{
									continue;
								}
								title = pc.getBuyList().getTitle();
								for (TradeItem i : pc.getBuyList().getItems())
								{
									stm_items.setInt(1, pc.getObjectId());
									stm_items.setInt(2, i.getItem().getItemId());
									stm_items.setLong(3, i.getCount());
									stm_items.setLong(4, i.getPrice());
									stm_items.executeUpdate();
									stm_items.clearParameters();
								}
								break;
							case L2PcInstance.STORE_PRIVATE_SELL:
							case L2PcInstance.STORE_PRIVATE_PACKAGE_SELL:
								if (!Config.OFFLINE_TRADE_ENABLE)
								{
									continue;
								}
								title = pc.getSellList().getTitle();
								for (TradeItem i : pc.getSellList().getItems())
								{
									stm_items.setInt(1, pc.getObjectId());
									stm_items.setInt(2, i.getObjectId());
									stm_items.setLong(3, i.getCount());
									stm_items.setLong(4, i.getPrice());
									stm_items.executeUpdate();
									stm_items.clearParameters();
								}
								break;
							case L2PcInstance.STORE_PRIVATE_MANUFACTURE:
								if (!Config.OFFLINE_CRAFT_ENABLE)
								{
									continue;
								}
								title = pc.getCreateList().getStoreName();
								for (L2ManufactureItem i : pc.getCreateList().getList())
								{
									stm_items.setInt(1, pc.getObjectId());
									stm_items.setInt(2, i.getRecipeId());
									stm_items.setLong(3, 0);
									stm_items.setLong(4, i.getCost());
									stm_items.executeUpdate();
									stm_items.clearParameters();
								}
						}
						stm3.setString(4, title);
						stm3.executeUpdate();
						stm3.clearParameters();
						con.commit(); // flush
					}
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while saving offline trader: " + pc.getObjectId() + " " + e, e);
				}
			}
			_log.info(getClass().getSimpleName() + ": Offline traders stored.");
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while saving offline traders: " + e, e);
		}
	}
	
	public void restoreOfflineTraders()
	{
		_log.info(getClass().getSimpleName() + ": Loading offline traders...");
		int nTraders = 0;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery(LOAD_OFFLINE_STATUS))
		{
			while (rs.next())
			{
				long time = rs.getLong("time");
				if (Config.OFFLINE_MAX_DAYS > 0)
				{
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(time);
					cal.add(Calendar.DAY_OF_YEAR, Config.OFFLINE_MAX_DAYS);
					if (cal.getTimeInMillis() <= System.currentTimeMillis())
					{
						continue;
					}
				}
				
				int type = rs.getInt("type");
				if (type == L2PcInstance.STORE_PRIVATE_NONE)
				{
					continue;
				}
				
				L2PcInstance player = null;
				
				try
				{
					L2GameClient client = new L2GameClient(null);
					client.setDetached(true);
					player = L2PcInstance.load(rs.getInt("charId"));
					client.setActiveChar(player);
					player.setOnlineStatus(true, false);
					client.setAccountName(player.getAccountNamePlayer());
					client.setState(GameClientState.IN_GAME);
					player.setClient(client);
					player.setOfflineStartTime(time);
					
					if (Config.OFFLINE_TRADE_EFFECT)
					{
						switch (Config.OFFLINE_EFFECT_ID)
						{
							case 1:
								player.startAbnormalEffect(AbnormalEffect.BLEEDING);
								break;
							case 2:
								player.startAbnormalEffect(AbnormalEffect.POISON);
								break;
							case 3:
								player.startAbnormalEffect(AbnormalEffect.ICE2);
								break;
							case 4:
								player.startAbnormalEffect(AbnormalEffect.MUTED);
								break;
							case 5:
								player.startAbnormalEffect(AbnormalEffect.SLEEP);
								break;
							case 6:
								player.startAbnormalEffect(AbnormalEffect.ROOT);
								break;
							case 7:
								player.startAbnormalEffect(AbnormalEffect.HOLD_2);
								break;
							case 8:
								player.startAbnormalEffect(AbnormalEffect.BIG_HEAD);
								break;
							case 9:
								player.startAbnormalEffect(AbnormalEffect.FLAME);
								break;
							case 10:
								player.startAbnormalEffect(AbnormalEffect.FIREROOT_STUN);
								break;
							case 11:
								player.startAbnormalEffect(AbnormalEffect.STEALTH);
								break;
							case 12:
								player.startAbnormalEffect(AbnormalEffect.IMPRISIONING_2);
								break;
						}
					}
					
					player.spawnMe(player.getX(), player.getY(), player.getZ());
					LoginServerThread.getInstance().addGameServerLogin(player.getAccountName(), client);
					try (PreparedStatement stm_items = con.prepareStatement(LOAD_OFFLINE_ITEMS))
					{
						stm_items.setInt(1, player.getObjectId());
						try (ResultSet items = stm_items.executeQuery())
						{
							switch (type)
							{
								case L2PcInstance.STORE_PRIVATE_BUY:
									while (items.next())
									{
										if (player.getBuyList().addItemByItemId(items.getInt(2), items.getLong(3), items.getLong(4)) == null)
										{
											throw new NullPointerException();
										}
									}
									player.getBuyList().setTitle(rs.getString("title"));
									break;
								case L2PcInstance.STORE_PRIVATE_SELL:
								case L2PcInstance.STORE_PRIVATE_PACKAGE_SELL:
									while (items.next())
									{
										if (player.getSellList().addItem(items.getInt(2), items.getLong(3), items.getLong(4)) == null)
										{
											throw new NullPointerException();
										}
									}
									player.getSellList().setTitle(rs.getString("title"));
									player.getSellList().setPackaged(type == L2PcInstance.STORE_PRIVATE_PACKAGE_SELL);
									break;
								case L2PcInstance.STORE_PRIVATE_MANUFACTURE:
									L2ManufactureList createList = new L2ManufactureList();
									while (items.next())
									{
										createList.add(new L2ManufactureItem(items.getInt(2), items.getLong(4)));
									}
									player.setCreateList(createList);
									player.getCreateList().setStoreName(rs.getString("title"));
									break;
							}
						}
					}
					player.sitDown();
					if (Config.OFFLINE_MODE_SET_INVULNERABLE)
					{
						player.setIsInvul(true);
					}
					if (Config.OFFLINE_SET_NAME_COLOR)
					{
						player.getAppearance().setNameColor(Config.OFFLINE_NAME_COLOR);
					}
					player.setPrivateStoreType(type);
					player.setOnlineStatus(true, true);
					player.restoreEffects();
					player.broadcastUserInfo();
					nTraders++;
				}
				catch (Exception e)
				{
					_log.log(Level.WARNING, getClass().getSimpleName() + ": Error loading trader: " + player, e);
					if (player != null)
					{
						player.deleteMe();
					}
				}
			}
			
			_log.info(getClass().getSimpleName() + ": Loaded: " + nTraders + " offline trader(s)");
			
			try (Statement stm1 = con.createStatement())
			{
				stm1.execute(CLEAR_OFFLINE_TABLE);
				stm1.execute(CLEAR_OFFLINE_TABLE_ITEMS);
			}
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Error while loading offline traders: ", e);
		}
	}
	
	/**
	 * Gets the single instance of OfflineTradersTable.
	 * @return single instance of OfflineTradersTable
	 */
	public static OfflineTradersTable getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final OfflineTradersTable _instance = new OfflineTradersTable();
	}
}