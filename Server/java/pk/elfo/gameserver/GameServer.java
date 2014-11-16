package pk.elfo.gameserver;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.mmocore.network.SelectorConfig;
import org.mmocore.network.SelectorThread;

import pk.elfo.Config;
import pk.elfo.L2DatabaseFactory;
import pk.elfo.Server;
import pk.elfo.UPnPService;
import pk.elfo.gameserver.cache.CrestCache;
import pk.elfo.gameserver.cache.HtmCache;
import pk.elfo.gameserver.custom.AutoVoteRewardManager;
import pk.elfo.gameserver.datatables.AdminTable;
import pk.elfo.gameserver.datatables.ArmorSetsData;
import pk.elfo.gameserver.datatables.AugmentationData;
import pk.elfo.gameserver.datatables.ChampionData;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.gameserver.datatables.CharSummonTable;
import pk.elfo.gameserver.datatables.CharTemplateTable;
import pk.elfo.gameserver.datatables.ClanTable;
import pk.elfo.gameserver.datatables.ClassListData;
import pk.elfo.gameserver.datatables.DoorTable;
import pk.elfo.gameserver.datatables.EnchantGroupsData;
import pk.elfo.gameserver.datatables.EnchantHPBonusData;
import pk.elfo.gameserver.datatables.EnchantItemData;
import pk.elfo.gameserver.datatables.EnchantOptionsData;
import pk.elfo.gameserver.datatables.EventDroplist;
import pk.elfo.gameserver.datatables.ExperienceTable;
import pk.elfo.gameserver.datatables.FakePcsTable;
import pk.elfo.gameserver.datatables.FishData;
import pk.elfo.gameserver.datatables.FishingMonstersData;
import pk.elfo.gameserver.datatables.FishingRodsData;
import pk.elfo.gameserver.datatables.HennaData;
import pk.elfo.gameserver.datatables.HerbDropTable;
import pk.elfo.gameserver.datatables.HitConditionBonus;
import pk.elfo.gameserver.datatables.InitialEquipmentData;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.datatables.ManorData;
import pk.elfo.gameserver.datatables.MerchantPriceConfigTable;
import pk.elfo.gameserver.datatables.MultiSell;
import pk.elfo.gameserver.datatables.MultilangMsgData;
import pk.elfo.gameserver.datatables.NpcBufferTable;
import pk.elfo.gameserver.datatables.NpcPersonalAIData;
import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.NpcWalkerRoutesData;
import pk.elfo.gameserver.datatables.OfflineTradersTable;
import pk.elfo.gameserver.datatables.PetDataTable;
import pk.elfo.gameserver.datatables.PremiumTable;
import pk.elfo.gameserver.datatables.ProductItemTable;
import pk.elfo.gameserver.datatables.RecipeData;
import pk.elfo.gameserver.datatables.SiegeScheduleData;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.datatables.SkillTreesData;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.datatables.StaticObjects;
import pk.elfo.gameserver.datatables.SummonItemsData;
import pk.elfo.gameserver.datatables.SummonSkillsTable;
import pk.elfo.gameserver.datatables.TeleportLocationTable;
import pk.elfo.gameserver.datatables.UITable;
import pk.elfo.gameserver.events.EventsInterface;
import pk.elfo.gameserver.events.Main;
import pk.elfo.gameserver.fence.FenceBuilderManager;
import pk.elfo.gameserver.fence.MovieMakerManager;
import pk.elfo.gameserver.geoeditorcon.GeoEditorListener;
import pk.elfo.gameserver.handler.EffectHandler;
import pk.elfo.gameserver.idfactory.IdFactory;
import pk.elfo.gameserver.instancemanager.AirShipManager;
import pk.elfo.gameserver.instancemanager.AntiFeedManager;
import pk.elfo.gameserver.instancemanager.AuctionManager;
import pk.elfo.gameserver.instancemanager.BoatManager;
import pk.elfo.gameserver.instancemanager.BonusExpManager;
import pk.elfo.gameserver.instancemanager.CHSiegeManager;
import pk.elfo.gameserver.instancemanager.CastleManager;
import pk.elfo.gameserver.instancemanager.CastleManorManager;
import pk.elfo.gameserver.instancemanager.ClanHallManager;
import pk.elfo.gameserver.instancemanager.CoupleManager;
import pk.elfo.gameserver.instancemanager.CursedWeaponsManager;
import pk.elfo.gameserver.instancemanager.DayNightSpawnManager;
import pk.elfo.gameserver.instancemanager.DimensionalRiftManager;
import pk.elfo.gameserver.instancemanager.ExpirableServicesManager;
import pk.elfo.gameserver.instancemanager.FortManager;
import pk.elfo.gameserver.instancemanager.FortSiegeManager;
import pk.elfo.gameserver.instancemanager.FourSepulchersManager;
import pk.elfo.gameserver.instancemanager.GlobalVariablesManager;
import pk.elfo.gameserver.instancemanager.GraciaSeedsManager;
import pk.elfo.gameserver.instancemanager.GrandBossManager;
import pk.elfo.gameserver.instancemanager.HellboundManager;
import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.instancemanager.ItemAuctionManager;
import pk.elfo.gameserver.instancemanager.ItemsOnGroundManager;
import pk.elfo.gameserver.instancemanager.MailManager;
import pk.elfo.gameserver.instancemanager.MapRegionManager;
import pk.elfo.gameserver.instancemanager.MercTicketManager;
import pk.elfo.gameserver.instancemanager.PcCafePointsManager;
import pk.elfo.gameserver.instancemanager.PetitionManager;
import pk.elfo.gameserver.instancemanager.PunishmentManager;
import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.instancemanager.RaidBossPointsManager;
import pk.elfo.gameserver.instancemanager.RaidBossSpawnManager;
import pk.elfo.gameserver.instancemanager.SiegeManager;
import pk.elfo.gameserver.instancemanager.SoIManager;
import pk.elfo.gameserver.instancemanager.TerritoryWarManager;
import pk.elfo.gameserver.instancemanager.TransformationManager;
import pk.elfo.gameserver.instancemanager.WalkingManager;
import pk.elfo.gameserver.instancemanager.ZoneManager;
import pk.elfo.gameserver.instancemanager.leaderboards.ArenaLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.CraftLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.FishermanLeaderboard;
import pk.elfo.gameserver.instancemanager.leaderboards.TvTLeaderboard;
import pk.elfo.gameserver.masteriopack.rankpvpsystem.RPSConfig;
import pk.elfo.gameserver.model.AutoSpawnHandler;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.PartyMatchRoomList;
import pk.elfo.gameserver.model.PartyMatchWaitingList;
import pk.elfo.gameserver.model.entity.Hero;
import pk.elfo.gameserver.model.entity.Hitman;
import pk.elfo.gameserver.model.entity.TownWarManager;
import pk.elfo.gameserver.model.entity.TvTManager;
import pk.elfo.gameserver.model.entity.TvTRoundManager;
import pk.elfo.gameserver.model.olympiad.Olympiad;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.L2GamePacketHandler;
import pk.elfo.gameserver.network.communityserver.CommunityServerThread;
import pk.elfo.gameserver.pathfinding.PathFinding;
import pk.elfo.gameserver.script.faenor.FaenorScriptEngine;
import pk.elfo.gameserver.scripting.L2ScriptEngineManager;
import pk.elfo.gameserver.taskmanager.AutoAnnounceTaskManager;
import pk.elfo.gameserver.taskmanager.KnownListUpdateTaskManager;
import pk.elfo.gameserver.taskmanager.TaskManager;
import pk.elfo.status.Status;
import pk.elfo.util.DeadLockDetector;
import pk.elfo.util.IPv4Filter;

