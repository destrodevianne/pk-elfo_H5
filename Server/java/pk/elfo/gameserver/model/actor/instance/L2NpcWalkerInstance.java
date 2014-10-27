package pk.elfo.gameserver.model.actor.instance;

import pk.elfo.gameserver.ai.L2CharacterAI;
import pk.elfo.gameserver.ai.L2NpcWalkerAI;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.model.skills.L2Skill;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.util.Broadcast;

public class L2NpcWalkerInstance extends L2Npc
{
	/**
	 * Constructor of L2NpcWalkerInstance (use L2Character and L2NpcInstance constructor).
	 * @param objectId given object id
	 * @param template L2NpcTemplateForThisAi
	 */
	public L2NpcWalkerInstance(int objectId, L2NpcTemplate template)
	{
		super(objectId, template);
		setInstanceType(InstanceType.L2NpcWalkerInstance);
		setAI(new L2NpcWalkerAI(new L2NpcWalkerAIAccessor()));
	}
	
	/**
	 * AI can't be detached, npc must move always with the same AI instance.
	 * @param newAI AI to set for this L2NpcWalkerInstance
	 */
	@Override
	public void setAI(L2CharacterAI newAI)
	{
		if (!(_ai instanceof L2NpcWalkerAI))
		{
			_ai = newAI;
		}
	}
	
	@Override
	public void onSpawn()
	{
		getAI().setHomeX(getX());
		getAI().setHomeY(getY());
		getAI().setHomeZ(getZ());
	}
	
	/**
	 * Sends a chat to all _knowObjects
	 * @param chat message to say
	 * @param npcString
	 */
	public void broadcastChat(String chat, NpcStringId npcString)
	{
		NpcSay cs;
		if (npcString == null)
		{
			cs = new NpcSay(getObjectId(), Say2.NPC_ALL, getNpcId(), chat);
		}
		else
		{
			cs = new NpcSay(getObjectId(), Say2.NPC_ALL, getNpcId(), npcString);
		}
		Broadcast.toKnownPlayers(this, cs);
	}
	
	/**
	 * NPCs are immortal
	 * @param i ignore it
	 * @param attacker ignore it
	 * @param awake ignore it
	 */
	@Override
	public void reduceCurrentHp(double i, L2Character attacker, boolean awake, boolean isDOT, L2Skill skill)
	{
	}
	
	/**
	 * NPCs are immortal
	 * @param killer ignore it
	 * @return false
	 */
	@Override
	public boolean doDie(L2Character killer)
	{
		return false;
	}
	
	@Override
	public L2NpcWalkerAI getAI()
	{
		return (L2NpcWalkerAI) _ai;
	}
	
	protected class L2NpcWalkerAIAccessor extends L2Character.AIAccessor
	{
		/**
		 * AI can't be detached.
		 */
		@Override
		public void detachAI()
		{
		}
	}
	
	@Override
	public boolean isWalker()
	{
		return true;
	}
}