package ai.individual;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.util.Rnd;
import ai.npc.AbstractNpcAI;
 
/**
 * Projeto PkElfo
 */

public class EtisEtina extends AbstractNpcAI
{
    private static final int ETIS = 18949;
    private static final int GUARD1 = 18950;
    private static final int GUARD2 = 18951;
    private boolean _FirstAttacked = false;

    public EtisEtina(String name, String descr)
    {
        super(name, descr);
        addAttackId(ETIS);
    }

    @Override
    public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet)
    {
        if (npc.getNpcId() == ETIS)
        {
            int maxHp = npc.getMaxHp();
            double nowHp = npc.getStatus().getCurrentHp();

            if (nowHp < maxHp * 0.7D)
            {
                if (this._FirstAttacked)
                {
                    return null;
                }

                L2Npc warrior = addSpawn(GUARD1, npc.getX() + Rnd.get(10, 50), npc.getY() + Rnd.get(10, 50), npc.getZ(), 0, false, 0L, false, npc.getInstanceId());
                warrior.setRunning();
                ((L2Attackable)warrior).addDamageHate(attacker, 1, 99999);
                warrior.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);

                L2Npc warrior1 = addSpawn(GUARD2, npc.getX() + Rnd.get(10, 80), npc.getY() + Rnd.get(10, 80), npc.getZ(), 0, false, 0L, false, npc.getInstanceId());
                warrior1.setRunning();
                ((L2Attackable)warrior1).addDamageHate(attacker, 1, 99999);
                warrior1.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, attacker);
                this._FirstAttacked = true;
            }
        }
        return null;
    }

    public static void main(String[] args)
    {
        new EtisEtina(EtisEtina.class.getSimpleName(), "ai");
    }
}