package pk.elfo.gameserver.network.clientpackets;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.CharNameTable;
import pk.elfo.gameserver.datatables.CharTemplateTable;
import pk.elfo.gameserver.datatables.SkillTable;
import pk.elfo.gameserver.datatables.SkillTreesData;
import pk.elfo.gameserver.instancemanager.QuestManager;
import pk.elfo.gameserver.model.L2ShortCut;
import pk.elfo.gameserver.model.L2SkillLearn;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.appearance.PcAppearance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.actor.stat.PcStat;
import pk.elfo.gameserver.model.actor.templates.L2PcTemplate;
import pk.elfo.gameserver.model.items.PcItemTemplate;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.network.L2GameClient;
import pk.elfo.gameserver.network.serverpackets.CharCreateFail;
import pk.elfo.gameserver.network.serverpackets.CharCreateOk;
import pk.elfo.gameserver.network.serverpackets.CharSelectionInfo;
import pk.elfo.gameserver.scripting.scriptengine.events.PlayerEvent;
import pk.elfo.gameserver.scripting.scriptengine.listeners.player.PlayerListener;
import pk.elfo.gameserver.util.Util;
import javolution.util.FastList;

@SuppressWarnings("unused")
public final class CharacterCreate extends L2GameClientPacket
{
	private static final String _C__0C_CHARACTERCREATE = "[C] 0C CharacterCreate";
	protected static final Logger _logAccounting = Logger.getLogger("accounting");
	private static final List<PlayerListener> _listeners = new FastList<PlayerListener>().shared();
	
