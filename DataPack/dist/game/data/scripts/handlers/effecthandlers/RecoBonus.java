package handlers.effecthandlers;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.effects.EffectTemplate;
import pk.elfo.gameserver.model.effects.L2Effect;
import pk.elfo.gameserver.model.effects.L2EffectType;
import pk.elfo.gameserver.model.stats.Env;

/**
 * Projeto PkElfo
 */
 
public class RecoBonus extends L2Effect
{
    public RecoBonus(Env env, EffectTemplate template)
    {
        super(env, template);
    }

    /**
     *
     * @see pk.elfo.gameserver.model.effects.L2Effect#getEffectType()
     */
    @Override
    public L2EffectType getEffectType()
    {
        return L2EffectType.BUFF;
    }

    /**
     *
     * @see pk.elfo.gameserver.model.effects.L2Effect#onStart()
     */
    @Override
    public boolean onStart()
    {
        if (!(getEffected() instanceof L2PcInstance))
        {
            return false;
        }

        ((L2PcInstance) getEffected()).setRecomBonusType(1).setRecoBonusActive(true);
        return true;
    }

    /**
     *
     * @see pk.elfo.gameserver.model.effects.L2Effect#onExit()
     */
    @Override
    public void onExit()
    {
        ((L2PcInstance) getEffected()).setRecomBonusType(0).setRecoBonusActive(false);
    }

    @Override
    protected boolean effectCanBeStolen()
    {
        return false;
    }

    /**
     *
     * @see pk.elfo.gameserver.model.effects.L2Effect#onActionTime()
     */
    @Override
    public boolean onActionTime()
    {
        return false;
    }
}