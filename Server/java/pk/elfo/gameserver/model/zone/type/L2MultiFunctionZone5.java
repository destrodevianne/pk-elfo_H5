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

public class L2MultiFunctionZone5 extends L2RespawnZone
{

    public L2MultiFunctionZone5(int id)
    {
        super(id);
        loadConfigs();
    }

    public static boolean pvp_enabled5, restart_zone5, store_zone5, logout_zone5, revive_noblesse5, revive_heal5, revive5, remove_buffs5, remove_pets5, give_noblesse5;
    static int radius5, enchant5, revive_delay5;
    static int[][] spawn_loc5;
    L2Skill noblesse5 = SkillTable.getInstance().getInfo(1323, 1);
    private static List<String> items5 = new FastList<>();
    private static List<String> grades5 = new FastList<>(), classes5 = new FastList<>();
    public static List<int[]> rewards5;
    static String[] gradeNames5 =
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
        character.setInsideZone(ZoneId.MULTI_FUNCTION5, true);
        if (!store_zone5)
        {
            character.setInsideZone(ZoneId.NO_STORE, true);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if ((classes5 != null) && classes5.contains("" + activeChar.getClassId().getId()))
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
            if (give_noblesse5)
            {
                noblesse5.getEffects(activeChar, activeChar);
            }
            if (pvp_enabled5 && (activeChar.getPvpFlag() == 0))
            {
                activeChar.updatePvPFlag(1);
            }
        }
    }

    @Override
    protected void onExit(L2Character character)
    {
        character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
        character.setInsideZone(ZoneId.MULTI_FUNCTION5, false);
        if (!store_zone5)
        {
            character.setInsideZone(ZoneId.NO_STORE, false);
        }

        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            activeChar.sendMessage("You left from a MultiFunction zone.");

            if (pvp_enabled5)
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
            if (revive5)
            {
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        activeChar.doRevive();
                        heal(activeChar);
                        int[] loc = spawn_loc5[Rnd.get(spawn_loc5.length)];
                        activeChar.teleToLocation(loc[0] + Rnd.get(-radius5, radius5), loc[1] + Rnd.get(-radius5, radius5), loc[2]);
                    }
                }, revive_delay5 * 1000);
            }
        }
    }

    @Override
    public void onReviveInside(L2Character character)
    {
        if (character instanceof L2PcInstance)
        {
            L2PcInstance activeChar = ((L2PcInstance) character);
            if (revive_noblesse5)
            {
                noblesse5.getEffects(activeChar, activeChar);
            }
            if (revive_heal5)
            {
                heal(activeChar);
            }
        }
    }

    private void clear(L2PcInstance player)
    {
        if (remove_buffs5)
        {
            player.stopAllEffectsExceptThoseThatLastThroughDeath();
            if (remove_pets5)
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
            if (remove_pets5)
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
        if (player.isInsideZone(ZoneId.MULTI_FUNCTION5))
        {
            for (int[] reward : rewards5)
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

        if ((enchant5 != 0) && (e >= enchant5))
        {
            return false;
        }

        if (grades5.contains(gradeNames5[o]))
        {
            return false;
        }

        if ((items5 != null) && items5.contains("" + item.getItemId()))
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
            prop.load(new FileInputStream(new File("./config/MultiFunctionZone/MultiFunctionZone5.properties")));
            pvp_enabled5 = Boolean.parseBoolean(prop.getProperty("EnablePvP5", "False"));
            spawn_loc5 = parseItemsList(prop.getProperty("SpawnLoc5", "150111,144740,-12248"));
            revive_delay5 = Integer.parseInt(prop.getProperty("ReviveDelay5", "10"));
            if (revive_delay5 != 0)
            {
                revive5 = true;
            }
            give_noblesse5 = Boolean.parseBoolean(prop.getProperty("GiveNoblesse5", "False"));
            String[] propertySplit = prop.getProperty("Items5", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    items5.add(i);
                }
            }
            propertySplit = prop.getProperty("Grades5", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    if (i.equals("D") || i.equals("C") || i.equals("B") || i.equals("A") || i.equals("S") || i.equals("S80") || i.equals("S84"))
                    {
                        grades5.add(i);
                    }
                }
            }
            propertySplit = prop.getProperty("Classes5", "").split(",");
            if (propertySplit.length != 0)
            {
                for (String i : propertySplit)
                {
                    classes5.add(i);
                }
            }
            radius5 = Integer.parseInt(prop.getProperty("RespawnRadius5", "500"));
            enchant5 = Integer.parseInt(prop.getProperty("Enchant5", "0"));
            remove_buffs5 = Boolean.parseBoolean(prop.getProperty("RemoveBuffs5", "False"));
            remove_pets5 = Boolean.parseBoolean(prop.getProperty("RemovePets5", "False"));
            restart_zone5 = Boolean.parseBoolean(prop.getProperty("NoRestartZone5", "False"));
            store_zone5 = Boolean.parseBoolean(prop.getProperty("NoStoreZone5", "False"));
            logout_zone5 = Boolean.parseBoolean(prop.getProperty("NoLogoutZone5", "False"));
            revive_noblesse5 = Boolean.parseBoolean(prop.getProperty("ReviveNoblesse5", "False"));
            revive_heal5 = Boolean.parseBoolean(prop.getProperty("ReviveHeal5", "False"));
            rewards5 = new ArrayList<>();
            propertySplit = prop.getProperty("Rewards5", "57,100000").split(";");
            for (String reward : propertySplit)
            {
                String[] rewardSplit = reward.split(",");
                if (rewardSplit.length == 2)
                {
                    try
                    {
                        rewards5.add(new int[]
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