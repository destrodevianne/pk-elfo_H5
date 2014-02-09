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

public class L2MultiFunctionZone1 extends L2RespawnZone
{

    public L2MultiFunctionZone1(int id)
    {
        super(id);
        loadConfigs();
    }

    public static boolean pvp_enabled1, restart_zone1, store_zone1, logout_zone1, revive_noblesse1, revive_heal1, revive1, remove_buffs1, remove_pets1, give_noblesse1;
    static int radius1, enchant1, revive_delay1;
    static int[][] spawn_loc1;
    L2Skill noblesse1 = SkillTable.getInstance().getInfo(1323, 1);
    private static List<String> items1 = new FastList<>();
    private static List<String> grades1 = new FastList<>(), classes1 = new FastList<>();
    public static List<int[]> rewards1;
    static String[] gradeNames1 =
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
        character.setInsideZone(ZoneId.MULTI_FUNCTION1, true);
        if (!store_zone1)
        {
            character.setInsideZone(ZoneId.NO_STORE, true);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if ((classes1 != null) && classes1.contains("" + activeChar.getClassId().getId()))
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
            if (give_noblesse1)
            {
                noblesse1.getEffects(activeChar, activeChar);
            }
            if (pvp_enabled1 && (activeChar.getPvpFlag() == 0))
            {
                activeChar.updatePvPFlag(1);
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.MULTI_FUNCTION1, false);
        if (!store_zone1)
        {
            character.setInsideZone(ZoneId.NO_STORE, false);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            activeChar.sendMessage("You left from a MultiFunction zone.");

            if (pvp_enabled1)
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
            if (revive1)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeChar.doRevive();
                        heal(activeChar);
                        int[] loc = spawn_loc1[Rnd.get(spawn_loc1.length)];
                        activeChar.teleToLocation(loc[0] + Rnd.get(-radius1, radius1), loc[1] + Rnd.get(-radius1, radius1), loc[2]);
                    }
                }, revive_delay1 * 1000);
            }
        }
    }

    @Override
    public void onReviveInside(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive_noblesse1)
            {
                noblesse1.getEffects(activeChar, activeChar);
            }
            if (revive_heal1)
            {
                heal(activeChar);
            }
        }
    }

    private void clear(L2PcInstance player)
    {
        if (remove_buffs1)
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            if (remove_pets1)
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
            if (remove_pets1)
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
        if (player.isInsideZone(ZoneId.MULTI_FUNCTION1))
        {
            for (int[] reward : rewards1)
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

        if ((enchant1 != 0) && (e >= enchant1))
        {
            return false;
        }

        if (grades1.contains(gradeNames1[o]))
        {
            return false;
        }

        if ((items1 != null) && items1.contains("" + item.getItemId()))
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
            prop.load(new FileInputStream(new File("./config/MultiFunctionZone/MultiFunctionZone1.properties")));
            pvp_enabled1 = Boolean.parseBoolean(prop.getProperty("EnablePvP1", "False"));
            spawn_loc1 = parseItemsList(prop.getProperty("SpawnLoc1", "150111,144740,-12248"));
            revive_delay1 = Integer.parseInt(prop.getProperty("ReviveDelay1", "10"));
            if (revive_delay1 != 0)
            {
                revive1 = true;
            }
            give_noblesse1 = Boolean.parseBoolean(prop.getProperty("GiveNoblesse1", "False"));
            String[] propertySplit = prop.getProperty("Items1", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    items1.add(i);
                }
            }
            propertySplit = prop.getProperty("Grades1", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    if (i.equals("D") || i.equals("C") || i.equals("B") || i.equals("A") || i.equals("S") || i.equals("S80") || i.equals("S84"))
                    {
                        grades1.add(i);
                    }
                }
            }
            propertySplit = prop.getProperty("Classes1", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    classes1.add(i);
                }
            }
            radius1 = Integer.parseInt(prop.getProperty("RespawnRadius1", "500"));
            enchant1 = Integer.parseInt(prop.getProperty("Enchant1", "0"));
            remove_buffs1 = Boolean.parseBoolean(prop.getProperty("RemoveBuffs1", "False"));
            remove_pets1 = Boolean.parseBoolean(prop.getProperty("RemovePets1", "False"));
            restart_zone1 = Boolean.parseBoolean(prop.getProperty("NoRestartZone1", "False"));
            store_zone1 = Boolean.parseBoolean(prop.getProperty("NoStoreZone1", "False"));
            logout_zone1 = Boolean.parseBoolean(prop.getProperty("NoLogoutZone1", "False"));
            revive_noblesse1 = Boolean.parseBoolean(prop.getProperty("ReviveNoblesse1", "False"));
            revive_heal1 = Boolean.parseBoolean(prop.getProperty("ReviveHeal1", "False"));
            rewards1 = new ArrayList<>();
            propertySplit = prop.getProperty("Rewards1", "57,100000").split(";");
            for (String reward : propertySplit)
            {
                String[] rewardSplit = reward.split(",");
                if (rewardSplit.length == 2)
                {
                    try
                    {
                        rewards1.add(new int[]
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