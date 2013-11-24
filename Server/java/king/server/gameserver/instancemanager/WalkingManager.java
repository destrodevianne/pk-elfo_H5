package king.server.gameserver.instancemanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import king.server.gameserver.ThreadPoolManager;
import king.server.gameserver.ai.CtrlIntention;
import king.server.gameserver.engines.DocumentParser;
import king.server.gameserver.model.L2CharPosition;
import king.server.gameserver.model.L2NpcWalkerNode;
import king.server.gameserver.model.L2WalkRoute;
import king.server.gameserver.model.Location;
import king.server.gameserver.model.actor.L2Npc;
import king.server.gameserver.model.actor.instance.L2MonsterInstance;
import king.server.gameserver.model.quest.Quest;
import king.server.gameserver.network.NpcStringId;
import king.server.util.Rnd;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class WalkingManager extends DocumentParser
{
	// Repeat style:
	// 0 - go back
	// 1 - go to first point (circle style)
	// 2 - teleport to first point (conveyor style)
	// 3 - random walking between points.
	private static final byte REPEAT_GO_BACK = 0;
	private static final byte REPEAT_GO_FIRST = 1;
	private static final byte REPEAT_TELE_FIRST = 2;
	private static final byte REPEAT_RANDOM = 3;
	
	protected final Map<Integer, L2WalkRoute> _routes = new HashMap<>(); // all available routes
	private final Map<Integer, WalkInfo> _activeRoutes = new HashMap<>(); // each record represents NPC, moving by predefined route from _routes, and moving progress
	private final Map<Integer, NpcRoutesHolder> _routesToAttach = new HashMap<>(); // each record represents NPC and all available routes for it
	
	/**
	 * Holds depending between NPC's spawn point and route
	 */
	private class NpcRoutesHolder
	{
		private final Map<String, Integer> _correspondences;
		
		public NpcRoutesHolder()
		{
			_correspondences = new HashMap<>();
		}
		
		/**
		 * Add correspondence between specific route and specific spawn point
		 * @param routeId id of route
		 * @param loc Location of spawn point
		 */
		public void addRoute(int routeId, Location loc)
		{
			_correspondences.put(getUniqueKey(loc), routeId);
		}
		
		/**
		 * @param npc
		 * @return route id for given NPC.
		 */
		public int getRouteId(L2Npc npc)
		{
			if (npc.getSpawn() != null)
			{
				String key = getUniqueKey(npc.getSpawn().getSpawnLocation());
				return _correspondences.containsKey(key) ? _correspondences.get(key) : -1;
			}
			
			return -1;
		}
		
		/**
		 * @param loc
		 * @return unique text string for given Location.
		 */
		private String getUniqueKey(Location loc)
		{
			return (loc.getX() + "-" + loc.getY() + "-" + loc.getZ());
		}
	}
	
	/**
	 * Holds info about current walk progress
	 */
	private class WalkInfo
	{
		protected ScheduledFuture<?> _walkCheckTask;
		protected boolean _blocked = false;
		protected boolean _suspended = false;
		protected boolean _stoppedByAttack = false;
		protected int _currentNode = 0;
		protected boolean _forward = true; // Determines first --> last or first <-- last direction
		private final int _routeId;
		protected long _lastActionTime; // Debug field
		
		public WalkInfo(int routeId)
		{
			_routeId = routeId;
		}
		
		/**
		 * @return id of route of this WalkInfo.
		 */
		protected L2WalkRoute getRoute()
		{
			return _routes.get(_routeId);
		}
		
		/**
		 * @return current node of this WalkInfo.
		 */
		protected L2NpcWalkerNode getCurrentNode()
		{
			return getRoute().getNodeList().get(_currentNode);
		}
		
		/**
		 * Calculate next node for this WalkInfo and send debug message from given npc
		 * @param npc NPC to debug message to be sent from
		 */
		protected void calculateNextNode(L2Npc npc)
		{
			// Check this first, within the bounds of random moving, we have no conception of "first" or "last" node
			if (getRoute().getRepeatType() == REPEAT_RANDOM)
			{
				int newNode = _currentNode;
				
				while (newNode == _currentNode)
				{
					newNode = Rnd.get(getRoute().getNodesCount());
				}
				
				_currentNode = newNode;
				npc.sendDebugMessage("Route id: " + getRoute().getId() + ", next random node is " + _currentNode);
			}
			
			else
			{
				if (_forward)
				{
					_currentNode++;
				}
				else
				{
					_currentNode--;
				}
				
				if (_currentNode == getRoute().getNodesCount()) // Last node arrived
				{
					npc.sendDebugMessage("Route id: " + getRoute().getId() + ", last node arrived");
					
					if (!getRoute().repeatWalk())
					{
						cancelMoving(npc);
						return;
					}
					
					switch (getRoute().getRepeatType())
					{
						case REPEAT_GO_BACK:
							_forward = false;
							_currentNode -= 2;
							break;
						case REPEAT_GO_FIRST:
							_currentNode = 0;
							break;
						case REPEAT_TELE_FIRST:
							npc.teleToLocation(npc.getSpawn().getLocx(), npc.getSpawn().getLocy(), npc.getSpawn().getLocz());
							_currentNode = 0;
							break;
					}
				}
				
				else if (_currentNode == -1) // First node arrived, when direction is first <-- last
				{
					_currentNode = 1;
					_forward = true;
				}
			}
		}
	}
	
	protected WalkingManager()
	{
		load();
	}
	
	@Override
	public final void load()
	{
		parseDatapackFile("data/Routes.xml");
		_log.info(getClass().getSimpleName() + ": " + _routes.size() + " walking routes.");
	}
	
	@Override
	protected void parseDocument()
	{
		Node n = getCurrentDocument().getFirstChild();
		for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
		{
			if (d.getNodeName().equals("route"))
			{
				final Integer routeId = parseInteger(d.getAttributes(), "id");
				boolean repeat = parseBoolean(d.getAttributes(), "repeat");
				String repeatStyle = d.getAttributes().getNamedItem("repeatStyle").getNodeValue();
				byte repeatType;
				if (repeatStyle.equalsIgnoreCase("back"))
				{
					repeatType = REPEAT_GO_BACK;
				}
				else if (repeatStyle.equalsIgnoreCase("cycle"))
				{
					repeatType = REPEAT_GO_FIRST;
				}
				else if (repeatStyle.equalsIgnoreCase("conveyor"))
				{
					repeatType = REPEAT_TELE_FIRST;
				}
				else if (repeatStyle.equalsIgnoreCase("random"))
				{
					repeatType = REPEAT_RANDOM;
				}
				else
				{
					repeatType = -1;
				}
				
				final List<L2NpcWalkerNode> list = new ArrayList<>();
				for (Node r = d.getFirstChild(); r != null; r = r.getNextSibling())
				{
					if (r.getNodeName().equals("point"))
					{
						NamedNodeMap attrs = r.getAttributes();
						int x = parseInt(attrs, "X");
						int y = parseInt(attrs, "Y");
						int z = parseInt(attrs, "Z");
						int delay = parseInt(attrs, "delay");
						
						String chatString = null;
						NpcStringId npcString = null;
						Node node = attrs.getNamedItem("string");
						if (node != null)
						{
							chatString = node.getNodeValue();
						}
						else
						{
							node = attrs.getNamedItem("npcString");
							if (node != null)
							{
								npcString = NpcStringId.getNpcStringId(node.getNodeValue());
								if (npcString == null)
								{
									_log.warning(getClass().getSimpleName() + ": Unknown npcstring '" + node.getNodeValue() + ".");
									continue;
								}
							}
							else
							{
								node = attrs.getNamedItem("npcStringId");
								if (node != null)
								{
									npcString = NpcStringId.getNpcStringId(Integer.parseInt(node.getNodeValue()));
									if (npcString == null)
									{
										_log.warning(getClass().getSimpleName() + ": Unknown npcstring '" + node.getNodeValue() + ".");
										continue;
									}
								}
							}
						}
						list.add(new L2NpcWalkerNode(0, npcString, chatString, x, y, z, delay, parseBoolean(attrs, "run")));
					}
					
					else if (r.getNodeName().equals("target"))
					{
						NamedNodeMap attrs = r.getAttributes();
						try
						{
							int npcId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
							int x = 0, y = 0, z = 0;
							
							x = Integer.parseInt(attrs.getNamedItem("spawnX").getNodeValue());
							y = Integer.parseInt(attrs.getNamedItem("spawnY").getNodeValue());
							z = Integer.parseInt(attrs.getNamedItem("spawnZ").getNodeValue());
							
							NpcRoutesHolder holder = _routesToAttach.containsKey(npcId) ? _routesToAttach.get(npcId) : new NpcRoutesHolder();
							holder.addRoute(routeId, new Location(x, y, z));
							_routesToAttach.put(npcId, holder);
						}
						catch (Exception e)
						{
							_log.warning("Walking Manager: Error in target definition for route ID: " + routeId);
						}
					}
				}
				_routes.put(routeId, new L2WalkRoute(routeId, list, repeat, false, repeatType));
			}
		}
	}
	
	/**
	 * @param npc NPC to check
	 * @return {@code true} if given NPC, or its leader is controlled by Walking Manager and moves currently.
	 */
	public boolean isOnWalk(L2Npc npc)
	{
		L2MonsterInstance monster = null;
		
		if (npc.isMonster())
		{
			if (((L2MonsterInstance) npc).getLeader() == null)
			{
				monster = (L2MonsterInstance) npc;
			}
			else
			{
				monster = ((L2MonsterInstance) npc).getLeader();
			}
		}
		
		if (((monster != null) && !isRegistered(monster)) || !isRegistered(npc))
		{
			return false;
		}
		
		WalkInfo walk = monster != null ? _activeRoutes.get(monster.getObjectId()) : _activeRoutes.get(npc.getObjectId());
		if (walk._stoppedByAttack || walk._suspended)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * @param npc NPC to check
	 * @return {@code true} if given NPC controlled by Walking Manager.
	 */
	public boolean isRegistered(L2Npc npc)
	{
		return _activeRoutes.containsKey(npc.getObjectId());
	}
	
	/**
	 * Start to move given NPC by given route
	 * @param npc NPC to move
	 * @param routeId id of route to move by
	 */
	public void startMoving(final L2Npc npc, final int routeId)
	{
		if (_routes.containsKey(routeId) && (npc != null) && !npc.isDead()) // check, if these route and NPC present
		{
			if (!_activeRoutes.containsKey(npc.getObjectId())) // new walk task
			{
				// only if not already moved / not engaged in battle... should not happens if called on spawn
				if ((npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) || (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE))
				{
					WalkInfo walk = new WalkInfo(routeId);
					
					if (npc.isDebug())
					{
						walk._lastActionTime = System.currentTimeMillis();
					}
					
					L2NpcWalkerNode node = walk.getCurrentNode();
					
					// adjust next waypoint, if NPC spawns at first waypoint
					if ((npc.getX() == node.getMoveX()) && (npc.getY() == node.getMoveY()))
					{
						walk.calculateNextNode(npc);
						node = walk.getCurrentNode();
						npc.sendDebugMessage("Route id " + routeId + ", spawn point is same with first waypoint, adjusted to next");
					}
					
					if (!npc.isInsideRadius(node.getMoveX(), node.getMoveY(), node.getMoveZ(), 3000, true, false))
					{
						npc.sendDebugMessage("Route id " + routeId + ", NPC is too far from starting point, walking will no start");
						return;
					}
					
					npc.sendDebugMessage("Starting to move at route " + routeId);
					npc.setIsRunning(node.getRunning());
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(node.getMoveX(), node.getMoveY(), node.getMoveZ(), 0));
					walk._walkCheckTask = ThreadPoolManager.getInstance().scheduleAiAtFixedRate(new Runnable()
					{
						@Override
						public void run()
						{
							startMoving(npc, routeId);
						}
					}, 60000, 60000); // start walk check task, for resuming walk after fight
					
					npc.getKnownList().startTrackingTask();
					
					_activeRoutes.put(npc.getObjectId(), walk); // register route
				}
				else
				{
					npc.sendDebugMessage("Trying to start move at route " + routeId + ", but cannot now, scheduled");
					ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
					{
						@Override
						public void run()
						{
							startMoving(npc, routeId);
						}
					}, 60000);
				}
			}
			else
			// walk was stopped due to some reason (arrived to node, script action, fight or something else), resume it
			{
				if ((npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_ACTIVE) || (npc.getAI().getIntention() == CtrlIntention.AI_INTENTION_IDLE))
				{
					WalkInfo walk = _activeRoutes.get(npc.getObjectId());
					
					// Prevent call simultaneously from scheduled task and onArrived() or temporarily stop walking for resuming in future
					if (walk._blocked || walk._suspended)
					{
						npc.sendDebugMessage("Trying continue to move at route " + routeId + ", but cannot now (operation is blocked)");
						return;
					}
					
					walk._blocked = true;
					
					L2NpcWalkerNode node = walk.getCurrentNode();
					npc.sendDebugMessage("Route id: " + routeId + ", continue to node " + walk._currentNode);
					npc.setIsRunning(node.getRunning());
					npc.getAI().setIntention(CtrlIntention.AI_INTENTION_MOVE_TO, new L2CharPosition(node.getMoveX(), node.getMoveY(), node.getMoveZ(), 0));
					walk._blocked = false;
					walk._stoppedByAttack = false;
				}
				else
				{
					npc.sendDebugMessage("Trying continue to move at route " + routeId + ", but cannot now (wrong AI state)");
				}
			}
		}
	}
	
	/**
	 * Cancel NPC moving permanently
	 * @param npc NPC to cancel
	 */
	public synchronized void cancelMoving(L2Npc npc)
	{
		if (_activeRoutes.containsKey(npc.getObjectId()))
		{
			final WalkInfo walk = _activeRoutes.remove(npc.getObjectId());
			walk._walkCheckTask.cancel(true);
			npc.getKnownList().stopTrackingTask();
		}
	}
	
	/**
	 * Resumes previously stopped moving
	 * @param npc NPC to resume
	 */
	public void resumeMoving(final L2Npc npc)
	{
		if (!_activeRoutes.containsKey(npc.getObjectId()))
		{
			return;
		}
		
		WalkInfo walk = _activeRoutes.get(npc.getObjectId());
		walk._suspended = false;
		walk._stoppedByAttack = false;
		startMoving(npc, walk.getRoute().getId());
	}
	
	/**
	 * Pause NPC moving until it will be resumed
	 * @param npc NPC to pause moving
	 * @param suspend {@code true} if moving was temporarily suspended for some reasons of AI-controlling script
	 * @param stoppedByAttack {@code true} if moving was suspended because of NPC was attacked or desired to attack
	 */
	public void stopMoving(L2Npc npc, boolean suspend, boolean stoppedByAttack)
	{
		L2MonsterInstance monster = null;
		
		if (npc.isMonster())
		{
			if (((L2MonsterInstance) npc).getLeader() == null)
			{
				monster = (L2MonsterInstance) npc;
			}
			else
			{
				monster = ((L2MonsterInstance) npc).getLeader();
			}
		}
		
		if (((monster != null) && !isRegistered(monster)) || !isRegistered(npc))
		{
			return;
		}
		
		WalkInfo walk = monster != null ? _activeRoutes.get(monster.getObjectId()) : _activeRoutes.get(npc.getObjectId());
		
		walk._suspended = suspend;
		walk._stoppedByAttack = stoppedByAttack;
		
		if (monster != null)
		{
			monster.stopMove(null);
			monster.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
		else
		{
			npc.stopMove(null);
			npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ACTIVE);
		}
	}
	
	/**
	 * Manage "node arriving"-related tasks: schedule move to next node; send ON_NODE_ARRIVED event to Quest script
	 * @param npc NPC to manage
	 */
	public void onArrived(final L2Npc npc)
	{
		if (_activeRoutes.containsKey(npc.getObjectId()))
		{
			// Notify quest
			if (npc.getTemplate().getEventQuests(Quest.QuestEventType.ON_NODE_ARRIVED) != null)
			{
				for (Quest quest : npc.getTemplate().getEventQuests(Quest.QuestEventType.ON_NODE_ARRIVED))
				{
					quest.notifyNodeArrived(npc);
				}
			}
			
			WalkInfo walk = _activeRoutes.get(npc.getObjectId());
			
			// Opposite should not happen... but happens sometime
			if ((walk._currentNode >= 0) && (walk._currentNode < walk.getRoute().getNodesCount()))
			{
				L2NpcWalkerNode node = walk.getRoute().getNodeList().get(walk._currentNode);
				if (npc.isInsideRadius(node.getMoveX(), node.getMoveY(), node.getMoveZ(), 10, false, false))
				{
					npc.sendDebugMessage("Route id: " + walk.getRoute().getId() + ", arrived to node " + walk._currentNode);
					npc.sendDebugMessage("Done in " + ((System.currentTimeMillis() - walk._lastActionTime) / 1000) + " s.");
					walk.calculateNextNode(npc);
					int delay = walk.getCurrentNode().getDelay();
					walk._blocked = true; // prevents to be ran from walk check task, if there is delay in this node.
					
					if (npc.isDebug())
					{
						walk._lastActionTime = System.currentTimeMillis();
					}
					ThreadPoolManager.getInstance().scheduleGeneral(new ArrivedTask(npc, walk), 100 + (delay * 1000L));
				}
			}
		}
	}
	
	/**
	 * Manage "on death"-related tasks: permanently cancel moving of died NPC
	 * @param npc NPC to manage
	 */
	public void onDeath(L2Npc npc)
	{
		cancelMoving(npc);
	}
	
	/**
	 * Manage "on spawn"-related tasks: start NPC moving, if there is route attached to its spawn point
	 * @param npc NPC to manage
	 */
	public void onSpawn(L2Npc npc)
	{
		if (_routesToAttach.containsKey(npc.getNpcId()))
		{
			int routeId = _routesToAttach.get(npc.getNpcId()).getRouteId(npc);
			
			if (routeId > 0)
			{
				startMoving(npc, routeId);
			}
		}
	}
	
	private class ArrivedTask implements Runnable
	{
		WalkInfo _walk;
		L2Npc _npc;
		
		public ArrivedTask(L2Npc npc, WalkInfo walk)
		{
			_npc = npc;
			_walk = walk;
		}
		
		@Override
		public void run()
		{
			_walk._blocked = false;
			startMoving(_npc, _walk.getRoute().getId());
		}
	}
	
	public static final WalkingManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final WalkingManager _instance = new WalkingManager();
	}
}