package events.GiftOfVitality;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.event.LongTimeEvent;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.quest.State;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

/**
 * PkElfo
 *
 */

public class GiftOfVitality extends LongTimeEvent
{
	// Reuse between buffs
	private static final int HOURS = 5;
	// NPC
	private static final int JACK = 4306;
	public GiftOfVitality(String name, String descr)
	{
		super(name, descr);
		addStartNpc(JACK);
		addFirstTalkId(JACK);
		addTalkId(JACK);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		QuestState st = player.getQuestState(getName());
		if (event.equalsIgnoreCase("vitality"))
		{
			long _reuse = 0;
			String _streuse = st.get("reuse");
			if (_streuse != null)
			{
				_reuse = Long.parseLong(_streuse);
			}
			if (_reuse > System.currentTimeMillis())
			{
				long remainingTime = (_reuse - System.currentTimeMillis()) / 1000;
				int hours = (int) (remainingTime / 3600);
				int minutes = (int) ((remainingTime % 3600) / 60);
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.AVAILABLE_AFTER_S1_S2_HOURS_S3_MINUTES);
				sm.addSkillName(23179);
				sm.addNumber(hours);
				sm.addNumber(minutes);
				player.sendPacket(sm);
				htmltext = "4306-notime.htm";
			}
			else
			{
				npc.setTarget(player);
				// Gift of Vitality
				npc.doCast(L2Skill.valueOf(23179, 1));
				st.setState(State.STARTED);
				st.set("reuse", String.valueOf(System.currentTimeMillis() + (HOURS * 3600000)));
				htmltext = "4306-okvitality.htm";
			}
		}
		else if (event.equalsIgnoreCase("memories_player"))
		{
			if (player.getLevel() < 76)
			{
				htmltext = "4306-nolevel.htm";
			}
			else
			{
				npc.setTarget(player);
				npc.doCast(L2Skill.valueOf(5627, 1)); // Wind Walk
				npc.doCast(L2Skill.valueOf(5628, 1)); // Shield
				npc.doCast(L2Skill.valueOf(5637, 1)); // Magic Barrier
				if (player.isMageClass())
				{
					npc.doCast(L2Skill.valueOf(5633, 1)); // Bless the Soul
					npc.doCast(L2Skill.valueOf(5634, 1)); // Acumen
					npc.doCast(L2Skill.valueOf(5635, 1)); // Concentration
					npc.doCast(L2Skill.valueOf(5636, 1)); // Empower
				}
				else
				{
					npc.doCast(L2Skill.valueOf(5629, 1)); // Bless the Body
					npc.doCast(L2Skill.valueOf(5630, 1)); // Vampiric Rage
					npc.doCast(L2Skill.valueOf(5631, 1)); // Regeneration
					npc.doCast(L2Skill.valueOf(5632, 1)); // Haste
				}
				htmltext = "4306-okbuff.htm";
			}
		}
		else if (event.equalsIgnoreCase("memories_summon"))
		{
			if (player.getLevel() < 76)
			{
				htmltext = "4306-nolevel.htm";
			}
			else if (!player.hasSummon() || !player.getSummon().isServitor())
			{
				htmltext = "4306-nosummon.htm";
			}
			else
			{
				npc.setTarget(player.getSummon());
				npc.doCast(L2Skill.valueOf(5627, 1)); // Wind Walk
				npc.doCast(L2Skill.valueOf(5628, 1)); // Shield
				npc.doCast(L2Skill.valueOf(5637, 1)); // Magic Barrier
				npc.doCast(L2Skill.valueOf(5629, 1)); // Bless the Body
				npc.doCast(L2Skill.valueOf(5633, 1)); // Bless the Soul
				npc.doCast(L2Skill.valueOf(5630, 1)); // Vampiric Rage
				npc.doCast(L2Skill.valueOf(5634, 1)); // Acumen
				npc.doCast(L2Skill.valueOf(5631, 1)); // Regeneration
				npc.doCast(L2Skill.valueOf(5635, 1)); // Concentration
				npc.doCast(L2Skill.valueOf(5632, 1)); // Haste
				npc.doCast(L2Skill.valueOf(5636, 1)); // Empower
				htmltext = "4306-okbuff.htm";
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
		return "4306.htm";
	}
	public static void main(String[] args)
	{
		new GiftOfVitality(GiftOfVitality.class.getSimpleName(), "events");
	}
}