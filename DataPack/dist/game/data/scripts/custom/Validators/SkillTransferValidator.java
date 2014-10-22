package custom.Validators;

import pk.elfo.Config;
import pk.elfo.gameserver.datatables.ClassListData;
import pk.elfo.gameserver.datatables.SkillTreesData;
import pk.elfo.gameserver.model.L2SkillLearn;
import pk.elfo.gameserver.model.PcCondOverride;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.holders.ItemHolder;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.scripting.scriptengine.events.ProfessionChangeEvent;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2Script;
import pk.elfo.gameserver.util.Util;

/**
 * Skill Transfer validator.
 */
public final class SkillTransferValidator extends L2Script
{
	private static final String qn = "SkillTransfer";
	
	private static final ItemHolder[] PORMANDERS =
	{
		// Cardinal (97)
		new ItemHolder(15307, 1),
		// Eva's Saint (105)
		new ItemHolder(15308, 1),
		// Shillen Saint (112)
		new ItemHolder(15309, 4)
	};
	
	public SkillTransferValidator(int id, String name, String descr)
	{
		super(id, name, descr);
		setOnEnterWorld(true);
	}
	
	@Override
	public String onEnterWorld(L2PcInstance player)
	{
		if (getTransferClassIndex(player) >= 0)
		{
			addProfessionChangeNotify(player);
			startQuestTimer("givePormanders", 2000, null, player);
		}
		return null;
	}
	
	@Override
	public void onProfessionChange(ProfessionChangeEvent event)
	{
		startQuestTimer("givePormanders", 2000, null, event.getPlayer());
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (event.equals("givePormanders"))
		{
			final int index = getTransferClassIndex(player);
			if (index >= 0)
			{
				QuestState st = player.getQuestState(qn);
				if (st == null)
				{
					st = newQuestState(player);
				}
				
				final String name = qn + String.valueOf(player.getClassId().getId());
				if (st.getInt(name) == 0)
				{
					st.setInternal(name, "1");
					if (st.getGlobalQuestVar(name).isEmpty())
					{
						st.saveGlobalQuestVar(name, "1");
						player.addItem(qn, PORMANDERS[index].getId(), PORMANDERS[index].getCount(), null, true);
					}
				}
				
				if (Config.SKILL_CHECK_ENABLE && (!player.canOverrideCond(PcCondOverride.SKILL_CONDITIONS) || Config.SKILL_CHECK_GM))
				{
					long count = PORMANDERS[index].getCount() - player.getInventory().getInventoryItemCount(PORMANDERS[index].getId(), -1, false);
					for (L2Skill sk : player.getAllSkills())
					{
						for (L2SkillLearn s : SkillTreesData.getInstance().getTransferSkillTree(player.getClassId()).values())
						{
							if (s.getSkillId() == sk.getId())
							{
								// Holy Weapon allowed for Shilien Saint/Inquisitor stance
								if ((sk.getId() == 1043) && (index == 2) && player.isInStance())
								{
									continue;
								}
								
								count--;
								if (count < 0)
								{
									final String className = ClassListData.getInstance().getClass(player.getClassId()).getClassName();
									Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " has too many transfered skills or items, skill:" + s.getName() + " (" + sk.getId() + "/" + sk.getLevel() + "), class:" + className, 1);
									if (Config.SKILL_CHECK_REMOVE)
									{
										player.removeSkill(sk);
									}
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private int getTransferClassIndex(L2PcInstance player)
	{
		switch (player.getClassId().getId())
		{
			case 97: // Cardinal
				return 0;
			case 105: // Eva's Saint
				return 1;
			case 112: // Shillien Saint
				return 2;
			default:
				return -1;
		}
	}
	
	public static void main(String[] args)
	{
		new SkillTransferValidator(-1, qn, "custom");
	}
}