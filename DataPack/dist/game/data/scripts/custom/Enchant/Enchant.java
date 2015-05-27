package custom.Enchant;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import pk.elfo.gameserver.model.Elementals;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.Inventory;
import pk.elfo.gameserver.model.items.instance.L2ItemInstance;
import pk.elfo.gameserver.model.olympiad.OlympiadManager;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.serverpackets.CharInfo;
import pk.elfo.gameserver.network.serverpackets.ExBrExtraUserInfo;
import pk.elfo.gameserver.network.serverpackets.InventoryUpdate;
import pk.elfo.gameserver.network.serverpackets.UserInfo;

/**
*Pk Elfo
*/

public class Enchant extends Quest
{
    public static final Logger _log = Logger.getLogger(Enchant.class.getName());

    private final static int npcId = 36611;
    // Item required to enchant armor +1
    private final static int itemRequiredArmor = 3470; // This id is the Event - Glittering Medals item. You can put 57 for Adena.
    private final static int itemRequiredArmorCount = 2;
    // Item required to enchant jewels +1
    private final static int itemRequiredJewels = 3470;
    private final static int itemRequiredJewelsCount = 2;
    // Item required to enchant weapon +1
    private final static int itemRequiredWeapon = 3470;
    private final static int itemRequiredWeaponCount = 2;
    // Item required to enchant belt/shirt +1
    private final static int itemRequiredBeltShirt = 3470;
    private final static int itemRequiredBeltShirtCount = 1;
    // Item required to add Lv 7 attribute to weapon
    private final static int itemRequiredAttrWeapon = 3470;
    private final static int itemRequiredAttrWeaponCount = 2;
    
    public Enchant(int questId, String name, String descr)
    {
        super(questId, name, descr);
       
        addStartNpc(npcId);
        addFirstTalkId(npcId);
        addTalkId(npcId);
    }
    
    public static void main(String[] args)
    {
        new Enchant(-1, "Enchant", "custom/Enchant");
    }
    
    public String onFirstTalk(L2Npc npc, L2PcInstance player)
    {
        String enchantType = "html/Index.htm";

        if (player.getQuestState(getName()) == null)
        {
            newQuestState(player);
        }
        else if (player.isInCombat())
        {
            return drawHtml("Voce esta em combate "," Nao lute, se voce quer falar comigo!", enchantType);
        }
        else if (player.getPvpFlag() == 1)
        {
            return drawHtml("Voce esta sinalizado "," Nao lute, se voce quer falar comigo!", enchantType);
        }
        else if (player.getKarma() != 0)
        {
            return drawHtml("Voce esta em estado caótico "," Nao lute, se voce quer falar comigo!", enchantType);
        }
        else if (OlympiadManager.getInstance().isRegistered(player))
        {
            return drawHtml("Esta registrado para Olimpiada "," Voce nao pode usar os meus serviços <br1> ao jogar as Olimpiadas.", enchantType);
        }
        return enchantType;
    }

