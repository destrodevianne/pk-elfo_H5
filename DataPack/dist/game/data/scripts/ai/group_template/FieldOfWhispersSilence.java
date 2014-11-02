package ai.group_template;

import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.serverpackets.CreatureSay;
import ai.npc.AbstractNpcAI;

public class FieldOfWhispersSilence extends AbstractNpcAI
{
	private static final int BRAZIER_OF_PURITY = 18806;
	private static final int GUARDIAN_SPIRITS_OF_MAGIC_FORCE = 22659;
	
	public FieldOfWhispersSilence(int questId, String name, String descr)
	{
		super(name, descr);
		
		addAggroRangeEnterId(BRAZIER_OF_PURITY);
		addAggroRangeEnterId(GUARDIAN_SPIRITS_OF_MAGIC_FORCE);
	}
	
	@Override
	public String onAggroRangeEnter(L2Npc npc, L2PcInstance player, boolean isPet)
	{
		switch (npc.getNpcId())
		{
			case BRAZIER_OF_PURITY:
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 0, npc.getName(), NpcStringId.THE_PURIFICATION_FIELD_IS_BEING_ATTACKED_GUARDIAN_SPIRITS_PROTECT_THE_MAGIC_FORCE));
				break;
			case GUARDIAN_SPIRITS_OF_MAGIC_FORCE:
				npc.broadcastPacket(new CreatureSay(npc.getObjectId(), 0, npc.getName(), NpcStringId.EVEN_THE_MAGIC_FORCE_BINDS_YOU_YOU_WILL_NEVER_BE_FORGIVEN));
				break;
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		new FieldOfWhispersSilence(-1, "FieldOfWhispersSilence", "ai/group_template");
	}
}