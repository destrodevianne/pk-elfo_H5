package events.MasterOfEnchanting;

import java.util.Date;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.event.LongTimeEvent;
import pk.elfo.gameserver.model.itemcontainer.Inventory;
import pk.elfo.gameserver.model.itemcontainer.PcInventory;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * Master of Enchanting event AI.
 * @author Gnacik
 */
public class MasterOfEnchanting extends LongTimeEvent
{
	// NPC
	private static final int MASTER_YOGI = 32599;
	// Items
	private static final int MASTER_YOGI_STAFF = 13539;
	private static final int MASTER_YOGI_SCROLL = 13540;
	// Misc
	private static final int STAFF_PRICE = 1000000;
	private static final int SCROLL_24_PRICE = 96000000;
	private static final int SCROLL_24_TIME = 6;
	private static final int SCROLL_1_PRICE = 4000000;
	private static final int SCROLL_10_PRICE = 40000000;
	
	private static final int[] HAT_SHADOW_REWARD =
	{
		13074,
		13075,
		13076
	};
	private static final int[] HAT_EVENT_REWARD =
	{
		13518,
		13519,
		13522
	};
	private static final int[] CRYSTAL_REWARD =
	{
		9570,
		9571,
		9572
	};
	
	@SuppressWarnings("deprecation")
	private static final Date _eventStart = new Date(2013, 1, 1);
	
