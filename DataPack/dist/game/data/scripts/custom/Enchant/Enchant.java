package custom.Enchant;

import java.util.logging.Logger;

import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.model.itemcontainer.Inventory;
import king.server.gameserver.model.items.instance.L2ItemInstance;
import king.server.gameserver.model.olympiad.OlympiadManager;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.model.quest.QuestState;
import king.server.gameserver.network.serverpackets.CharInfo;
import king.server.gameserver.network.serverpackets.ExBrExtraUserInfo;
import king.server.gameserver.network.serverpackets.InventoryUpdate;
import king.server.gameserver.network.serverpackets.UserInfo;

public class Enchant extends Quest
{
    public static final Logger _log = Logger.getLogger(Enchant.class.getName());

    int npcId = 36609;

    // Item required to enchant armor +1
    int itemRequiredArmor = 3470;
    int itemRequiredArmorCount = 2;

    // Item required to enchant jewels +1
    int itemRequiredJewels = 3470;
    int itemRequiredJewelsCount = 2;

    // Item required to enchant weapon +1
    int itemRequiredWeapon = 3470;
    int itemRequiredWeaponCount = 2;

    // Item required to enchant belt/shirt +1
    int itemRequiredBeltShirt = 3470;
    int itemRequiredBeltShirtCount = 1;

    public Enchant(int questId, String name, String descr)
    {
        super(questId, name, descr);
       
        addStartNpc(npcId);
        addFirstTalkId(npcId);
        addTalkId(npcId);
    }
    
    public static void main(String[] args)
    {
        new Enchant(-1, Enchant.class.getSimpleName(), "custom");
    }
    
    public String onFirstTalk(L2Npc npc, L2PcInstance player)
    {
        String enchantType = "Enchant.htm";

        if (player.getQuestState(getName()) == null)
        {
            newQuestState(player);
        }
        else if (player.isInCombat())
        {
            return drawHtml("Voce esta em modo de combate", "Nao lute, se voce quiser falar comigo!", enchantType);
        }
        else if (player.getPvpFlag() == 1)
        {
            return drawHtml("Voce esta em PvP", "Nao lute, se voce quiser falar comigo!", enchantType);
        }
        else if (player.getKarma() != 0)
        {
            return drawHtml("Voce esta PK", "Nao lute, se voce quiser falar comigo!", enchantType);
        }
        else if (OlympiadManager.getInstance().isRegistered(player))
        {
            return drawHtml("Voce esta registrado para Olimpiada", "Voce nao pode usar os meus servicos <br1>estando participando das Olimpiadas.", enchantType);
        }

        return "Enchant.htm";
    }

    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        String htmlText = event;

        String enchantType = "Enchant.htm";

        int armorType = -1;

