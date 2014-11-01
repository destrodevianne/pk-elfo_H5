package ai.npc.DragonVortex;

import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import ai.npc.AbstractNpcAI;

/**
 * PkElfo
 */
public class DragonVortex extends AbstractNpcAI
{
	private static final int VORTEX = 32871;
	
	private static final int[] RAIDS =
	{
        25718, // Emerald Horn
        25719, // Dust Rider
        25720, // Bleeding Fly
        25721, // Blackdagger Wing
        25722, // Shadow Summoner
        25723, // Spike Slasher
        25724, // Muscle Bomber
	};
	
	private static final int LARGE_DRAGON_BONE = 17248;
	
	private static final int DESPAWN_DELAY = 1800000; // 30min
	
	private DragonVortex(String name, String descr)
	{
		super(name, descr);
		addStartNpc(VORTEX);
		addFirstTalkId(VORTEX);
		addTalkId(VORTEX);
		addKillId(RAIDS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if ("Spawn".equals(event))
		{
			int raid = RAIDS[getRandom(RAIDS.length)];
			
			if (hasQuestItems(player, LARGE_DRAGON_BONE))
			{
				takeItems(player, LARGE_DRAGON_BONE, 1);
				addSpawn(raid, new Location(player.getX() + getRandom(400), player.getY() + getRandom(400), player.getZ(), player.getHeading()), true, DESPAWN_DELAY);
			}
			else
			{
				return "32871-no.html";
			}
		}
		
		return super.onAdvEvent(event, npc, player);
	}
	
	public static void main(String[] args)
	{
		new DragonVortex(DragonVortex.class.getSimpleName(), "ai/npc/");
	}
}