	// cSdddddddddddd
	private String _name;
	private int _race;
	private byte _sex;
	private int _classId;
	private int _int;
	private int _str;
	private int _con;
	private int _men;
	private int _dex;
	private int _wit;
	private byte _hairStyle;
	private byte _hairColor;
	private byte _face;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_race = readD();
		_sex = (byte) readD();
		_classId = readD();
		_int = readD();
		_str = readD();
		_con = readD();
		_men = readD();
		_dex = readD();
		_wit = readD();
		_hairStyle = (byte) readD();
		_hairColor = (byte) readD();
		_face = (byte) readD();
	}
	
	@Override
	protected void runImpl()
	{
		// Last Verified: May 30, 2009 - Gracia Final - Players are able to create characters with names consisting of as little as 1,2,3 letter/number combinations.
		if ((_name.length() < 1) || (_name.length() > 16))
		{
			if (Config.DEBUG)
			{
				_log.fine("Character Creation Failure: Character name " + _name + " is invalid. Message generated: Your title cannot exceed 16 characters in length. Please try again.");
			}

			sendPacket(new CharCreateFail(CharCreateFail.REASON_16_ENG_CHARS));
			return;
		}
		
		if (Config.FORBIDDEN_NAMES.length > 1)
		{
			for (String st : Config.FORBIDDEN_NAMES)
			{
				if (_name.toLowerCase().contains(st.toLowerCase()))
				{
					sendPacket(new CharCreateFail(CharCreateFail.REASON_INCORRECT_NAME));
					return;
				}
			}
		}
		
		// Last Verified: May 30, 2009 - Gracia Final
		if (!Util.isAlphaNumeric(_name) || !isValidName(_name))
		{
			if (Config.DEBUG)
			{
				_log.fine("Character Creation Failure: Character name " + _name + " is invalid. Message generated: Incorrect name. Please try again.");
			}
			
			sendPacket(new CharCreateFail(CharCreateFail.REASON_INCORRECT_NAME));
			return;
		}
		
		if ((_face > 2) || (_face < 0))
		{
			_log.warning("Character Creation Failure: Character face " + _face + " is invalid. Possible client hack. " + getClient());
			
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		if ((_hairStyle < 0) || ((_sex == 0) && (_hairStyle > 4)) || ((_sex != 0) && (_hairStyle > 6)))
		{
			_log.warning("Character Creation Failure: Character hair style " + _hairStyle + " is invalid. Possible client hack. " + getClient());
			
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		if ((_hairColor > 3) || (_hairColor < 0))
		{
			_log.warning("Character Creation Failure: Character hair color " + _hairColor + " is invalid. Possible client hack. " + getClient());
			
			sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
			return;
		}
		
		L2PcInstance newChar = null;
		L2PcTemplate template = null;
		
		/*
		 * DrHouse: Since checks for duplicate names are done using SQL, lock must be held until data is written to DB as well.
		 */
		synchronized (CharNameTable.getInstance())
		{
			if ((CharNameTable.getInstance().accountCharNumber(getClient().getAccountName()) >= Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT) && (Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT != 0))
			{
				if (Config.DEBUG)
				{
					_log.fine("Max number of characters reached. Creation failed.");
				}
				
				sendPacket(new CharCreateFail(CharCreateFail.REASON_TOO_MANY_CHARACTERS));
				return;
			}
			else if (CharNameTable.getInstance().doesCharNameExist(_name))
			{
				if (Config.DEBUG)
				{
					_log.fine("Character Creation Failure: Message generated: You cannot create another character. Please delete the existing character and try again.");
				}
				
				sendPacket(new CharCreateFail(CharCreateFail.REASON_NAME_ALREADY_EXISTS));
				return;
			}
			
			template = CharTemplateTable.getInstance().getTemplate(_classId);
			if ((template == null) || (template.getClassBaseLevel() > 1))
			{
				if (Config.DEBUG)
				{
					_log.fine("Character Creation Failure: " + _name + " classId: " + _classId + " Template: " + template + " Message generated: Your character creation has failed.");
				}
				
				sendPacket(new CharCreateFail(CharCreateFail.REASON_CREATION_FAILED));
				return;
			}
			final PcAppearance app = new PcAppearance(_face, _hairColor, _hairStyle, _sex != 0);
			newChar = L2PcInstance.create(template, getClient().getAccountName(), _name, app);
		}
		
		newChar.setCurrentHp(template.getBaseHpMax());
		newChar.setCurrentCp(template.getBaseCpMax());
		newChar.setCurrentMp(template.getBaseMpMax());
		// newChar.setMaxLoad(template.getBaseLoad());
		
		CharCreateOk cco = new CharCreateOk();
		sendPacket(cco);
		
		initNewChar(getClient(), newChar);
		
		LogRecord record = new LogRecord(Level.INFO, "Created new character");
		record.setParameters(new Object[]
		{
			newChar,
			getClient()
		});
		_logAccounting.log(record);
	}
	
	private boolean isValidName(String text)
	{
		boolean result = true;
		String test = text;
		Pattern pattern;
		// UnAfraid: TODO: Move that into Config
		try
		{
			pattern = Pattern.compile(Config.CNAME_TEMPLATE);
		}
		catch (PatternSyntaxException e) // case of illegal pattern
		{
			_log.warning("ERROR : Character name pattern of config is wrong!");
			pattern = Pattern.compile(".*");
		}
		Matcher regexp = pattern.matcher(test);
		if (!regexp.matches())
		{
			result = false;
		}
		return result;
	}
	
	private void initNewChar(L2GameClient client, L2PcInstance newChar)
	{
		if (Config.DEBUG)
		{
			_log.fine("Character init start");
		}
		
		L2World.getInstance().storeObject(newChar);
		//quantidade de adena que os novos jogares comecarao
		if (Config.STARTING_ADENA > 0)
		{
			newChar.addAdena("Init", Config.STARTING_ADENA, null, false);
		}
		if (Config.ALLOW_CUSTOM_CHAR_VIP)
		{
			//	activeChar.getStat().add(setvip.getName.Config.CUSTOM_DAY_VIP);
			//	activeChar.getStat().add(setVipEndTime.getName.Config.CUSTOM_DAY_VIP);
			newChar.setVip(true);
			newChar.setVipEndTime(Config.CUSTOM_DAY_VIP);
		}
		// TODO: Make it random.
		final L2PcTemplate template = newChar.getTemplate();
		//spawn custom para novos jogadores
		if (Config.SPAWN_CHAR)
		{
			newChar.setXYZInvisible(Config.SPAWN_X, Config.SPAWN_Y, Config.SPAWN_Z );
		}
			else
			{
				newChar.setXYZInvisible(template.getSpawnX(), template.getSpawnY(), template.getSpawnZ());
			}
		//titulo para novos jogadores
		if (Config.CHAR_TITLE)
		{
			newChar.setTitle(Config.ADD_CHAR_TITLE);
			newChar.getAppearance().setTitleColor(Config.TITLE_COLOR);
		}
			else
			{
				newChar.setTitle("");
			}
		//mudar cor do nome de novos jogadores
		if (Config.COLOR_NEW_CHAR_NAME)
		{
			newChar.getAppearance().setNameColor(Config.NAME_COLOR);
		}
		//items na bag de novos jogadores
		if (Config.STARTING_ITEMS)
		{
			newChar.addItem("Init", Config.STARTING_ITEMS_ID, (int) Config.STARTING_ITEMS_COUNT, null, false);
		}
		
		if (Config.ENABLE_VITALITY)
		{
			newChar.setVitalityPoints(Math.min(Config.STARTING_VITALITY_POINTS, PcStat.MAX_VITALITY_POINTS), true);
		}
		//level que os novos jogadores iniciarao
		if (Config.STARTING_LEVEL > 1)
		{
			newChar.getStat().addLevel((byte) (Config.STARTING_LEVEL - 1));
		}
		//quantidade de SP que os novos jogadores iniciarao
		if (Config.STARTING_SP > 0)
		{
			newChar.getStat().addSp(Config.STARTING_SP);
		}
		
		L2ShortCut shortcut;
		// add attack shortcut
		shortcut = new L2ShortCut(0, 0, 3, 2, 0, 1);
		newChar.registerShortCut(shortcut);
		// add take shortcut
		shortcut = new L2ShortCut(3, 0, 3, 5, 0, 1);
		newChar.registerShortCut(shortcut);
		// add sit shortcut
		shortcut = new L2ShortCut(10, 0, 3, 0, 0, 1);
		newChar.registerShortCut(shortcut);
		//novos jogadores comecarem com equipamentos
		if (template.hasInitialEquipment())
		{
			L2ItemInstance item;
			for (PcItemTemplate ie : template.getInitialEquipment())
			{
				item = newChar.getInventory().addItem("Init", ie.getItemId(), ie.getCount(), newChar, null);
				if (item == null)
				{
					_log.warning("Nao foi possivel criar um item durante a criacao de char: itemId " + ie.getItemId() + ", amount " + ie.getCount() + ".");
					continue;
				}
				
				// Place Tutorial Guide shortcut.
				if (item.getItemId() == 5588)
				{
					shortcut = new L2ShortCut(11, 0, 1, item.getObjectId(), 0, 1);
					newChar.registerShortCut(shortcut);
				}
				
				if (item.isEquipable() && ie.isEquipped())
				{
					newChar.getInventory().equipItem(item);
				}
			}
		}
		
		for (L2SkillLearn skill : SkillTreesData.getInstance().getAvailableSkills(newChar, newChar.getClassId(), false, true))
		{
			newChar.addSkill(SkillTable.getInstance().getInfo(skill.getSkillId(), skill.getSkillLevel()), true);
			if ((skill.getSkillId() == 1001) || (skill.getSkillId() == 1177))
			{
				shortcut = new L2ShortCut(1, 0, 2, skill.getSkillId(), skill.getSkillLevel(), 1);
				newChar.registerShortCut(shortcut);
			}
			if (skill.getSkillId() == 1216)
			{
				shortcut = new L2ShortCut(10, 0, 2, skill.getSkillId(), skill.getSkillLevel(), 1);
				newChar.registerShortCut(shortcut);
			}
			if (Config.DEBUG)
			{
				_log.fine("Adding starter skill:" + skill.getSkillId() + " / " + skill.getSkillLevel());
			}
		}
		
		if (!Config.DISABLE_TUTORIAL)
		{
			startTutorialQuest(newChar);
		}
		
		PlayerEvent event = new PlayerEvent();
		event.setObjectId(newChar.getObjectId());
		event.setName(newChar.getName());
		event.setClient(client);
		firePlayerListener(event);
		
		newChar.setOnlineStatus(true, false);
		newChar.deleteMe();
		
		final CharSelectionInfo cl = new CharSelectionInfo(client.getAccountName(), client.getSessionId().playOkID1);
		client.getConnection().sendPacket(cl);
		client.setCharSelection(cl.getCharInfo());
		
		if (Config.DEBUG)
		{
			_log.fine("Character init end");
		}
	}
	
	/**
	 * TODO: Unhardcode it using the new listeners.
	 * @param player
	 */
	public void startTutorialQuest(L2PcInstance player)
	{
		final QuestState qs = player.getQuestState("255_Tutorial");
		Quest q = null;
		if (qs == null)
		{
			q = QuestManager.getInstance().getQuest("255_Tutorial");
		}
		if (q != null)
		{
			q.newQuestState(player).setState(State.STARTED);
		}
	}
	
	private void firePlayerListener(PlayerEvent event)
	{
		for (PlayerListener listener : _listeners)
		{
			listener.onCharCreate(event);
		}
	}
	
	public static void addPlayerListener(PlayerListener listener)
	{
		if (!_listeners.contains(listener))
		{
			_listeners.add(listener);
		}
	}
	
	public static void removePlayerListener(PlayerListener listener)
	{
		_listeners.remove(listener);
	}
	
	@Override
	public String getType()
	{
		return _C__0C_CHARACTERCREATE;
	}
}
