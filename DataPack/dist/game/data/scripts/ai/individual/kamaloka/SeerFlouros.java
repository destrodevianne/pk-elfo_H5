package ai.individual.kamaloka;

import ai.group_template.L2AttackableAIScript;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class SeerFlouros extends L2AttackableAIScript
{

    private static final int SEER = 18559;
    private static final int GUARD = 18560;
    private int isGuardSpawn = 0;

    public SeerFlouros(int questId, String name, String descr)
    {
        super(questId, name, descr);
        addAttackId(SEER);
        addKillId(GUARD);
        addKillId(SEER);
    }

    @Override
    public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        int x = player.getX();
        int y = player.getY();

        if (event.equalsIgnoreCase("time_to_spawn"))
        {
            if (isGuardSpawn == 0)
            {
                addSpawn(GUARD, x, y, npc.getZ(), 0, false, 0, false, npc.getInstanceId(), 0);
                isGuardSpawn = 1;
            }
            else if (isGuardSpawn == 1)
            {
                addSpawn(GUARD, x, y, npc.getZ(), 0, false, 0, false, npc.getInstanceId(), 0);
                isGuardSpawn = 2;
            }
            else if (isGuardSpawn == 2)
            {
                return null;
            }
        }
        else if (event.equalsIgnoreCase("time_to_spawn1"))
        {
            if (isGuardSpawn == 1)
            {
                addSpawn(GUARD, x, y, npc.getZ(), 0, false, 0, false, npc.getInstanceId(), 0);
                isGuardSpawn = 2;
            }
            else if (isGuardSpawn == 2)
            {
                return null;
            }
        }
        return super.onAdvEvent(event, npc, player);
    }

    @Override
    public final String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
    {
        int npcId = npc.getId();
        if (npcId == SEER)
        {
            if (isGuardSpawn == 0)
            {
                startQuestTimer("time_to_spawn", 30000, npc, player);
            }
            else if (isGuardSpawn == 1)
            {
                startQuestTimer("time_to_spawn1", 60000, npc, player);
            }
        }
        return super.onAttack(npc, player, damage, isPet);

    }

    @Override
    public final String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
    {
        int npcId = npc.getId();
        if (npcId == GUARD)
        {
            if (isGuardSpawn == 0)
            {
                startQuestTimer("time_to_spawn", 30000, npc, player);
            }
            else if (isGuardSpawn == 1)
            {
                startQuestTimer("time_to_spawn1", 60000, npc, player);
                isGuardSpawn -= 1;
            }
            else if (isGuardSpawn == 2)
            {
                isGuardSpawn -= 1;
                startQuestTimer("time_to_spawn1", 60000, npc, player);
            }
        }
        else if (npcId == SEER)
        {
            cancelQuestTimer("time_to_spawn", npc, player);
            cancelQuestTimer("time_to_spawn1", npc, player);
        }
        return super.onKill(npc, player, isPet);
    }

    public static void main(String[] args)
    {
        new SeerFlouros(-1, SeerFlouros.class.getSimpleName(), "ai/individual/kamaloka");
    }
}