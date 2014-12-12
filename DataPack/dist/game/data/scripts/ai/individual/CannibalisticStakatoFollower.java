package ai.individual;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2MonsterInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class CannibalisticStakatoFollower extends AbstractNpcAI
{
    private static final int CANNIBALISTIC_LEADER = 22625;

    public CannibalisticStakatoFollower(String name, String descr)
    {
        super(name, descr);
        addAttackId(CANNIBALISTIC_LEADER);
    }

    @Override
    public String onAttack(L2Npc npc, L2PcInstance player, int damage, boolean isPet)
    {
        if (npc.getMaxHp() * 0.3D > npc.getCurrentHp())
        {
            if (Rnd.get(100) <= 25)
            {
                L2Npc minion = getLeaderMinion(npc);
                if ((minion != null) && (!minion.isDead()))
                {
                    npc.broadcastPacket(new MagicSkillUse(npc, minion, 4485, 1, 3000, 0));
                    ThreadPoolManager.getInstance().scheduleGeneral(new eatTask(npc, minion), 3000L);
                }
            }
        }
        return super.onAttack(npc, player, damage, isPet);
    }

    public L2Npc getLeaderMinion(L2Npc leader)
    {
        if (((L2MonsterInstance)leader).getMinionList().getSpawnedMinions().size() > 0)
        {
            return ((L2MonsterInstance)leader).getMinionList().getSpawnedMinions().get(0);
        }
        return null;
    }

    private class eatTask implements Runnable
    {
        private L2Npc _npc;
        private L2Npc _minion;

        private eatTask(L2Npc npc, L2Npc minion)
        {
            this._npc = npc;
            this._minion = minion;
        }

        @Override
        public void run()
        {
            if (this._minion == null)
            {
                return;
            }
            double hpToSacrifice = this._minion.getCurrentHp();
            this._npc.setCurrentHp(this._npc.getCurrentHp() + hpToSacrifice);
            this._npc.broadcastPacket(new MagicSkillUse(this._npc, this._minion, 4484, 1, 1000, 0));
            this._minion.doDie(this._minion);
        }
    }

    public static void main(String[] args)
    {
        new CannibalisticStakatoFollower(CannibalisticStakatoFollower.class.getSimpleName(), "ai");
    }
}