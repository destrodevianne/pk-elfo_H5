package pk.elfo.gameserver.model.actor.instance;

import pk.elfo.Config;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;

/**
 * This class ...
 * @version $Revision: $ $Date: $
 * @author LBaldi
 */
public class L2AdventurerInstance extends L2NpcInstance
{
	public L2AdventurerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2AdventurerInstance);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String pom = "";
		
		if (val == 0)
		{
			pom = "" + npcId;
		}
		else
		{
			pom = npcId + "-" + val;
		}
		
		if (Config.PC_BANG_ENABLED)
		{
			return "data/html/adventurer_guildsman/" + pom + "-pcbangpoint.htm";
		}
		return "data/html/adventurer_guildsman/" + pom + ".htm";
	}
}