package pk.elfo.gameserver.enums;

public enum IllegalActionPunishmentType
{
	NONE,
	BROADCAST,
	KICK,
	KICKBAN,
	JAIL;
	
	public static final IllegalActionPunishmentType findByName(String name)
	{
		for (IllegalActionPunishmentType type : values())
		{
			if (type.name().toLowerCase().equals(name.toLowerCase()))
			{
				return type;
			}
		}
		return NONE;
	}
}