package king.server.gameserver.datatables;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import king.server.Config;
import king.server.gameserver.GameTimeController;
import king.server.gameserver.engines.DocumentParser;
import king.server.gameserver.model.actor.L2Character;

public final class HitConditionBonus extends DocumentParser
{
	private int frontBonus = 0;
	private int sideBonus = 0;
	private int backBonus = 0;
	private int highBonus = 0;
	private int lowBonus = 0;
	private int darkBonus = 0;
	private int rainBonus = 0;
	
	/**
	 * Instantiates a new hit condition bonus.
	 */
	protected HitConditionBonus()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseDatapackFile("data/stats/hitConditionBonus.xml");
		_log.info(getClass().getSimpleName() + ": Hit Condition bonuses.");
		if (Config.DEBUG)
		{
			_log.info(getClass().getSimpleName() + ": Front bonus: " + frontBonus);
			_log.info(getClass().getSimpleName() + ": Side bonus: " + sideBonus);
			_log.info(getClass().getSimpleName() + ": Back bonus: " + backBonus);
			_log.info(getClass().getSimpleName() + ": High bonus: " + highBonus);
			_log.info(getClass().getSimpleName() + ": Low bonus: " + lowBonus);
			_log.info(getClass().getSimpleName() + ": Dark bonus: " + darkBonus);
			_log.info(getClass().getSimpleName() + ": Rain bonus: " + rainBonus);
		}
	}
	
	@Override
	protected void parseDocument()
	{
		final Node n = getCurrentDocument().getFirstChild();
		NamedNodeMap attrs;
		for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
		{
			attrs = d.getAttributes();
			switch (d.getNodeName())
			{
				case "front":
					frontBonus = parseInt(attrs, "val");
					break;
				case "side":
					sideBonus = parseInt(attrs, "val");
					break;
				case "back":
					backBonus = parseInt(attrs, "val");
					break;
				case "high":
					highBonus = parseInt(attrs, "val");
					break;
				case "low":
					lowBonus = parseInt(attrs, "val");
					break;
				case "dark":
					darkBonus = parseInt(attrs, "val");
					break;
				case "rain":
					rainBonus = parseInt(attrs, "val");
					break;
			}
		}
	}
	
	/**
	 * Gets the condition bonus.
	 * @param attacker the attacking character.
	 * @param target the attacked character.
	 * @return the bonus of the attacker against the target.
	 */
	public double getConditionBonus(L2Character attacker, L2Character target)
	{
		double mod = 100;
		// Get high or low bonus
		if ((attacker.getZ() - target.getZ()) > 50)
		{
			mod += highBonus;
		}
		else if ((attacker.getZ() - target.getZ()) < -50)
		{
			mod += lowBonus;
		}
		
		// Get weather bonus
		if (GameTimeController.getInstance().isNowNight())
		{
			mod += darkBonus;
			// else if () No rain support yet.
			// chance += hitConditionBonus.rainBonus;
		}
		
		// Get side bonus
		if (attacker.isBehindTarget())
		{
			mod += backBonus;
		}
		else if (attacker.isInFrontOfTarget())
		{
			mod += frontBonus;
		}
		else
		{
			mod += sideBonus;
		}
		
		// If (mod / 100) is less than 0, return 0, because we can't lower more than 100%.
		return Math.max(mod / 100, 0);
	}
	
	/**
	 * Gets the single instance of HitConditionBonus.
	 * @return single instance of HitConditionBonus
	 */
	public static HitConditionBonus getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final HitConditionBonus _instance = new HitConditionBonus();
	}
}