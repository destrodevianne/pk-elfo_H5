package ai.individual;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.itemcontainer.Inventory;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import ai.npc.AbstractNpcAI;

/**
 * Cat's Eye Bandit (Quest Monster) AI.
 */
public final class CatsEyeBandit extends AbstractNpcAI
{
	// NPC ID
	private static final int MOB_ID = 27038;
	// Weapons
	private static final int BOW = 1181;
	private static final int DAGGER = 1182;
	
	private CatsEyeBandit(String name, String descr)
	{
		super(name, descr);
		addAttackId(MOB_ID);
		addKillId(MOB_ID);
	}
	
	@Override
	public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isSummon)
	{
		final QuestState qs = attacker.getQuestState("403_PathToRogue"); // TODO: Replace with class name.
		if (npc.isScriptValue(0) && (qs != null) && ((qs.getItemEquipped(Inventory.PAPERDOLL_RHAND) == BOW) || (qs.getItemEquipped(Inventory.PAPERDOLL_RHAND) == DAGGER)))
		{
			broadcastNpcSay(npc, Say2.NPC_ALL, NpcStringId.YOU_CHILDISH_FOOL_DO_YOU_THINK_YOU_CAN_CATCH_ME);
			npc.setScriptValue(1);
		}
		return super.onAttack(npc, attacker, damage, isSummon);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState qs = killer.getQuestState("403_PathToRogue"); // TODO: Replace with class name.
		if (qs != null)
		{
			broadcastNpcSay(npc, Say2.NPC_ALL, NpcStringId.I_MUST_DO_SOMETHING_ABOUT_THIS_SHAMEFUL_INCIDENT);
		}
		return super.onKill(npc, killer, isSummon);
	}
	
	public static void main(String[] args)
	{
		new CatsEyeBandit(CatsEyeBandit.class.getSimpleName(), "ai/individual");
	}
}