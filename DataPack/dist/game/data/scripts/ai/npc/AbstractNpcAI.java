package ai.npc;

import java.util.logging.Logger;

import pk.elfo.gameserver.ai.CtrlIntention;
import pk.elfo.gameserver.model.actor.L2Attackable;
import pk.elfo.gameserver.model.actor.L2Character;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.L2Playable;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.serverpackets.NpcSay;
import pk.elfo.gameserver.network.serverpackets.SocialAction;
import pk.elfo.gameserver.scripting.scriptengine.impl.L2Script;
import pk.elfo.gameserver.util.Broadcast;

/**
 * Abstract NPC AI class for datapack based AIs.
 * PkElfo
 */
public abstract class AbstractNpcAI extends L2Script
{
	public Logger _log = Logger.getLogger(getClass().getSimpleName());
	
	/**
	 * Simple on first talk event handler.
	 */
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return npc.getNpcId() + ".html";
	}
	
	public AbstractNpcAI(String name, String descr)
	{
		super(-1, name, descr);
	}
        /**
         * This is used simply for convenience of replacing
         * jython 'element in list' boolean method.
         * @param array 
         * @param obj 
         * @param <T> 
         * @return 
         */
        public static <T> boolean contains(T[] array, T obj)
        {
        	for (int i = 0; i < array.length; i++)
        	{
        		if (array[i] == obj)
        		{
        			return true;
        		}
        	}
        	return false;
        }
        
        /**
         * Checking if array contains object.
         * 
         * @param array
         * @param obj
         * @return true, if array contains obj
         */
        public static boolean contains(int[] array, int obj)
        {
        	for (int i = 0; i < array.length; i++)
        	{
        		if (array[i] == obj)
        		{
        			return true;
        		}
        	}
            return false;
        }

        /**
         * Checking if array contains object and returns place where is.
         * 
         * @param array
         * @param obj
         * @return place, where is this obj
         */
        public static int getContainsId(int[] array, int obj)
        {
        	for (int i = 0; i < array.length; i++)
        	{
        		if (array[i] == obj)
        		{
        			return i;
        		}
        	}
        	return -1;
        }

	/**
	 * Registers the following events to the current script:<br>
	 * <ul>
	 * <li>ON_ATTACK</li>
	 * <li>ON_KILL</li>
	 * <li>ON_SPAWN</li>
	 * <li>ON_SPELL_FINISHED</li>
	 * <li>ON_SKILL_SEE</li>
	 * <li>ON_FACTION_CALL</li>
	 * <li>ON_AGGR_RANGE_ENTER</li>
	 * </ul>
	 * @param mobs
	 */
	public void registerMobs(int... mobs)
	{
		addAttackId(mobs);
		addKillId(mobs);
		addSpawnId(mobs);
		addSpellFinishedId(mobs);
		addSkillSeeId(mobs);
		addAggroRangeEnterId(mobs);
		addFactionCallId(mobs);
	}
	
	/**
	 * This is used to register all monsters contained in mobs for a particular script event types defined in types.
	 * @param mobs
	 * @param types
	 */
	public void registerMobs(int[] mobs, QuestEventType... types)
	{
		for (QuestEventType type : types)
		{
			addEventId(type, mobs);
		}
	}
	
	public void registerMobs(Iterable<Integer> mobs, QuestEventType... types)
	{
		for (int id : mobs)
		{
			for (QuestEventType type : types)
			{
				addEventId(type, id);
			}
		}
	}
	
	/**
	 * Broadcasts NpcSay packet to all known players with custom string.
	 * @param npc
	 * @param type
	 * @param text
	 */
	protected void broadcastNpcSay(L2Npc npc, int type, String text)
	{
		Broadcast.toKnownPlayers(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getIdTemplate(), text));
	}
	
	/**
	 * Broadcasts NpcSay packet to all known players with npc string id.
	 * @param npc
	 * @param type
	 * @param stringId
	 */
	protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId)
	{
		Broadcast.toKnownPlayers(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getIdTemplate(), stringId));
	}
	
	/**
	 * Broadcasts NpcSay packet to all known players with npc string id.
	 * @param npc
	 * @param type
	 * @param stringId
	 * @param parameters
	 */
	protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId, String... parameters)
	{
		final NpcSay say = new NpcSay(npc.getObjectId(), type, npc.getTemplate().getIdTemplate(), stringId);
		if (parameters != null)
		{
			for (String parameter : parameters)
			{
				say.addStringParameter(parameter);
			}
		}
		Broadcast.toKnownPlayers(npc, say);
	}
	
	/**
	 * Broadcasts NpcSay packet to all known players with custom string in specific radius.
	 * @param npc
	 * @param type
	 * @param text
	 * @param radius
	 */
	protected void broadcastNpcSay(L2Npc npc, int type, String text, int radius)
	{
		Broadcast.toKnownPlayersInRadius(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getIdTemplate(), text), radius);
	}
	
	/**
	 * Broadcasts NpcSay packet to all known players with npc string id in specific radius.
	 * @param npc
	 * @param type
	 * @param stringId
	 * @param radius
	 */
	protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId, int radius)
	{
		Broadcast.toKnownPlayersInRadius(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getIdTemplate(), stringId), radius);
	}
	
	/**
	 * Broadcasts SocialAction packet to self and known players.
	 * @param character
	 * @param actionId
	 */
	protected void broadcastSocialAction(L2Character character, int actionId)
	{
		Broadcast.toSelfAndKnownPlayers(character, new SocialAction(character.getObjectId(), actionId));
	}
	
	/**
	 * Broadcasts SocialAction packet to self and known players in specific radius.
	 * @param character
	 * @param actionId
	 * @param radius
	 */
	protected void broadcastSocialAction(L2Character character, int actionId, int radius)
	{
		Broadcast.toSelfAndKnownPlayersInRadius(character, new SocialAction(character.getObjectId(), actionId), radius);
	}
	
	/**
	 * Monster is running and attacking the playable.
	 * @param npc
	 * @param playable
	 */
	protected void attackPlayer(L2Attackable npc, L2Playable playable)
	{
		npc.setIsRunning(true);
		npc.addDamageHate(playable, 0, 999);
		npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, playable);
	}
}