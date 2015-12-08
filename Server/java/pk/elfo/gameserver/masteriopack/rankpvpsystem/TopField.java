package pk.elfo.gameserver.masteriopack.rankpvpsystem;

/**
 * PkElfo
 */
public class TopField
{
	private int _characterId = 0;
	private String _characterName = null;
	private int _characterLevel = 0;
	private int _characterBaseClassId = 0;
	private long _value = 0; 			// rank points or total kills
	private int _topPosition = 0;
	
	/**
	 * @return the _characterId
	 */
	public int getCharacterId()
	{
		return _characterId;
	}
	/**
	 * @param characterId the _characterId to set
	 */
	public void setCharacterId(int characterId)
	{
		_characterId = characterId;
	}
	/**
	 * Get Kills or Points. This fields are the same.
	 * @return the _value
	 */
	public long getValue()
	{
		return _value;
	}
	/**
	 * Set Kills or Points. This fields are the same.
	 * @param value the _value to set
	 */
	public void setValue(long value)
	{
		_value = value;
	}
	/**
	 * @return the _characterName
	 */
	public String getCharacterName()
	{
		return _characterName;
	}
	/**
	 * @param characterName the _characterName to set
	 */
	public void setCharacterName(String characterName)
	{
		_characterName = characterName;
	}
	/**
	 * @return the _characterLevel
	 */
	public int getCharacterLevel()
	{
		return _characterLevel;
	}
	/**
	 * @param characterLevel the _characterLevel to set
	 */
	public void setCharacterLevel(int characterLevel)
	{
		_characterLevel = characterLevel;
	}
	/**
	 * @return the _characterBaseClassId
	 */
	public int getCharacterBaseClassId()
	{
		return _characterBaseClassId;
	}
	/**
	 * @param characterBaseClassId the _characterBaseClassId to set
	 */
	public void setCharacterBaseClassId(int characterBaseClassId)
	{
		_characterBaseClassId = characterBaseClassId;
	}
	/**
	 * Returns actual position on top list.
	 * @return 
	 */
	public int getTopPosition() 
	{
		return _topPosition;
	}
	/**
	 * Set actual position on top list.
	 * @param topPosition 
	 */
	public void setTopPosition(int topPosition) 
	{
		_topPosition = topPosition;
	}
	
}
