package pk.elfo.gameserver.model.entity;

import java.util.concurrent.ScheduledFuture;

import pk.elfo.Config;
import pk.elfo.gameserver.Announcements;
import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.ItemTable;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;
import pk.elfo.util.Rnd;

public class PkHunterEventConditions
{
    private static ScheduledFuture<?> _timerTask;

    public static void checkFinishByMobs(L2PcInstance killer)
    {
        if (PkHunterEvent.isPk(killer) && Config.ENABLE_PKHUNTEREVENT)
        {
            if (killer.getKarma() == 0)
            {
                Announcements.getInstance().announceToAll("Evento Pk Hunter terminou. " + killer.getName() + " sobreviveu.", true);
                PkHunterEvent.setPk(null);
                PkHunterEvent.setActive(false);
                killer.setTeam(0);
                endTask();
                rewardPk(killer);
            }
        }
    }

    public static void checkDie(L2Object killer, final L2PcInstance killed)
    {
        if (PkHunterEvent.isPk(killed) && Config.ENABLE_PKHUNTEREVENT)
        {
            if (killer instanceof L2PcInstance)
            {
                L2PcInstance kr = ((L2PcInstance) killer);
                SystemMessage systemMessage = null;

                for (int[] reward : Config.PKHUNTEREVENT_REWARD)
                {
                    PcInventory inv = kr.getInventory();

                    if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
                    {
                        inv.addItem("Evento PK Hunter", reward[0], reward[1], kr, kr);

                        if (reward[1] > 1)
                        {
                            systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
                            systemMessage.addItemName(reward[0]);
                            systemMessage.addItemNumber(reward[1]);
                        }
                        else
                        {
                            systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                            systemMessage.addItemName(reward[0]);
                        }
                        kr.sendPacket(systemMessage);
                    }
                    else
                    {
                        for (int i = 0; i < reward[1]; ++i)
                        {
                            inv.addItem("Evento PK Hunter", reward[0], 1, kr, kr);
                            systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                            systemMessage.addItemName(reward[0]);
                            kr.sendPacket(systemMessage);
                        }
                    }
                }
            }
            Announcements.getInstance().announceToAll("Pk Hunting Event: Event ended. " + killer.getName() + " killed the Pk.", true);
            killed.setTeam(0);
            PkHunterEvent.setActive(false);
            PkHunterEvent.setPk(null);
            endTask();

            ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
            {
                @Override
                public void run()
                {
                    killed.setKarma(0);
                }
            }, 100);
        }
    }

    public static void checkPk(L2PcInstance killer, L2Character target)
    {
        if (Config.ENABLE_PKHUNTEREVENT && !PkHunterEvent.isActive())
        {
            switch (Rnd.get(1, Config.PKHUNTEREVENT_CHANCE))
            {
                case 1:
                    killer.setKarma(900000);
                    killer.setTeam(2);
                    PkHunterEvent.setActive(true);
                    PkHunterEvent.setPk(killer);
                    PkHunterEvent.setPkLocation(killer.getX(), killer.getY(), killer.getZ());
                    startEvent();
                    break;
            }
        }
    }

    public static void endCoward(L2PcInstance activeChar)
    {
        if (PkHunterEvent.isActive())
        {
            PkHunterEvent.setActive(false);
            PkHunterEvent.setPk(null);
            Announcements.getInstance().announceToAll("Evento Pk Hunter terminou.", true);
            endTask();
        }
        activeChar.setKarma(10000);
        activeChar.sendMessage("Seu karma foi atualizado para 10.000 devido a ser um covarde, Pk Hunter Evento terminou.");
    }

    public static void startEvent()
    {
        Announcements.getInstance().announceToAll("Evento Pk Hunter comecou: " + PkHunterEvent.getPk().getName() + " esta PK, use comando .gopk para se teletransportar, onde o PK foi feito. use o comando .pkinfo para saber se voce esta longe ou perto dele.", true);
        _timerTask = ThreadPoolManager.getInstance().scheduleGeneral(new PkHunterEventTask(), Config.TIME_PKHUNTEREVENT * 60000);
    }

    static void endTask()
    {
        if (_timerTask != null)
        {
            _timerTask.cancel(false);
        }
        _timerTask = null;
    }

    public static void rewardPk(L2PcInstance kr)
    {
        SystemMessage systemMessage = null;

        for (int[] reward : Config.PKHUNTEREVENT_PK_REWARD)
        {
            PcInventory inv = kr.getInventory();

            if (ItemTable.getInstance().createDummyItem(reward[0]).isStackable())
            {
                inv.addItem("Evento PK Hunter", reward[0], reward[1], kr, kr);

                if (reward[1] > 1)
                {
                    systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S2_S1_S);
                    systemMessage.addItemName(reward[0]);
                    systemMessage.addItemNumber(reward[1]);
                }
                else
                {
                    systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                    systemMessage.addItemName(reward[0]);
                }
                kr.sendPacket(systemMessage);
            }
            else
            {
                for (int i = 0; i < reward[1]; ++i)
                {
                    inv.addItem("Evento PK Hunter", reward[0], 1, kr, kr);
                    systemMessage = SystemMessage.getSystemMessage(SystemMessageId.EARNED_ITEM_S1);
                    systemMessage.addItemName(reward[0]);
                    kr.sendPacket(systemMessage);
                }
            }
        }
    }
}