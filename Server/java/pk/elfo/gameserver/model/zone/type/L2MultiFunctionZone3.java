package pk.elfo.gameserver.model.zone.type;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javolution.util.FastList;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Summon;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.model.zone.ZoneId;
import pk.elfo.util.Rnd;

/**
 * PkElfo
 */

public class L2MultiFunctionZone3 extends L2RespawnZone
{

    public L2MultiFunctionZone3(int id)
    {
        super(id);
        loadConfigs();
    }

    public static boolean pvp_enabled3, restart_zone3, store_zone3, logout_zone3, revive_noblesse3, revive_heal3, revive3, remove_buffs3, remove_pets3, give_noblesse3;
    static int radius3, enchant3, revive_delay3;
    static int[][] spawn_loc3;
    L2Skill noblesse3 = L2Skill.valueOf(1323, 1);
    private static List<String> items3 = new FastList<>();
    private static List<String> grades3 = new FastList<>(), classes3 = new FastList<>();
    public static List<int[]> rewards3;
    static String[] gradeNames3 =
    {
        "",
        "D",
        "C",
        "B",
        "A",
        "S",
        "S80",
        "S84"
    };

    @Override
    protected void onEnter(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
        character.setInsideZone(ZoneId.MULTI_FUNCTION3, true);
        if (!store_zone3)
        {
            character.setInsideZone(ZoneId.NO_STORE, true);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if ((classes3 != null) && classes3.contains("" + activeChar.getClassId().getId()))
            {
                activeChar.teleToLocation(83597, 147888, -3405);
                activeChar.sendMessage("Sua classe nao e permitido no MultiFunction zone.");
                return;
            }

            for (L2ItemInstance o : activeChar.getInventory()._items)
            {
                if (o.isEquipable() && o.isEquipped() && !checkItem(o))
                {
                    int slot = activeChar.getInventory().getSlotFromItem(o);
                    activeChar.getInventory().unEquipItemInBodySlot(slot);
                    activeChar.sendMessage(o.getName() + " unequiped porque nao e permitido dentro desta zone.");
                }
            }
            activeChar.sendMessage("Voce entrou em uma MultiFunction zone.");
            clear(activeChar);
            if (give_noblesse3)
            {
                noblesse3.getEffects(activeChar, activeChar);
            }
            if (pvp_enabled3 && (activeChar.getPvpFlag() == 0))
            {
                activeChar.updatePvPFlag(1);
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.MULTI_FUNCTION3, false);
        if (!store_zone3)
        {
            character.setInsideZone(ZoneId.NO_STORE, false);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            activeChar.sendMessage("Voce saiu de uma MultiFunction zone.");

            if (pvp_enabled3)
            {
                activeChar.stopPvPFlag();
            }
        }
    }

    @Override
    public void onDieInside(final L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            final L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive3)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeChar.doRevive();
                        heal(activeChar);
                        int[] loc = spawn_loc3[Rnd.get(spawn_loc3.length)];
                        activeChar.teleToLocation(loc[0] + Rnd.get(-radius3, radius3), loc[1] + Rnd.get(-radius3, radius3), loc[2]);
                    }
                }, revive_delay3 * 1000);
            }
        }
    }

    @Override
    public void onReviveInside(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive_noblesse3)
            {
                noblesse3.getEffects(activeChar, activeChar);
            }
            if (revive_heal3)
            {
                heal(activeChar);
            }
        }
    }

    private void clear(L2PcInstance player)
    {
        if (remove_buffs3)
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            if (remove_pets3)
            {
                L2Summon pet = player.getSummon();
                if (pet != null)
                {
                    pet.stopAllEffectsExceptThoseThatLastThroughDeath();
                    pet.unSummon(player);
                }
            }
        }
        else
        {
            if (remove_pets3)
            {
                L2Summon pet = player.getSummon();
                if (pet != null)
                {
                    pet.unSummon(player);
                }
            }
        }
    }

    static void heal(L2PcInstance activeChar)
    {
        activeChar.setCurrentHp(activeChar.getMaxHp());
        activeChar.setCurrentCp(activeChar.getMaxCp());
        activeChar.setCurrentMp(activeChar.getMaxMp());
    }

    public static void givereward(L2PcInstance player)
    {
        if (player.isInsideZone(ZoneId.MULTI_FUNCTION3))
        {
            for (int[] reward : rewards3)
            {
                PcInventory inv = player.getInventory();
                inv.addItem("Custom Reward", reward[0], reward[1], player, player);
            }
        }
    }

    public static boolean checkItem(L2ItemInstance item)
    {
        int o = item.getItem().getCrystalType();
        int e = item.getEnchantLevel();

        if ((enchant3 != 0) && (e >= enchant3))
        {
            return false;
        }

        if (grades3.contains(gradeNames3[o]))
        {
            return false;
        }

        if ((items3 != null) && items3.contains("" + item.getItemId()))
        {
            return false;
        }
        return true;
    }

    private static void loadConfigs()
    {
        try
        {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File("./config/MultiFunctionZone/MultiFunctionZone3.properties")));
            pvp_enabled3 = Boolean.parseBoolean(prop.getProperty("EnablePvP3", "False"));
            spawn_loc3 = parseItemsList(prop.getProperty("SpawnLoc3", "150111,144740,-12248"));
            revive_delay3 = Integer.parseInt(prop.getProperty("ReviveDelay3", "10"));
            if (revive_delay3 != 0)
            {
                revive3 = true;
            }
            give_noblesse3 = Boolean.parseBoolean(prop.getProperty("GiveNoblesse3", "False"));
            String[] propertySplit = prop.getProperty("Items3", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    items3.add(i);
                }
            }
            propertySplit = prop.getProperty("Grades3", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    if (i.equals("D") || i.equals("C") || i.equals("B") || i.equals("A") || i.equals("S") || i.equals("S80") || i.equals("S84"))
                    {
                        grades3.add(i);
                    }
                }
            }
            propertySplit = prop.getProperty("Classes3", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    classes3.add(i);
                }
            }
            radius3 = Integer.parseInt(prop.getProperty("RespawnRadius3", "500"));
            enchant3 = Integer.parseInt(prop.getProperty("Enchant3", "0"));
            remove_buffs3 = Boolean.parseBoolean(prop.getProperty("RemoveBuffs3", "False"));
            remove_pets3 = Boolean.parseBoolean(prop.getProperty("RemovePets3", "False"));
            restart_zone3 = Boolean.parseBoolean(prop.getProperty("NoRestartZone3", "False"));
            store_zone3 = Boolean.parseBoolean(prop.getProperty("NoStoreZone3", "False"));
            logout_zone3 = Boolean.parseBoolean(prop.getProperty("NoLogoutZone3", "False"));
            revive_noblesse3 = Boolean.parseBoolean(prop.getProperty("ReviveNoblesse3", "False"));
            revive_heal3 = Boolean.parseBoolean(prop.getProperty("ReviveHeal3", "False"));
            rewards3 = new ArrayList<>();
            propertySplit = prop.getProperty("Rewards3", "57,100000").split(";");
            for (String reward : propertySplit)
            {
                String[] rewardSplit = reward.split(",");
                if (rewardSplit.length == 2)
                {
                    try
                    {
                        rewards3.add(new int[]
                        {
                            Integer.parseInt(rewardSplit[0]),
                            Integer.parseInt(rewardSplit[1])
                        });
                    }
                    catch (NumberFormatException nfe)
                    {
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static int[][] parseItemsList(String line)
    {
        final String[] propertySplit = line.split(";");
        if (propertySplit.length == 0)
        {
            return null;
        }

        int i = 0;
        String[] valueSplit;
        final int[][] result = new int[propertySplit.length][];
        for (String value : propertySplit)
        {
            valueSplit = value.split(",");
            if (valueSplit.length != 3)
            {
                return null;
            }

            result[i] = new int[3];
            try
            {
                result[i][0] = Integer.parseInt(valueSplit[0]);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
            try
            {
                result[i][1] = Integer.parseInt(valueSplit[1]);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
            try
            {
                result[i][2] = Integer.parseInt(valueSplit[2]);
            }
            catch (NumberFormatException e)
            {
                return null;
            }
            i++;
        }
        return result;
    }
}