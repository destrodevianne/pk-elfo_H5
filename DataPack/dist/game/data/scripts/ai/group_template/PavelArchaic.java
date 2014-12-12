package ai.group_template;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public final class PavelArchaic extends AbstractNpcAI
{
    private static final int SAFETY_DEVICE = 18917; // Pavel Safety Device
    private static final int PINCER_GOLEM = 22801; // Cruel Pincer Golem
    private static final int PINCER_GOLEM2 = 22802; // Cruel Pincer Golem
    private static final int PINCER_GOLEM3 = 22803; // Cruel Pincer Golem
    private static final int JACKHAMMER_GOLEM = 22804; // Horrifying Jackhammer Golem

    private PavelArchaic()
    {
        super(PavelArchaic.class.getSimpleName(), "ai");
        addKillId(SAFETY_DEVICE, PINCER_GOLEM, JACKHAMMER_GOLEM);
    }

    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
    {
        if (getRandom(100) < 70)
        {
            final L2Attackable golem1 = (L2Attackable) addSpawn(PINCER_GOLEM2, npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, false);
            attackPlayer(golem1, killer);

            final L2Attackable golem2 = (L2Attackable) addSpawn(PINCER_GOLEM3, npc.getX(), npc.getY(), npc.getZ() + 10, npc.getHeading(), false, 0, false);
            attackPlayer(golem2, killer);
        }
        return super.onKill(npc, killer, isSummon);
    }

    public static void main(String[] args)
    {
        new PavelArchaic();
    }
}