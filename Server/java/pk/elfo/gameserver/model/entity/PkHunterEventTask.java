package pk.elfo.gameserver.model.entity;

import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class PkHunterEventTask implements Runnable
{
    @Override
    public void run()
    {
        if (PkHunterEvent.isPkOnline())
        {
            L2PcInstance kr = PkHunterEvent.getPk();
            kr.setKarma(0);
            kr.setTeam(0);
            PkHunterEventConditions.rewardPk(kr);
        }
        Announcements.getInstance().announceToAll("Evento Pk Hunter terminou. " + PkHunterEvent.getPk().getName() + " sobreviveu.", true);
        PkHunterEvent.setActive(false);
        PkHunterEvent.setPk(null);
    }
}