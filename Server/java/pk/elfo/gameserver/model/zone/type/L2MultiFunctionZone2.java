package pk.elfo.gameserver.model.zone.type;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javolution.util.FastList;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.datatables.SkillTable;
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

public class L2MultiFunctionZone2 extends L2RespawnZone
{

    public L2MultiFunctionZone2(int id)
    {
        super(id);
        loadConfigs();
    }

    public static boolean pvp_enabled2, restart_zone2, store_zone2, logout_zone2, revive_noblesse2, revive_heal2, revive2, remove_buffs2, remove_pets2, give_noblesse2;
    static int radius2, enchant2, revive_delay2;
    static int[][] spawn_loc2;
    L2Skill noblesse2 = SkillTable.getInstance().getInfo(1323, 1);
    private static List<String> items2 = new FastList<>();
    private static List<String> grades2 = new FastList<>(), classes2 = new FastList<>();
    public static List<int[]> rewards2;
    static String[] gradeNames2 =
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
        character.setInsideZone(ZoneId.MULTI_FUNCTION2, true);
        if (!store_zone2)
        {
            character.setInsideZone(ZoneId.NO_STORE, true);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if ((classes2 != null) && classes2.contains("" + activeChar.getClassId().getId()))
            {
                activeChar.teleToLocation(83597, 147888, -3405);
                activeChar.sendMessage("Your class is not allowed in the MultiFunction zone.");
                return;
            }

            for (L2ItemInstance o : activeChar.getInventory()._items)
            {
                if (o.isEquipable() && o.isEquipped() && !checkItem(o))
                {
                    int slot = activeChar.getInventory().getSlotFromItem(o);
                    activeChar.getInventory().unEquipItemInBodySlot(slot);
                    activeChar.sendMessage(o.getName() + " unequiped because is not allowed inside this zone.");
                }
            }
            activeChar.sendMessage("You entered in a MultiFunction zone.");
            clear(activeChar);
            if (give_noblesse2)
            {
                noblesse2.getEffects(activeChar, activeChar);
            }
            if (pvp_enabled2 && (activeChar.getPvpFlag() == 0))
            {
                activeChar.updatePvPFlag(1);
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.MULTI_FUNCTION2, false);
        if (!store_zone2)
        {
            character.setInsideZone(ZoneId.NO_STORE, false);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            activeChar.sendMessage("You left from a MultiFunction zone.");

            if (pvp_enabled2)
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
            if (revive2)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeChar.doRevive();
                        heal(activeChar);
                        int[] loc = spawn_loc2[Rnd.get(spawn_loc2.length)];
                        activeChar.teleToLocation(loc[0] + Rnd.get(-radius2, radius2), loc[1] + Rnd.get(-radius2, radius2), loc[2]);
                    }
                }, revive_delay2 * 1000);
            }
        }
    }

    @Override
    public void onReviveInside(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive_noblesse2)
            {
                noblesse2.getEffects(activeChar, activeChar);
            }
            if (revive_heal2)
            {
                heal(activeChar);
            }
        }
    }

    private void clear(L2PcInstance player)
    {
        if (remove_buffs2)
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            if (remove_pets2)
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
            if (remove_pets2)
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
        if (player.isInsideZone(ZoneId.MULTI_FUNCTION2))
        {
            for (int[] reward : rewards2)
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

        if ((enchant2 != 0) && (e >= enchant2))
        {
            return false;
        }

        if (grades2.contains(gradeNames2[o]))
        {
            return false;
        }

        if ((items2 != null) && items2.contains("" + item.getItemId()))
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
            prop.load(new FileInputStream(new File("./config/MultiFunctionZone/MultiFunctionZone2.properties")));
            pvp_enabled2 = Boolean.parseBoolean(prop.getProperty("EnablePvP2", "False"));
            spawn_loc2 = parseItemsList(prop.getProperty("SpawnLoc2", "150111,144740,-12248"));
            revive_delay2 = Integer.parseInt(prop.getProperty("ReviveDelay2", "10"));
            if (revive_delay2 != 0)
            {
                revive2 = true;
            }
            give_noblesse2 = Boolean.parseBoolean(prop.getProperty("GiveNoblesse2", "False"));
            String[] propertySplit = prop.getProperty("Items2", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    items2.add(i);
                }
            }
            propertySplit = prop.getProperty("Grades2", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    if (i.equals("D") || i.equals("C") || i.equals("B") || i.equals("A") || i.equals("S") || i.equals("S80") || i.equals("S84"))
                    {
                        grades2.add(i);
                    }
                }
            }
            propertySplit = prop.getProperty("Classes2", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    classes2.add(i);
                }
            }
            radius2 = Integer.parseInt(prop.getProperty("RespawnRadius2", "500"));
            enchant2 = Integer.parseInt(prop.getProperty("Enchant2", "0"));
            remove_buffs2 = Boolean.parseBoolean(prop.getProperty("RemoveBuffs2", "False"));
            remove_pets2 = Boolean.parseBoolean(prop.getProperty("RemovePets2", "False"));
            restart_zone2 = Boolean.parseBoolean(prop.getProperty("NoRestartZone2", "False"));
            store_zone2 = Boolean.parseBoolean(prop.getProperty("NoStoreZone2", "False"));
            logout_zone2 = Boolean.parseBoolean(prop.getProperty("NoLogoutZone2", "False"));
            revive_noblesse2 = Boolean.parseBoolean(prop.getProperty("ReviveNoblesse2", "False"));
            revive_heal2 = Boolean.parseBoolean(prop.getProperty("ReviveHeal2", "False"));
            rewards2 = new ArrayList<>();
            propertySplit = prop.getProperty("Rewards2", "57,100000").split(";");
            for (String reward : propertySplit)
            {
                String[] rewardSplit = reward.split(",");
                if (rewardSplit.length == 2)
                {
                    try
                    {
                        rewards2.add(new int[]
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