package pk.elfo.gameserver.util;

import pk.elfo.Config;
import pk.elfo.gameserver.network.L2GameClient;

/**
 * Collection of flood protectors for single player.
 */
public final class FloodProtectors
{
	private final FloodProtectorAction _useItem;
	private final FloodProtectorAction _rollDice;
	private final FloodProtectorAction _firework;
	private final FloodProtectorAction _itemPetSummon;
	private final FloodProtectorAction _heroVoice;
	private final FloodProtectorAction _globalChat;
	private final FloodProtectorAction _subclass;
	private final FloodProtectorAction _dropItem;
	private final FloodProtectorAction _serverBypass;
	private final FloodProtectorAction _multiSell;
	private final FloodProtectorAction _transaction;
	private final FloodProtectorAction _manufacture;
	private final FloodProtectorAction _manor;
	private final FloodProtectorAction _sendMail;
	private final FloodProtectorAction _characterSelect;
	private final FloodProtectorAction _itemAuction;
	
	/**
	 * Creates new instance of FloodProtectors.
	 * @param client game client for which the collection of flood protectors is being created.
	 */
	public FloodProtectors(final L2GameClient client)
	{
		super();
		_useItem = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_USE_ITEM);
		_rollDice = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_ROLL_DICE);
		_firework = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_FIREWORK);
		_itemPetSummon = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_ITEM_PET_SUMMON);
		_heroVoice = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_HERO_VOICE);
		_globalChat = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_GLOBAL_CHAT);
		_subclass = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_SUBCLASS);
		_dropItem = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_DROP_ITEM);
		_serverBypass = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_SERVER_BYPASS);
		_multiSell = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_MULTISELL);
		_transaction = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_TRANSACTION);
		_manufacture = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_MANUFACTURE);
		_manor = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_MANOR);
		_sendMail = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_SENDMAIL);
		_characterSelect = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_CHARACTER_SELECT);
		_itemAuction = new FloodProtectorAction(client, Config.FLOOD_PROTECTOR_ITEM_AUCTION);
	}
	
	/**
	 * Returns {@link #_useItem}.
	 * @return {@link #_useItem}
	 */
	public FloodProtectorAction getUseItem()
	{
		return _useItem;
	}
	
	/**
	 * Returns {@link #_rollDice}.
	 * @return {@link #_rollDice}
	 */
	public FloodProtectorAction getRollDice()
	{
		return _rollDice;
	}
	
	/**
	 * Returns {@link #_firework}.
	 * @return {@link #_firework}
	 */
	public FloodProtectorAction getFirework()
	{
		return _firework;
	}
	
	/**
	 * Returns {@link #_itemPetSummon}.
	 * @return {@link #_itemPetSummon}
	 */
	public FloodProtectorAction getItemPetSummon()
	{
		return _itemPetSummon;
	}
	
	/**
	 * Returns {@link #_heroVoice}.
	 * @return {@link #_heroVoice}
	 */
	public FloodProtectorAction getHeroVoice()
	{
		return _heroVoice;
	}
	
	/**
	 * Returns {@link #_globalChat}.
	 * @return {@link #_globalChat}
	 */
	public FloodProtectorAction getGlobalChat()
	{
		return _globalChat;
	}
	
	/**
	 * Returns {@link #_subclass}.
	 * @return {@link #_subclass}
	 */
	public FloodProtectorAction getSubclass()
	{
		return _subclass;
	}
	
	/**
	 * Returns {@link #_dropItem}.
	 * @return {@link #_dropItem}
	 */
	public FloodProtectorAction getDropItem()
	{
		return _dropItem;
	}
	
	/**
	 * Returns {@link #_serverBypass}.
	 * @return {@link #_serverBypass}
	 */
	public FloodProtectorAction getServerBypass()
	{
		return _serverBypass;
	}
	
	/**
	 * @return {@link #_multiSell}
	 */
	public FloodProtectorAction getMultiSell()
	{
		return _multiSell;
	}
	
	/**
	 * Returns {@link #_transaction}.
	 * @return {@link #_transaction}
	 */
	public FloodProtectorAction getTransaction()
	{
		return _transaction;
	}
	
	/**
	 * Returns {@link #_manufacture}.
	 * @return {@link #_manufacture}
	 */
	public FloodProtectorAction getManufacture()
	{
		return _manufacture;
	}
	
	/**
	 * Returns {@link #_manor}.
	 * @return {@link #_manor}
	 */
	public FloodProtectorAction getManor()
	{
		return _manor;
	}
	
	/**
	 * Returns {@link #_sendMail}.
	 * @return {@link #_sendMail}
	 */
	public FloodProtectorAction getSendMail()
	{
		return _sendMail;
	}
	
	/**
	 * Returns {@link #_characterSelect}.
	 * @return {@link #_characterSelect}
	 */
	public FloodProtectorAction getCharacterSelect()
	{
		return _characterSelect;
	}
	
	/**
	 * Returns {@link #_itemAuction}.
	 * @return {@link #_itemAuction}
	 */
	public FloodProtectorAction getItemAuction()
	{
		return _itemAuction;
	}
}