        // Armor parts
        if (event.equals("enchantHelmet"))
        {
            armorType = Inventory.PAPERDOLL_HEAD;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantChest"))
        {
            armorType = Inventory.PAPERDOLL_CHEST;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantLeggings"))
        {
            armorType = Inventory.PAPERDOLL_LEGS;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantGloves"))
        {
            armorType = Inventory.PAPERDOLL_GLOVES;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantBoots"))
        {
            armorType = Inventory.PAPERDOLL_FEET;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantShieldOrSigil"))
        {
            armorType = Inventory.PAPERDOLL_LHAND;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        // Jewels
        else if (event.equals("enchantUpperEarring"))
        {
            armorType = Inventory.PAPERDOLL_LEAR;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantLowerEarring"))
        {
            armorType = Inventory.PAPERDOLL_REAR;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantNecklace"))
        {
            armorType = Inventory.PAPERDOLL_NECK;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantUpperRing"))
        {
            armorType = Inventory.PAPERDOLL_LFINGER;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantLowerRing"))
        {
            armorType = Inventory.PAPERDOLL_RFINGER;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        // Belt/Shirt
        else if (event.equals("enchantBelt"))
        {
            armorType = Inventory.PAPERDOLL_BELT;
            enchantType = "EnchantBeltShirt.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredBeltShirt, itemRequiredBeltShirtCount);
        }
        else if (event.equals("enchantShirt"))
        {
            armorType = Inventory.PAPERDOLL_UNDER;
            enchantType = "EnchantBeltShirt.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredBeltShirt, itemRequiredBeltShirtCount);
        }
        // Weapon
        else if (event.equals("enchantWeapon"))
        {
            armorType = Inventory.PAPERDOLL_RHAND;
            enchantType = "EnchantWeapon.htm";
            
            htmlText = enchant(enchantType, player, armorType, itemRequiredWeapon, itemRequiredWeaponCount);
        }

        return htmlText;
    }

    private String enchant(String enchantType, L2PcInstance player, int armorType, int itemRequired, int itemRequiredCount)
    {
        QuestState st = player.getQuestState(getName());

        int currentEnchant = 0;
        int newEnchantLevel = 0;

        if (st.getQuestItemsCount(itemRequired) >= itemRequiredCount)
        {
            try
            {
                L2ItemInstance item = getItemToEnchant(player, armorType);

                if (item != null)
                {
                    currentEnchant = item.getEnchantLevel();

                    if ( currentEnchant < 30 )
                    {
                        newEnchantLevel = setEnchant(player, item, currentEnchant+1, armorType);

                        if ( newEnchantLevel > 0 )
                        {
                            st.takeItems(itemRequired, itemRequiredCount);
                            player.sendMessage("Voce encantou com sucesso o seu " + item.getItem().getName() +" dse +" + currentEnchant + " para +" + newEnchantLevel + "!");

                            String htmlContent = "<center>Voce encantou com sucesso o seu:<br>"+
                                                    "<font color=\"FF7200\">" + item.getItem().getName() + "</font><br>"+
                                                    "De: <font color=\"AEFF00\">+" + currentEnchant + "</font> para <font color=\"AEFF00\">+" + newEnchantLevel + "</font>"+
                                                "</center>";

                            return drawHtml("Congratulations!", htmlContent, enchantType);
                        }
                    }
                    else
                    {
                        player.sendMessage("Seu " + item.getItem().getName() + " ja esta +30!");
                        return drawHtml("Ja esta +30", "<center>Seu <font color=\"FF7200\">" + item.getItem().getName() +"</font> ja esta +30!</center>", enchantType);
                    }
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                player.sendMessage("Algo deu errado. Esta equipado com o item?");
                return drawHtml("Erro ao encantar", "<center>Algo deu errado.<br> Esta equipado com o item?</center>", enchantType);
            }
            catch (NumberFormatException e)
            {
                player.sendMessage("Algo deu errado. Esta equipado com o item?");
                return drawHtml("Erro ao encantar", "<center>Algo deu errado.<br> Esta equipado com o item?</center>", enchantType);
            }


            player.sendMessage("Algo deu errado. Esta equipado com o item?");
            return drawHtml("Erro ao encantar", "<center>Algo deu errado.<br> Esta equipado com o item?</center>", enchantType);
        }
        else
        {
            String content = "<center>"+
                                "Voce nao tem <font color=\"FF7200\">Event - Glitering Medals</font> suficiente!<br>";

                    if ( st.getQuestItemsCount(itemRequired) > 0 )
                    {
                        content += "Voce tem " + st.getQuestItemsCount(itemRequired) + " Glittering Medals,<br1>"+
                                    "necessario " + (itemRequiredCount - st.getQuestItemsCount(itemRequired)) + " mais.";
                    }
                    else
                    {
                        content += "Voce precisa de <font color=\"FF7200\">" + itemRequiredCount + " Event - Glitering Medals</font>!";
                    }

                    content += "</center>";

            return drawHtml("Not Enough Items", content, enchantType);
        }
    }

    private L2ItemInstance getItemToEnchant(L2PcInstance player, int armorType)
    {
        L2ItemInstance itemInstance = null;
        L2ItemInstance parmorInstance = player.getInventory().getPaperdollItem(armorType);

        if ((parmorInstance != null) && (parmorInstance.getLocationSlot() == armorType))
        {
            itemInstance = parmorInstance;

            if (itemInstance != null)
            {
                return itemInstance;
            }
        }

        return null;
    }

    private int setEnchant(L2PcInstance player, L2ItemInstance item, int newEnchantLevel, int armorType)
    {
        if (item != null)
        {
            // set enchant value
            player.getInventory().unEquipItemInSlot(armorType);
            item.setEnchantLevel(newEnchantLevel);
            player.getInventory().equipItem(item);
            
            // send packets
            InventoryUpdate iu = new InventoryUpdate();
            iu.addModifiedItem(item);
            player.sendPacket(iu);
            player.broadcastPacket(new CharInfo(player));
            player.sendPacket(new UserInfo(player));
            player.broadcastPacket(new ExBrExtraUserInfo(player));

            return newEnchantLevel;
        }

        return -1;
    }

    public String drawHtml(String title, String content, String enchantType)
    {
        String html = "<html>"+
                        "<title>L2 PkElfo - Encantamento</title>"+
                        "<body>"+
                            "<center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"+
                            "<font color=\"FF9900\">" + title + "</font></center><br>"+
                            content +
                            "<br><br>"+
                            "<center><a action=\"bypass -h Quest Enchant " + enchantType + "\">Voltar</a></center>"+
                        "</body>"+
                        "</html>";

        return html;
    }
}