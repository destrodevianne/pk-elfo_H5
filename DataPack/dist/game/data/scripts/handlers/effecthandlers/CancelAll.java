package handlers.effecthandlers;

import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * PkElfo
 */

public class CancelAll extends L2Effect
{
    public CancelAll(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    @Override
    public L2EffectType getEffectType()
    {
        return L2EffectType.CANCEL_ALL;
    }

    @Override
    public boolean onStart()
    {
        getEffected().stopAllEffects();
        return false;
    }

    @Override
    public boolean onActionTime()
    {
        return false;
    }
}