	public MasterOfEnchanting(String name, String descr)
	{
		super(name, descr);
		addStartNpc(MASTER_YOGI);
		addFirstTalkId(MASTER_YOGI);
		addTalkId(MASTER_YOGI);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (event.equalsIgnoreCase("buy_staff"))
		{
			if (!st.hasQuestItems(MASTER_YOGI_STAFF) && (st.getQuestItemsCount(PcInventory.ADENA_ID) > STAFF_PRICE))
			{
				st.takeItems(PcInventory.ADENA_ID, STAFF_PRICE);
				st.giveItems(MASTER_YOGI_STAFF, 1);
				htmltext = "32599-staffbuyed.htm";
			}
			else
			{
				htmltext = "32599-staffcant.htm";
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_24"))
		{
			long _curr_time = System.currentTimeMillis();
			String value = loadGlobalQuestVar(player.getAccountName());
			long _reuse_time = value == "" ? 0 : Long.parseLong(value);
			if (player.getCreateDate().after(_eventStart))
			{
				return "32599-bidth.htm";
			}
			
			if (_curr_time > _reuse_time)
			{
				if (st.getQuestItemsCount(PcInventory.ADENA_ID) > SCROLL_24_PRICE)
				{
					st.takeItems(PcInventory.ADENA_ID, SCROLL_24_PRICE);
					st.giveItems(MASTER_YOGI_SCROLL, 24);
					saveGlobalQuestVar(player.getAccountName(), Long.toString(System.currentTimeMillis() + (SCROLL_24_TIME * 3600000)));
					htmltext = "32599-scroll24.htm";
				}
				else
				{
					htmltext = "32599-s24-no.htm";
				}
			}
			else
			{
				long _remaining_time = (_reuse_time - _curr_time) / 1000;
				int hours = (int) _remaining_time / 3600;
				int minutes = ((int) _remaining_time % 3600) / 60;
				if (hours > 0)
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ITEM_PURCHASABLE_IN_S1_HOURS_S2_MINUTES);
					sm.addNumber(hours);
					sm.addNumber(minutes);
					player.sendPacket(sm);
					htmltext = "32599-scroll24.htm";
				}
				else if (minutes > 0)
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ITEM_PURCHASABLE_IN_S1_MINUTES);
					sm.addNumber(minutes);
					player.sendPacket(sm);
					htmltext = "32599-scroll24.htm";
				}
				else
				{
					// Little glitch. There is no SystemMessage with seconds only.
					// If time is less than 1 minute player can buy scrolls
					if (st.getQuestItemsCount(PcInventory.ADENA_ID) > SCROLL_24_PRICE)
					{
						st.takeItems(PcInventory.ADENA_ID, SCROLL_24_PRICE);
						st.giveItems(MASTER_YOGI_SCROLL, 24);
						saveGlobalQuestVar(player.getAccountName(), Long.toString(System.currentTimeMillis() + (SCROLL_24_TIME * 3600000)));
						htmltext = "32599-scroll24.htm";
					}
					else
					{
						htmltext = "32599-s24-no.htm";
					}
				}
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_1"))
		{
			if (st.getQuestItemsCount(PcInventory.ADENA_ID) > SCROLL_1_PRICE)
			{
				st.takeItems(PcInventory.ADENA_ID, SCROLL_1_PRICE);
				st.giveItems(MASTER_YOGI_SCROLL, 1);
				htmltext = "32599-scroll-ok.htm";
			}
			else
			{
				htmltext = "32599-s1-no.htm";
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_10"))
		{
			if (st.getQuestItemsCount(PcInventory.ADENA_ID) > SCROLL_10_PRICE)
			{
				st.takeItems(PcInventory.ADENA_ID, SCROLL_10_PRICE);
				st.giveItems(MASTER_YOGI_SCROLL, 10);
				htmltext = "32599-scroll-ok.htm";
			}
			else
			{
				htmltext = "32599-s10-no.htm";
			}
		}
		else if (event.equalsIgnoreCase("receive_reward"))
		{
			if ((st.getItemEquipped(Inventory.PAPERDOLL_RHAND) == MASTER_YOGI_STAFF) && (st.getEnchantLevel(MASTER_YOGI_STAFF) > 3))
			{
				switch (st.getEnchantLevel(MASTER_YOGI_STAFF))
				{
					case 4:
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 5:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						break;
					case 6:
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						break;
					case 7:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(956, 1); // Scroll: Enchant Armor (D)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(955, 1); // Scroll: Enchant Weapon (D)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 8:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(956, 1); // Scroll: Enchant Armor (D)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(955, 1); // Scroll: Enchant Weapon (D)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 9:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(951, 1); // Scroll: Enchant Weapon (C)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(952, 1); // Scroll: Enchant Armor (C)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 10:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(951, 1); // Scroll: Enchant Weapon (C)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(952, 1); // Scroll: Enchant Armor (C)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1); 
						break;
					case 11:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(948, 1); // Scroll: Enchant Armor (B)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(947, 1); // Scroll: Enchant Weapon (B)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);					
						break;
					case 12:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(948, 1); // Scroll: Enchant Armor (B)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(947, 1); // Scroll: Enchant Weapon (B)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 13:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(729, 1); // Scroll: Enchant Weapon (A)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(730, 1); // Scroll: Enchant Armor (A)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 14:
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(729, 1); // Scroll: Enchant Weapon (A)
						st.giveItems(6406, 3); // Firework
						st.giveItems(6407, 2); // Large Firework
						st.giveItems(730, 1); // Scroll: Enchant Armor (A)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						break;
					case 15:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(948, 1); // Scroll: Enchant Armor (B)
						st.giveItems(729, 1); // Scroll: Enchant Weapon (A)
						st.giveItems(947, 1); // Scroll: Enchant Weapon (B)
						st.giveItems(730, 1); // Scroll: Enchant Armor (A)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						break;
					case 16:
						st.giveItems(HAT_EVENT_REWARD[getRandom(3)], 1);
						st.giveItems(948, 1); // Scroll: Enchant Armor (B)
						st.giveItems(729, 1); // Scroll: Enchant Weapon (A)
						st.giveItems(947, 1); // Scroll: Enchant Weapon (B)
						st.giveItems(730, 1); // Scroll: Enchant Armor (A)
						st.giveItems(HAT_SHADOW_REWARD[getRandom(3)], 1);
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						break;
					case 17:
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(960, 1); // Scroll: Enchant Armor (S)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						st.giveItems(CRYSTAL_REWARD[getRandom(3)], 1); // Red/Blue/Green Soul Crystal - Stage 14
						st.giveItems(9576, 1); // Top-Grade Life Stone: level 80
						st.giveItems(14169, 1); // Top-Grade Life Stone: level 84
						st.giveItems(16167, 1); // Top-Grade Life Stone: level 86
						st.giveItems(959, 1); // Scroll: Enchant Weapon (S)
						break;
					case 18:
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(960, 1); // Scroll: Enchant Armor (S)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						st.giveItems(CRYSTAL_REWARD[getRandom(3)], 1); // Red/Blue/Green Soul Crystal - Stage 14
						st.giveItems(9576, 1); // Top-Grade Life Stone: level 80
						st.giveItems(14169, 1); // Top-Grade Life Stone: level 84
						st.giveItems(16167, 1); // Top-Grade Life Stone: level 86
						st.giveItems(959, 1); // Scroll: Enchant Weapon (S)
						break;
					case 19:
						st.giveItems(13992, 1); // Grade S Accessory Chest (Event)
						st.giveItems(13991, 1); // Grade S Armor Chest (Event)
						st.giveItems(13990, 1); // Grade S Weapon Chest (Event)
						st.giveItems(960, 1); // Scroll: Enchant Armor (S)
						st.giveItems(8762, 1); // Top-Grade Life Stone: level 76
						st.giveItems(9576, 1); // Top-Grade Life Stone: level 80
						st.giveItems(CRYSTAL_REWARD[getRandom(3)], 1); // Red/Blue/Green Soul Crystal - Stage 14
						st.giveItems(14169, 1); // Top-Grade Life Stone: level 84
						st.giveItems(16167, 1); // Top-Grade Life Stone: level 86
						st.giveItems(959, 1); // Scroll: Enchant Weapon (S)
						break;
					case 20:
						st.giveItems(CRYSTAL_REWARD[getRandom(3)], 1); // Red/Blue/Green Soul Crystal - Stage 14
						st.giveItems(9575, 1); // High-Grade Life Stone: level 80
						st.giveItems(8752, 1); // High-Grade Life Stone: level 76
						st.giveItems(14168, 1); // High-Grade Life Stone: level 84
						st.giveItems(16166, 1); // High-Grade Life Stone: level 86
						break;
					case 21:
						st.giveItems(9575, 1); // High-Grade Life Stone: level 80
						st.giveItems(8752, 1); // High-Grade Life Stone: level 76
						st.giveItems(14168, 1); // High-Grade Life Stone: level 84
						st.giveItems(16166, 1); // High-Grade Life Stone: level 86
						st.giveItems(CRYSTAL_REWARD[getRandom(3)], 1); // Red/Blue/Green Soul Crystal - Stage 14
						break;
					case 22:
						st.giveItems(13989, 3); // S80 Grade Armor Chest (Event)
						st.giveItems(13988, 3); // S80 Grade Weapon Chest (Event)
						break;
					case 23:
						st.giveItems(13989, 5); // S80 Grade Armor Chest (Event)
						st.giveItems(13988, 5); // S80 Grade Weapon Chest (Event)
					default:
						if (st.getEnchantLevel(MASTER_YOGI_STAFF) > 23)
						{
							st.giveItems(13989, 10); // S80 Grade Armor Chest (Event)
							st.giveItems(13988, 10); // S80 Grade Weapon Chest (Event)
						}
						break;
				}
				st.takeItems(MASTER_YOGI_STAFF, 1);
				htmltext = "32599-rewardok.htm";
			}
			else
			{
				htmltext = "32599-rewardnostaff.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		if (player.getQuestState(getName()) == null)
		{
			newQuestState(player);
		}
		return npc.getNpcId() + ".htm";
	}
	
	public static void main(String[] args)
	{
		new MasterOfEnchanting(MasterOfEnchanting.class.getSimpleName(), "events");
	}
}