public class GameServer
{
	private static final Logger _log = Logger.getLogger(GameServer.class.getName());
	
	private final SelectorThread<L2GameClient> _selectorThread;
	private final L2GamePacketHandler _gamePacketHandler;
	private final DeadLockDetector _deadDetectThread;
	private final IdFactory _idFactory;
	public static GameServer gameServer;
	private final LoginServerThread _loginThread;
	private static Status _statusServer;
	public static final Calendar dateTimeServerStarted = Calendar.getInstance();
	
	public long getUsedMemoryMB()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576; // ;
	}
	
	public SelectorThread<L2GameClient> getSelectorThread()
	{
		return _selectorThread;
	}
	
	public L2GamePacketHandler getL2GamePacketHandler()
	{
		return _gamePacketHandler;
	}
	
	public DeadLockDetector getDeadLockDetectorThread()
	{
		return _deadDetectThread;
	}
	
	public GameServer() throws Exception
	{
		long serverLoadStart = System.currentTimeMillis();
		
		gameServer = this;
		_log.finest(getClass().getSimpleName() + ": Memoria usada:" + getUsedMemoryMB() + "MB");
		_log.info(getClass().getSimpleName() + ": L2PkElfo Hight Five Project");
		
		if (Config.SERVER_VERSION != null)
		{
			_log.info(getClass().getSimpleName() + ": PkElfo Versao do CORE:    " + Config.SERVER_VERSION);
		}
		if (Config.DATAPACK_VERSION != null)
		{
			_log.info(getClass().getSimpleName() + ": PkElfo Versao do DP:  " + Config.DATAPACK_VERSION);
		}
		
		_idFactory = IdFactory.getInstance();
		
		if (!_idFactory.isInitialized())
		{
			_log.severe(getClass().getSimpleName() + ": Could not read object IDs from DB. Please Check Your Data.");
			throw new Exception("Could not initialize the ID factory");
		}
		
		ThreadPoolManager.getInstance();
		
		new File(Config.DATAPACK_ROOT, "data/crests").mkdirs();
		new File("log/game").mkdirs();
		
		// load script engines
		printSection("Mecanismos");
		L2ScriptEngineManager.getInstance();
		
		if (Config.IS_TELNET_ENABLED)
		{
			_statusServer = new Status(Server.serverMode);
			_statusServer.start();
		}
		else
		{
			_log.info(GameServer.class.getSimpleName() + ": Telnet desativado.");
		}
		
		printSection("Mundo");
		
		// start game time control early
		GameTimeController.getInstance();
		InstanceManager.getInstance();
		L2World.getInstance();
		MapRegionManager.getInstance();
		Announcements.getInstance();
		GlobalVariablesManager.getInstance();
		
		printSection("Skills");
		EffectHandler.getInstance().executeScript();
		EnchantGroupsData.getInstance();
		SkillTreesData.getInstance();
		SkillTable.getInstance();
		SummonSkillsTable.getInstance();
		
		printSection("Items");
		ItemTable.getInstance();
		EnchantItemData.getInstance();
		EnchantOptionsData.getInstance();
		SummonItemsData.getInstance();
		EnchantHPBonusData.getInstance();
		MerchantPriceConfigTable.getInstance().loadInstances();
		TradeController.getInstance();
		MultiSell.getInstance();
		ProductItemTable.getInstance();
		RecipeData.getInstance();
		ArmorSetsData.getInstance();
		FishData.getInstance();
		FishingMonstersData.getInstance();
		FishingRodsData.getInstance();
		HennaData.getInstance();
		
		printSection("Personagens");
		ClassListData.getInstance();
		InitialEquipmentData.getInstance();
		ExperienceTable.getInstance();
		HitConditionBonus.getInstance();
		CharTemplateTable.getInstance();
		CharNameTable.getInstance();
		AdminTable.getInstance();
		GmListTable.getInstance();
		RaidBossPointsManager.getInstance();
		PetDataTable.getInstance();
		CharSummonTable.getInstance().init();
		
		PremiumTable.getInstance();
		ExpirableServicesManager.getInstance();
		
		printSection("Clans");
		ClanTable.getInstance();
		CHSiegeManager.getInstance();
		ClanHallManager.getInstance();
		AuctionManager.getInstance();
		
		printSection("Geodata");
		GeoData.getInstance();
		if (Config.GEODATA == 2)
		{
			PathFinding.getInstance();
		}
		
		printSection("NPCs");
		HerbDropTable.getInstance();
		NpcTable.getInstance();
		if (Config.L2JMOD_CHAMPION_ENABLE)
		{
			ChampionData.getInstance();
		}
		NpcWalkerRoutesData.getInstance();
		WalkingManager.getInstance();
		NpcPersonalAIData.getInstance();
		StaticObjects.getInstance();
		ZoneManager.getInstance();
		DoorTable.getInstance();
		if (Config.FENCE_MOVIE_BUILDER)
		{
			MovieMakerManager.getInstance();
			FenceBuilderManager.getInstance();
		}
		ItemAuctionManager.getInstance();
		CastleManager.getInstance().loadInstances();
		NpcBufferTable.getInstance();
		FakePcsTable.getInstance();
		SpawnTable.getInstance();
		FourSepulchersManager.getInstance().init();
		DimensionalRiftManager.getInstance();
		HellboundManager.getInstance();
		FortManager.getInstance().loadInstances();
		
		if (Config.ENABLE_BONUS_MANAGER)
		{
			BonusExpManager.getInstance();
		}
		
		RaidBossSpawnManager.getInstance();
		DayNightSpawnManager.getInstance().trim().notifyChangeMode();
		GrandBossManager.getInstance().initZones();
		EventDroplist.getInstance();
		
		printSection("Siege");
		SiegeScheduleData.getInstance();
		SiegeManager.getInstance().getSieges();
		FortSiegeManager.getInstance();
		TerritoryWarManager.getInstance();
		CastleManorManager.getInstance();
		MercTicketManager.getInstance();
		PcCafePointsManager.getInstance();
		ManorData.getInstance();
		
		printSection("Olimpiadas");
		Olympiad.getInstance();
		Hero.getInstance();
	
		SevenSigns.getInstance();
		
		// Call to load caches
		printSection("Cache");
		HtmCache.getInstance();
		CrestCache.getInstance();
		TeleportLocationTable.getInstance();
		UITable.getInstance();
		PartyMatchWaitingList.getInstance();
		PartyMatchRoomList.getInstance();
		PetitionManager.getInstance();
		AugmentationData.getInstance();
		CursedWeaponsManager.getInstance();
		RPSConfig.load();
		
		printSection("Scripts");
		QuestManager.getInstance();
		MultilangMsgData.getInstance();
		TransformationManager.getInstance();
		BoatManager.getInstance();
		AirShipManager.getInstance();
		Hitman.start();
		GraciaSeedsManager.getInstance();
		SoIManager.getInstance();
		CastleManager.getInstance().activateInstances();
		FortManager.getInstance().activateInstances();
		MerchantPriceConfigTable.getInstance().updateReferences();
		
		if (Config.VOTE_SYSTEM_ENABLE == true)
		{
			AutoVoteRewardManager.getInstance();
		}
		
		try
		{
			_log.info(getClass().getSimpleName() + ": Carregando Scripts");
			File scripts = new File(Config.DATAPACK_ROOT, "data/scripts.cfg");
			if (!Config.ALT_DEV_NO_HANDLERS || !Config.ALT_DEV_NO_QUESTS)
			{
				L2ScriptEngineManager.getInstance().executeScriptList(scripts);
			}
		}
		catch (IOException ioe)
		{
			_log.severe(getClass().getSimpleName() + ": falha ao ler scripts.cfg, nenhum script vai ser carregado");
		}
		
		QuestManager.getInstance().report();
		TransformationManager.getInstance().report();
		
		if (Config.SAVE_DROPPED_ITEM)
		{
			ItemsOnGroundManager.getInstance();
		}
		
		if ((Config.AUTODESTROY_ITEM_AFTER > 0) || (Config.HERB_AUTO_DESTROY_TIME > 0))
		{
			ItemsAutoDestroy.getInstance();
		}
		
		MonsterRace.getInstance();
		SevenSigns.getInstance().spawnSevenSignsNPC();
		SevenSignsFestival.getInstance();
		AutoSpawnHandler.getInstance();
		
		FaenorScriptEngine.getInstance();
		// Init of a cursed weapon manager
		
		_log.info("AutoSpawnHandler: " + AutoSpawnHandler.getInstance().size() + " handlers no total.");
		
		if (Config.L2JMOD_ALLOW_WEDDING)
		{
			CoupleManager.getInstance();
		}
		
		if (Config.RANK_ARENA_ENABLED)
		{
			ArenaLeaderboard.getInstance();
		}
		
		if (Config.RANK_FISHERMAN_ENABLED)
		{
			FishermanLeaderboard.getInstance();
		}
		
		if (Config.RANK_CRAFT_ENABLED)
		{
			CraftLeaderboard.getInstance();
		}
		
		if (Config.RANK_TVT_ENABLED)
		{
			TvTLeaderboard.getInstance();
		}
		
		TaskManager.getInstance();
		AntiFeedManager.getInstance().registerEvent(AntiFeedManager.GAME_ID);
		
		if (Config.ALLOW_MAIL)
		{
			MailManager.getInstance();
		}
		
		if (Config.ACCEPT_GEOEDITOR_CONN)
		{
			GeoEditorListener.getInstance();
		}
		
		PunishmentManager.getInstance();
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		_log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());
		
		KnownListUpdateTaskManager.getInstance();
		
		printSection("Eventos do Server");
		Main.main();
		EventsInterface.start();
		TvTManager.getInstance();
		TownWarManager.getInstance();
		TvTRoundManager.getInstance();
		// L2 PkElfo Team
		PkElfo.PkElfoInfo();
		
		if ((Config.OFFLINE_TRADE_ENABLE || Config.OFFLINE_CRAFT_ENABLE) && Config.RESTORE_OFFLINERS)
		{
			OfflineTradersTable.getInstance().restoreOfflineTraders();
		}
		
		if (Config.DEADLOCK_DETECTOR)
		{
			_deadDetectThread = new DeadLockDetector();
			_deadDetectThread.setDaemon(true);
			_deadDetectThread.start();
		}
		else
		{
			_deadDetectThread = null;
		}
		System.gc();
		
		if (Config.AUTO_RESTART_ENABLE)
		{
			GameServerRestart.getInstance().StartCalculationOfNextRestartTime();
		}
		else
		{
			_log.info("[Auto Restart]: O Sistema esta desativado.");
		}
		
		// maxMemory is the upper limit the jvm can use, totalMemory the size of
		// the current allocation pool, freeMemory the unused memory in the
		// allocation pool
		long freeMem = ((Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory()) + Runtime.getRuntime().freeMemory()) / 1048576;
		long totalMem = Runtime.getRuntime().maxMemory() / 1048576;
		_log.info(getClass().getSimpleName() + ": Iniciado, memoria livre " + freeMem + " Mb de " + totalMem + " Mb");
		Toolkit.getDefaultToolkit().beep();
		
		_loginThread = LoginServerThread.getInstance();
		_loginThread.start();
		
		CommunityServerThread.initialize();
		
		final SelectorConfig sc = new SelectorConfig();
		sc.MAX_READ_PER_PASS = Config.MMO_MAX_READ_PER_PASS;
		sc.MAX_SEND_PER_PASS = Config.MMO_MAX_SEND_PER_PASS;
		sc.SLEEP_TIME = Config.MMO_SELECTOR_SLEEP_TIME;
		sc.HELPER_BUFFER_COUNT = Config.MMO_HELPER_BUFFER_COUNT;
		sc.TCP_NODELAY = Config.MMO_TCP_NODELAY;
		
		_gamePacketHandler = new L2GamePacketHandler();
		_selectorThread = new SelectorThread<>(sc, _gamePacketHandler, _gamePacketHandler, _gamePacketHandler, new IPv4Filter());
		
		InetAddress bindAddress = null;
		if (!Config.GAMESERVER_HOSTNAME.equals("*"))
		{
			try
			{
				bindAddress = InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
			}
			catch (UnknownHostException e1)
			{
				_log.log(Level.SEVERE, getClass().getSimpleName() + ": AVISO: O endereco bind do GameServer e invalido, usando todos os IPs disponiveis. Razao: " + e1.getMessage(), e1);
			}
		}
		
		try
		{
			_selectorThread.openServerSocket(bindAddress, Config.PORT_GAME);
			_selectorThread.start();
			_log.log(Level.INFO, getClass().getSimpleName() + ": is now listening on: " + Config.GAMESERVER_HOSTNAME + ":" + Config.PORT_GAME);
		}
		catch (IOException e)
		{
			_log.log(Level.SEVERE, getClass().getSimpleName() + ": FATAL: Falha ao abrir o socket do servidor. Razao: " + e.getMessage(), e);
			System.exit(1);
		}
		_log.log(Level.INFO, getClass().getSimpleName() + ": Numero maximo de jogadores online: " + Config.MAXIMUM_ONLINE_USERS);
		_log.log(Level.INFO, getClass().getSimpleName() + ": Server lido em " + ((System.currentTimeMillis() - serverLoadStart) / 1000) + " segundos.");
		printSection("UPnP");
		UPnPService.getInstance();
		
		AutoAnnounceTaskManager.getInstance();
		pk.elfo.gameserver.datatables.AIOItemTable.getInstance().loadAioItemData();
	}
	
	public static void main(String[] args) throws Exception
	{
		Server.serverMode = Server.MODE_GAMESERVER;
		// Local Constants
		final String LOG_FOLDER = "log"; // Name of folder for log file
		final String LOG_NAME = "./log.cfg"; // Name of log file
		
		/*** Main ***/
		// Create log folder
		File logFolder = new File(Config.DATAPACK_ROOT, LOG_FOLDER);
		logFolder.mkdir();
		
		// Create input stream for log file -- or store file data into memory
		try (InputStream is = new FileInputStream(new File(LOG_NAME)))
		{
			LogManager.getLogManager().readConfiguration(is);
		}
		
		// Initialize config
		Config.load();
		pk.elfo.AIOItem_Config.load();
				
		printSection("Database");
		L2DatabaseFactory.getInstance();
		gameServer = new GameServer();
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 78)
		{
			s = "-" + s;
		}
		_log.info(s);
	}
}