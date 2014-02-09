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

public class L2MultiFunctionZone4 extends L2RespawnZone
{

    public L2MultiFunctionZone4(int id)
    {
        super(id);
        loadConfigs();
    }

    public static boolean pvp_enabled4, restart_zone4, store_zone4, logout_zone4, revive_noblesse4, revive_heal4, revive4, remove_buffs4, remove_pets4, give_noblesse4;
    static int radius4, enchant4, revive_delay4;
    static int[][] spawn_loc4;
    L2Skill noblesse4 = SkillTable.getInstance().getInfo(1323, 1);
    private static List<String> items4 = new FastList<>();
    private static List<String> grades4 = new FastList<>(), classes4 = new FastList<>();
    public static List<int[]> rewards4;
    static String[] gradeNames4 =
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
        character.setInsideZone(ZoneId.MULTI_FUNCTION4, true);
        if (!store_zone4)
        {
            character.setInsideZone(ZoneId.NO_STORE, true);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if ((classes4 != null) && classes4.contains("" + activeChar.getClassId().getId()))
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
            if (give_noblesse4)
            {
                noblesse4.getEffects(activeChar, activeChar);
            }
            if (pvp_enabled4 && (activeChar.getPvpFlag() == 0))
            {
                activeChar.updatePvPFlag(1);
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.MULTI_FUNCTION4, false);
        if (!store_zone4)
        {
            character.setInsideZone(ZoneId.NO_STORE, false);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            activeChar.sendMessage("You left from a MultiFunction zone.");

            if (pvp_enabled4)
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
            if (revive4)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeChar.doRevive();
                        heal(activeChar);
                        int[] loc = spawn_loc4[Rnd.get(spawn_loc4.length)];
                        activeChar.teleToLocation(loc[0] + Rnd.get(-radius4, radius4), loc[1] + Rnd.get(-radius4, radius4), loc[2]);
                    }
                }, revive_delay4 * 1000);
            }
        }
    }

    @Override
    public void onReviveInside(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive_noblesse4)
            {
                noblesse4.getEffects(activeChar, activeChar);
            }
            if (revive_heal4)
            {
                heal(activeChar);
            }
        }
    }

    private void clear(L2PcInstance player)
    {
        if (remove_buffs4)
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            if (remove_pets4)
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
            if (remove_pets4)
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
        if (player.isInsideZone(ZoneId.MULTI_FUNCTION4))
        {
            for (int[] reward : rewards4)
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

        if ((enchant4 != 0) && (e >= enchant4))
        {
            return false;
        }

        if (grades4.contains(gradeNames4[o]))
        {
            return false;
        }

        if ((items4 != null) && items4.contains("" + item.getItemId()))
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
            prop.load(new FileInputStream(new File("./config/MultiFunctionZone/MultiFunctionZone4.properties")));
            pvp_enabled4 = Boolean.parseBoolean(prop.getProperty("EnablePvP4", "False"));
            spawn_loc4 = parseItemsList(prop.getProperty("SpawnLoc4", "150111,144740,-12248"));
            revive_delay4 = Integer.parseInt(prop.getProperty("ReviveDelay4", "10"));
            if (revive_delay4 != 0)
            {
                revive4 = true;
            }
            give_noblesse4 = Boolean.parseBoolean(prop.getProperty("GiveNoblesse4", "False"));
            String[] propertySplit = prop.getProperty("Items4", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    items4.add(i);
                }
            }
            propertySplit = prop.getProperty("Grades4", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    if (i.equals("D") || i.equals("C") || i.equals("B") || i.equals("A") || i.equals("S") || i.equals("S80") || i.equals("S84"))
                    {
                        grades4.add(i);
                    }
                }
            }
            propertySplit = prop.getProperty("Classes4", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    classes4.add(i);
                }
            }
            radius4 = Integer.parseInt(prop.getProperty("RespawnRadius4", "500"));
            enchant4 = Integer.parseInt(prop.getProperty("Enchant4", "0"));
            remove_buffs4 = Boolean.parseBoolean(prop.getProperty("RemoveBuffs4", "False"));
            remove_pets4 = Boolean.parseBoolean(prop.getProperty("RemovePets4", "False"));
            restart_zone4 = Boolean.parseBoolean(prop.getProperty("NoRestartZone4", "False"));
            store_zone4 = Boolean.parseBoolean(prop.getProperty("NoStoreZone4", "False"));
            logout_zone4 = Boolean.parseBoolean(prop.getProperty("NoLogoutZone4", "False"));
            revive_noblesse4 = Boolean.parseBoolean(prop.getProperty("ReviveNoblesse4", "False"));
            revive_heal4 = Boolean.parseBoolean(prop.getProperty("ReviveHeal4", "False"));
            rewards4 = new ArrayList<>();
            propertySplit = prop.getProperty("Rewards4", "57,100000").split(";");
            for (String reward : propertySplit)
            {
                String[] rewardSplit = reward.split(",");
                if (rewardSplit.length == 2)
                {
                    try
                    {
                        rewards4.add(new int[]
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