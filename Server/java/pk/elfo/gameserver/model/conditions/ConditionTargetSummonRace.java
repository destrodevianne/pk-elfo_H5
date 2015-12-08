package pk.elfo.gameserver.model.conditions;

import java.util.Set;

import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.base.NpcRace;
import pk.elfo.gameserver.model.stats.Env;

public class ConditionTargetSummonRace extends Condition
{
    private final Set<NpcRace> _races;

    /**
     * Instantiates a new condition target Summon race
     * @param races the races
     */
    public ConditionTargetSummonRace(Set<NpcRace> races)
    {
        _races = races;
    }

    @Override
    public boolean testImpl(Env env)
    {
        final L2Summon summon = env.getTarget() instanceof L2Summon ? (L2Summon) env.getTarget() : null;
        return (summon != null) && _races.contains(summon.getTemplate().getRace());
    }
}