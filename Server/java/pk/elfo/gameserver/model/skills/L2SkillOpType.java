package pk.elfo.gameserver.model.skills;

/**
 * This enum class holds the skill operative types:
 * <ul>
 * <li>A1</li>
 * <li>A2</li>
 * <li>A3</li>
 * <li>A4</li>
 * <li>CA1</li>
 * <li>CA5</li>
 * <li>DA1</li>
 * <li>DA2</li>
 * <li>P</li>
 * <li>T</li>
 * </ul>
 * @author Zoey76
 */
public enum L2SkillOpType
{
	/**
	 * Active Skill with "Instant Effect" (for example damage skills heal/pdam/mdam/cpdam skills).
	 */
	A1,
	
	/**
	 * Active Skill with "Continuous effect + Instant effect" (for example buff/debuff or damage/heal over time skills).
	 */
	A2,
	
	/**
	 * Active Skill with "Instant effect + Continuous effect"
	 */
	A3,
	
	/**
	 * Active Skill with "Instant effect + ?" used for special event herb.
	 */
	A4,
	
	/**
	 * Continuous Active Skill with "instant effect" (instant effect casted by ticks).
	 */
	CA1,
	
	/**
	 * Continuous Active Skill with "continuous effect" (continuous effect casted by ticks).
	 */
	CA5,
	
	/**
	 * Directional Active Skill with "Charge/Rush instant effect".
	 */
	DA1,
	
	/**
	 * Directional Active Skill with "Charge/Rush Continuous effect".
	 */
	DA2,
	
	/**
	 * Passive Skill.
	 */
	P,
	
	/**
	 * Toggle Skill.
	 */
	T;
	
	/**
	 * @return {@code true} if the operative skill type is active, {@code false} otherwise.
	 */
	public boolean isActive()
	{
		switch (this)
		{
			case A1:
			case A2:
			case A3:
			case A4:
			case CA1:
			case CA5:
			case DA1:
			case DA2:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * @return {@code true} if the operative skill type is passive, {@code false} otherwise.
	 */
	public boolean isPassive()
	{
		return (this == P);
	}
	
	/**
	 * @return {@code true} if the operative skill type is toggle, {@code false} otherwise.
	 */
	public boolean isToggle()
	{
		return (this == T);
	}
}
