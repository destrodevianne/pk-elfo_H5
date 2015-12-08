package pk.elfo.gameserver.model;

import java.util.List;
import java.util.Map;

import pk.elfo.Config;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.items.L2Item;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;

/**
 * PkElfo
 */
public class EnchantScroll extends EnchantItem
{
	private final boolean _isBlessed;
	private final boolean _isSafe;
	private final Map<Integer, Double> _enchantSteps;
	
	/**
	 * @param set
	 * @param items
	 * @param enchantSteps
	 */
	public EnchantScroll(StatsSet set, List<Integer> items, Map<Integer, Double> enchantSteps)
	{
		super(set, items);
		
		_isBlessed = set.getBool("isBlessed", false);
		_isSafe = set.getBool("isSafe", false);
		_enchantSteps = enchantSteps;
	}
	
	/**
	 * @return true for blessed scrolls
	 */
	public final boolean isBlessed()
	{
		return _isBlessed;
	}
	
	/**
	 * @return true for safe-enchant scrolls (enchant level will remain on failure)
	 */
	public final boolean isSafe()
	{
		return _isSafe;
	}
	
	/**
	 * @param enchantItem
	 * @param supportItem
	 * @return
	 */
	public final boolean isValid(L2ItemInstance enchantItem, EnchantItem supportItem)
	{
		// blessed scrolls can't use support items
		if ((supportItem != null) && (!supportItem.isValid(enchantItem) || isBlessed()))
		{
			return false;
		}
		
		return super.isValid(enchantItem);
	}
	
	/**
	 * @param enchantItem
	 * @param supportItem
	 * @return
	 */
	public final double getChance(L2ItemInstance enchantItem, EnchantItem supportItem)
	{
		if (!isValid(enchantItem, supportItem))
		{
			return -1;
		}
		
		boolean fullBody = enchantItem.getItem().getBodyPart() == L2Item.SLOT_FULL_ARMOR;
		if ((enchantItem.getEnchantLevel() < Config.ENCHANT_SAFE_MAX) || (fullBody && (enchantItem.getEnchantLevel() < Config.ENCHANT_SAFE_MAX_FULL)))
		{
			return 100;
		}
		
		L2PcInstance activeChar = L2World.getInstance().getPlayer(enchantItem.getOwnerId());
		int level = enchantItem.getEnchantLevel() + 1;
		double chance = _chanceAdd;
		
		if (_enchantSteps.containsKey(level))
		{
			chance = _enchantSteps.get(level);
		}
		
		if (activeChar.isDebug())
		{
			activeChar.sendDebugMessage("Enchant Level: " + level + " Chance: " + chance + " " + (supportItem != null ? "Support item: " + supportItem.getChanceAdd() : ""));
		}
		
		if ((supportItem != null) && !_isBlessed)
		{
			chance *= supportItem.getChanceAdd();
		}
		
		return chance;
	}
	/**
	 * @return the maximum enchant level that this scroll/item can be used with
	 */
	public int getMaxEnchantLevel()
	{
		return _maxEnchantLevel;
	}
}
