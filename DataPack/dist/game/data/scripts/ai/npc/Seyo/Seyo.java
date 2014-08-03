package ai.npc.Seyo;

import ai.npc.AbstractNpcAI;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;

public final class Seyo extends AbstractNpcAI
{
	// NPC
	private static final int SEYO = 32737;
	// Item
	private static final int STONE_FRAGMENT = 15486; // Spirit Stone Fragment
	
	private Seyo()
	{
		super(Seyo.class.getSimpleName(), "ai/npc");
		addStartNpc(SEYO);
		addTalkId(SEYO);
		addFirstTalkId(SEYO);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = null;
		if (event.equals("TRICKERY_TIMER") && (npc != null) && npc.isScriptValue(1))
		{
			npc.setScriptValue(0);
			final int random = getRandom(5) + 1;
			switch (random)
			{
				case 1:
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.OK_WHOS_NEXT_IT_ALL_DEPENDS_ON_YOUR_FATE_AND_LUCK_RIGHT_AT_LEAST_COME_AND_TAKE_A_LOOK));
					break;
				}
				case 2:
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.NO_ONE_ELSE_DONT_WORRY_I_DONT_BITE_HAHA));
					break;
				}
				case 3:
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.THERE_WAS_SOMEONE_WHO_WON_10000_FROM_ME_A_WARRIOR_SHOULDNT_JUST_BE_GOOD_AT_FIGHTING_RIGHT_YOUVE_GOTTA_BE_GOOD_IN_EVERYTHING));
					break;
				}
				case 4:
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.OK_MASTER_OF_LUCK_THATS_YOU_HAHA_WELL_ANYONE_CAN_COME_AFTER_ALL));
					break;
				}
				case 5:
				{
					npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.SHEDDING_BLOOD_IS_A_GIVEN_ON_THE_BATTLEFIELD_AT_LEAST_ITS_SAFE_HERE));
					break;
				}
			}
		}
		else if (npc.isScriptValue(0))
		{
			switch (event)
			{
				case "give1":
				{
					if (getQuestItemsCount(player, STONE_FRAGMENT) > 0)
					{
						npc.setScriptValue(1);
						takeItems(player, STONE_FRAGMENT, 1);
						final int chance = getRandom(100) + 1;
						if (chance > 99)
						{
							giveItems(player, STONE_FRAGMENT, 100);
							final NpcSay text = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.AMAZING_S1_TOOK_100_OF_THESE_SOUL_STONE_FRAGMENTS_WHAT_A_COMPLETE_SWINDLER);
							text.addStringParameter(player.getName());
							npc.broadcastPacket(text);
						}
						else
						{
							final NpcSay text = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.HMM_HEY_DID_YOU_GIVE_S1_SOMETHING_BUT_IT_WAS_JUST_1_HAHA);
							text.addStringParameter(player.getName());
							npc.broadcastPacket(text);
						}
						startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
					}
					else
					{
						htmltext = "32737-01.html";
					}
					break;
				}
				case "give5":
				{
					if (getQuestItemsCount(player, STONE_FRAGMENT) >= 5)
					{
						npc.setScriptValue(1);
						takeItems(player, STONE_FRAGMENT, 5);
						final int chance = getRandom(100) + 1;
						if (chance > 80)
						{
							final int itemCount = (getRandom(3) + 5) * 2;
							giveItems(player, STONE_FRAGMENT, itemCount);
							final NpcSay text = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.S1_PULLED_ONE_WITH_S2_DIGITS_LUCKY_NOT_BAD);
							text.addStringParameters(player.getName(), String.valueOf(itemCount));
							npc.broadcastPacket(text);
						}
						else if ((chance > 20) && (chance <= 80))
						{
							giveItems(player, STONE_FRAGMENT, 1);
							npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.ITS_BETTER_THAN_LOSING_IT_ALL_RIGHT_OR_DOES_THIS_FEEL_WORSE));
						}
						else
						{
							final NpcSay text = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.AHEM_S1_HAS_NO_LUCK_AT_ALL_TRY_PRAYING);
							text.addStringParameter(player.getName());
							npc.broadcastPacket(text);
						}
						startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
					}
					else
					{
						htmltext = "32737-02.html";
					}
					break;
				}
				case "give20":
				{
					if (getQuestItemsCount(player, STONE_FRAGMENT) >= 20)
					{
						npc.setScriptValue(1);
						takeItems(player, STONE_FRAGMENT, 20);
						final int chance = getRandom(10000) + 1;
						if (chance == 10000)
						{
							giveItems(player, STONE_FRAGMENT, chance);
							final NpcSay text = new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.AH_ITS_OVER_WHAT_KIND_OF_GUY_IS_THAT_DAMN_FINE_YOU_S1_TAKE_IT_AND_GET_OUTTA_HERE);
							text.addStringParameter(player.getName());
							npc.broadcastPacket(text);
						}
						else if ((chance > 10) && (chance <= 9999))
						{
							giveItems(player, STONE_FRAGMENT, 1);
							npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.A_BIG_PIECE_IS_MADE_UP_OF_LITTLE_PIECES_SO_HERES_A_LITTLE_PIECE));
						}
						else if (chance <= 10) // TODO: possible bug in L2Off AI, need recheck it
						{
							giveItems(player, STONE_FRAGMENT, 1);
							npc.broadcastPacket(new NpcSay(npc.getObjectId(), Say2.NPC_ALL, npc.getNpcId(), NpcStringId.YOU_DONT_FEEL_BAD_RIGHT_ARE_YOU_SAD_BUT_DONT_CRY));
						}
						startQuestTimer("TRICKERY_TIMER", 5000, npc, null);
					}
					else
					{
						htmltext = "32737-03.html";
					}
					break;
				}
			}
		}
		else
		{
			htmltext = "32737-04.html";
		}
		return htmltext;
	}
	
	public static void main(String[] args)
	{
		new Seyo();
	}
}