    public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
    {
        String htmlText = event;

        String enchantType = "Index.htm";

        int armorType = -1;

        // If it contains space, then it's an attribute enchant. After space it's the element name. Eg: Fire
        if (event.contains(" "))
        {
        	final StringTokenizer st = new StringTokenizer(event);
    		
			if (st.nextToken().equalsIgnoreCase("attributeWeapon"))
			{
	        	armorType = Inventory.PAPERDOLL_RHAND;
	            enchantType = "Attributes.htm";
	            
	            final String element = st.nextToken();

	            htmlText = enchant(element, enchantType, player, armorType, itemRequiredAttrWeapon, itemRequiredAttrWeaponCount);
			}
        }
        // Armor parts
        else if (event.equals("enchantHelmet"))
        {
            armorType = Inventory.PAPERDOLL_HEAD;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantChest"))
        {
            armorType = Inventory.PAPERDOLL_CHEST;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantLeggings"))
        {
            armorType = Inventory.PAPERDOLL_LEGS;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantGloves"))
        {
            armorType = Inventory.PAPERDOLL_GLOVES;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantBoots"))
        {
            armorType = Inventory.PAPERDOLL_FEET;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        else if (event.equals("enchantShieldOrSigil"))
        {
            armorType = Inventory.PAPERDOLL_LHAND;
            enchantType = "EnchantArmor.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredArmor, itemRequiredArmorCount);
        }
        // Jewels
        else if (event.equals("enchantUpperEarring"))
        {
            armorType = Inventory.PAPERDOLL_LEAR;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantLowerEarring"))
        {
            armorType = Inventory.PAPERDOLL_REAR;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantNecklace"))
        {
            armorType = Inventory.PAPERDOLL_NECK;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantUpperRing"))
        {
            armorType = Inventory.PAPERDOLL_LFINGER;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        else if (event.equals("enchantLowerRing"))
        {
            armorType = Inventory.PAPERDOLL_RFINGER;
            enchantType = "EnchantJewels.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredJewels, itemRequiredJewelsCount);
        }
        // Belt/Shirt
        else if (event.equals("enchantBelt"))
        {
            armorType = Inventory.PAPERDOLL_BELT;
            enchantType = "EnchantBeltShirt.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredBeltShirt, itemRequiredBeltShirtCount);
        }
        else if (event.equals("enchantShirt"))
        {
            armorType = Inventory.PAPERDOLL_UNDER;
            enchantType = "EnchantBeltShirt.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredBeltShirt, itemRequiredBeltShirtCount);
        }
        // Weapon
        else if (event.equals("enchantWeapon"))
        {
            armorType = Inventory.PAPERDOLL_RHAND;
            enchantType = "EnchantWeapon.htm";
            
            htmlText = enchant("enchant", enchantType, player, armorType, itemRequiredWeapon, itemRequiredWeaponCount);
        }
        else 
        {
        	htmlText = "html/" + htmlText;
        }
        return htmlText;
    }

    private String enchant(String type, String enchantType, L2PcInstance player, int armorType, int itemRequired, int itemRequiredCount)
    {
        QuestState qs = player.getQuestState(getName());

        // See if player has the required item and the correct amount of it
        if (qs.getQuestItemsCount(itemRequired) >= itemRequiredCount)
        {
        	// Try to enchant
            try
            {
            	// Get the item to enchant
                L2ItemInstance item = getItemToEnchant(player, armorType);

                // If valid item
                if (item != null)
                {
                	// Check if item is valid
                    if ( item.isItem() && item.isEquipable() && !item.isConsumable() && !item.isCommonItem() && !item.isOlyRestrictedItem() && !item.isShadowItem() && !item.isQuestItem() )
                    {
                    	// If we want to enchant
                    	if (type == "enchant")
                    	{
	                    	final int currentEnchant = item.getEnchantLevel();
	
	                        if ( currentEnchant < 30 )
	                        {
	                            int newEnchantLevel = setEnchant(player, item, currentEnchant+1, armorType);
	
	                            if ( newEnchantLevel > 0 )
	                            {
	                                qs.takeItems(itemRequired, itemRequiredCount);
	                                player.sendMessage("You successfully enchanted your " + item.getItem().getName() +" from +" + currentEnchant + " to +" + newEnchantLevel + "!");
	
	                                String htmlContent = "<center>You successfully enchanted your:<br>"+ "<font color=\"FF7200\">" + item.getItem().getName() + "</font><br>"+ "From: <font color=\"AEFF00\">+" + currentEnchant + "</font> to <font color=\"AEFF00\">+" + newEnchantLevel + "</font>"+ "</center>";
	
	                                return drawHtml("Congratulations!", htmlContent, enchantType);
	                            }
	                        }
	                        else
	                        {
	                            player.sendMessage("Your " + item.getItem().getName() + " is already +20!");
	                            return drawHtml("It's already +30", "<center>Your <font color=\"FF7200\">" + item.getItem().getName() +"</font> is already +30!</center>", enchantType);
	                        }
                    	}
                    	// If we want to add attribute
                    	else
                    	{
                    		qs.takeItems(itemRequired, itemRequiredCount);
                    		
                    		setElement(player, item, type, armorType);
                    		
                    		player.sendMessage("You successfully added " + type + " element Lv 7 to your " + item.getItem().getName() +"!");
                    		
                            String htmlContent = "<center>You added <font color=\"AEFF00\">" + type + "</font> element Lv 7 to your:<br>"+ "<font color=\"FF7200\">" + item.getItem().getName() + "</font>"+ "</center>";

                            return drawHtml("Congratulations!", htmlContent, enchantType);
                    	}
                    }
                    // If item can not be enchanted
                    else
                    {
                        player.sendMessage("Your " + item.getItem().getName() + " is not enchantable!");
                        return drawHtml("Not enchantable item!", "<center>Your <font color=\"FF7200\">" + item.getItem().getName() +"</font> is not enchantable!</center>", enchantType);
                    }
                }
            }
            catch (StringIndexOutOfBoundsException e)
            {
                player.sendMessage("Something went wrong. Are equiped with the item?");
                return drawHtml("Error Enchant", "<center>Something went wrong.<br>Are equiped with the item?</center>", enchantType);
            }
            catch (NumberFormatException e)
            {
                player.sendMessage("Something went wrong. Are equiped with the item?");
                return drawHtml("Error Enchant", "<center>Something went wrong.<br>Are equiped with the item?</center>", enchantType);
            }

            player.sendMessage("Something went wrong. Are equiped with the item?");
            return drawHtml("Error Enchant", "<center>Something went wrong.<br>Are equiped with the item?</center>", enchantType);
        }
        // If player doesn't have correct amount of required item, tell him what he needs
        else
        {
            String content = "<center>"+ "Not enough <font color=\"FF7200\">Event - Glitering Medals</font>!<br>";

                    if ( qs.getQuestItemsCount(itemRequired) > 0 )
                    {
                        content += "You have " + qs.getQuestItemsCount(itemRequired) + " Glittering Medals,<br1>"+ "Need " + (itemRequiredCount - qs.getQuestItemsCount(itemRequired)) + " more.";
                    }
                    else
                    {
                        content += "You need <font color=\"FF7200\">" + itemRequiredCount + " Event - Glitering Medals</font>!";
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
    
    private void setElement(L2PcInstance player, L2ItemInstance item, String type, int armorType)
    {
        if (item != null)
        {
            // set enchant value
            player.getInventory().unEquipItemInSlot(armorType);
            
            // New attr
            byte attr = Elementals.getElementId(type);

			byte clearAttr = -1;
			item.clearElementAttr(clearAttr);

			item.setElementAttr(attr, 300);
            
            player.getInventory().equipItem(item);
            
            // send packets
            InventoryUpdate iu = new InventoryUpdate();
			iu.addModifiedItem(item);
			player.sendPacket(iu);
        }
    }

    public String drawHtml(String title, String content, String enchantType)
    {
        String html = "<html>"+ "<title>L2Mondial Enchanter</title>"+ "<body>"+ "<center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>"+ "<font color=\"FF9900\">" + title + "</font></center><br>"+ content + "<br><br>"+ "<center><a action=\"bypass -h Quest Enchant " + enchantType + "\">Go Back</a></center>"+ "</body>"+ "</html>";
        return html;
    }
}