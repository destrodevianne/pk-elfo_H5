package ai.individual;

import java.util.Calendar;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
 
/**
 * Projeto PkElfo
 */

public class Enira extends AbstractNpcAI
{
    // Npc
    private static final int ENIRA = 25625;

    public Enira(int questId, String name, String descr)
    {
        super(name, descr);
        eniraSpawn();
    }

    private void eniraSpawn()
    {
        Calendar _date = Calendar.getInstance();
        final int newSecond = _date.get(Calendar.SECOND);
        final int newMinute = _date.get(Calendar.MINUTE);
        final int newHour = _date.get(Calendar.HOUR);

        final int targetHour = (((24 - newHour) * 60) * 60) * 1000;
        final int extraMinutesAndSeconds = (((60 - newMinute) * 60) + (60 - newSecond)) * 1000;
        final int timerDuration = targetHour + extraMinutesAndSeconds;

        startQuestTimer("enira_spawn", timerDuration, null, null);
    }

    @Override
    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        if (event.equalsIgnoreCase("enira_spawn"))
        {
            if (getRandom(100) <= 40)
            {
                for (L2Spawn eniraSpawn : SpawnTable.getInstance().getSpawns(ENIRA))
                {
                    if (eniraSpawn != null)
                    {
                        eniraSpawn.getLastSpawn();
                    }
                    addSpawn(ENIRA, -181989, 208968, 4030, 0, false, 3600000);
                }
            }
            eniraSpawn();
        }
        return null;
    }

    public static void main(String[] args)
    {
        new Enira(-1, Enira.class.getSimpleName(), "ai");
    }
}