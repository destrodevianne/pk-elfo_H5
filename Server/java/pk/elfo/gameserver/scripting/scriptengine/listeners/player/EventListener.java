package pk.elfo.gameserver.scripting.scriptengine.listeners.player;

import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2JListener;

/**
 * PkElfo
 */

public abstract class EventListener extends L2JListener
{
    public EventListener(L2PcInstance player)
    {
        super.player = player;
        register();
    }

    /**
     * @return {@code true} if player is on event, {@code false} otherwise.
     */
    public abstract boolean isOnEvent();

    /**
     * @return {@code true} if player is blocked from leaving the game, {@code false} otherwise.
     */
    public abstract boolean isBlockingExit();

    /**
     * @return {@code true} if player is blocked from receiving death penalty upon death, {@code false} otherwise.
     */
    public abstract boolean isBlockingDeathPenalty();

    @Override
    public void register()
    {
        if (player != null)
        {
            player.addEventListener(this);
        }
    }

    @Override
    public void unregister()
    {
        if (player != null)
        {
            player.removeEventListener(this);
        }
    }
}