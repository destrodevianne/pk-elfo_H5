package ai.npc.DragonVortex;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public final class DragonVortex extends AbstractNpcAI
{
    // NPC
    private static final int VORTEX = 32871;
    // Raids
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
    // Item
    private static final int LARGE_DRAGON_BONE = 17248;
    // Misc
    private static final int DESPAWN_DELAY = 1800000; // 30min

    private DragonVortex()
    {
        super(DragonVortex.class.getSimpleName(), "ai/npc");
        addStartNpc(VORTEX);
        addFirstTalkId(VORTEX);
        addTalkId(VORTEX);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        if ("Spawn".equals(event))
        {
            if (hasQuestItems(player, LARGE_DRAGON_BONE))
            {
                takeItems(player, LARGE_DRAGON_BONE, 1);
                final int random = getRandom(1000);
                int raid = 0;
                if (random < 292)
                {
                    raid = RAIDS[0]; // Emerald Horn 29.2%
                }
                else if (random < 516)
                {
                    raid = RAIDS[1]; // Dust Rider 22.4%
                }
                else if (random < 692)
                {
                    raid = RAIDS[2]; // Bleeding Fly 17.6%
                }
                else if (random < 808)
                {
                    raid = RAIDS[3]; // Blackdagger Wing 11.6%
                }
                else if (random < 900)
                {
                    raid = RAIDS[4]; // Spike Slasher 9.2%
                }
                else if (random < 956)
                {
                    raid = RAIDS[5]; // Shadow Summoner 5.6%
                }
                else
                {
                    raid = RAIDS[6]; // Muscle Bomber 4.4%
                }
                addSpawn(raid, npc.getX() + getRandom(-500, 500), npc.getY() + getRandom(-500, 500), npc.getZ() + 10, 0, false, DESPAWN_DELAY, true);
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
        new DragonVortex();
    }
}