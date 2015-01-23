package vehicles.AirShipGludioGracia;

import pk.elfo.gameserver.ThreadPoolManager;
import pk.elfo.gameserver.instancemanager.AirShipManager;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.VehiclePathPoint;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2AirShipInstance;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.NpcStringId;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.clientpackets.Say2;
import pk.elfo.gameserver.network.serverpackets.NpcSay;

public class AirShipGludioGracia extends Quest implements Runnable
{
	private static final int[] CONTROLLERS =
	{
		32607,
		32609
	};
	
	private static final int GLUDIO_DOCK_ID = 10;
	private static final int GRACIA_DOCK_ID = 11;
	
	private static final Location OUST_GLUDIO = new Location(-149379, 255246, -80);
	private static final Location OUST_GRACIA = new Location(-186563, 243590, 2608);
	
	private static final VehiclePathPoint[] GLUDIO_TO_WARPGATE =
	{
		new VehiclePathPoint(-151202, 252556, 231),
		new VehiclePathPoint(-160403, 256144, 222),
		new VehiclePathPoint(-167874, 256731, -509, 0, 41035)
	// teleport: x,y,z,speed=0,heading
	};
	
	private static final VehiclePathPoint[] WARPGATE_TO_GRACIA =
	{
		new VehiclePathPoint(-169763, 254815, 282),
		new VehiclePathPoint(-171822, 250061, 425),
		new VehiclePathPoint(-172595, 247737, 398),
		new VehiclePathPoint(-174538, 246185, 39),
		new VehiclePathPoint(-179440, 243651, 1337),
		new VehiclePathPoint(-182601, 243957, 2739),
		new VehiclePathPoint(-184952, 245122, 2694),
		new VehiclePathPoint(-186936, 244563, 2617)
	};
	
	private static final VehiclePathPoint[] GRACIA_TO_WARPGATE =
	{
		new VehiclePathPoint(-187801, 244997, 2672),
		new VehiclePathPoint(-188520, 245932, 2465),
		new VehiclePathPoint(-189932, 245243, 1682),
		new VehiclePathPoint(-191192, 242969, 1523),
		new VehiclePathPoint(-190408, 239088, 1706),
		new VehiclePathPoint(-187475, 237113, 2768),
		new VehiclePathPoint(-184673, 238433, 2802),
		new VehiclePathPoint(-184524, 241119, 2816),
		new VehiclePathPoint(-182129, 243385, 2733),
		new VehiclePathPoint(-179440, 243651, 1337),
		new VehiclePathPoint(-174538, 246185, 39),
		new VehiclePathPoint(-172595, 247737, 398),
		new VehiclePathPoint(-171822, 250061, 425),
		new VehiclePathPoint(-169763, 254815, 282),
		new VehiclePathPoint(-168067, 256626, 343),
		new VehiclePathPoint(-157261, 255664, 221, 0, 64781)
	// teleport: x,y,z,speed=0,heading
	};
	
	private static final VehiclePathPoint[] WARPGATE_TO_GLUDIO =
	{
		new VehiclePathPoint(-153414, 255385, 221),
		new VehiclePathPoint(-149548, 258172, 221),
		new VehiclePathPoint(-146884, 257097, 221),
		new VehiclePathPoint(-146672, 254239, 221),
		new VehiclePathPoint(-147855, 252712, 206),
		new VehiclePathPoint(-149378, 252552, 198)
	};
	
	private final L2AirShipInstance _ship;
	private int _cycle = 0;
	
	private boolean _foundAtcGludio = false;
	private L2Npc _atcGludio = null;
	private boolean _foundAtcGracia = false;
	private L2Npc _atcGracia = null;
	
	public AirShipGludioGracia(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(CONTROLLERS);
		addFirstTalkId(CONTROLLERS);
		addTalkId(CONTROLLERS);
		
		_ship = AirShipManager.getInstance().getNewAirShip(-149378, 252552, 198, 33837);
		_ship.setOustLoc(OUST_GLUDIO);
		_ship.setInDock(GLUDIO_DOCK_ID);
		_ship.registerEngine(this);
		_ship.runEngine(60000);
	}
	
	private final void broadcastInGludio(NpcStringId npcString)
	{
		if (!_foundAtcGludio)
		{
			_foundAtcGludio = true;
			_atcGludio = findController();
		}
		if (_atcGludio != null)
		{
			_atcGludio.broadcastPacket(new NpcSay(_atcGludio.getObjectId(), Say2.NPC_SHOUT, _atcGludio.getNpcId(), npcString));
		}
	}
	
	private final void broadcastInGracia(NpcStringId npcStringId)
	{
		if (!_foundAtcGracia)
		{
			_foundAtcGracia = true;
			_atcGracia = findController();
		}
		if (_atcGracia != null)
		{
			_atcGracia.broadcastPacket(new NpcSay(_atcGracia.getObjectId(), Say2.NPC_SHOUT, _atcGracia.getNpcId(), npcStringId));
		}
	}
	
	private final L2Npc findController()
	{
		// check objects around the ship
		for (L2Object obj : L2World.getInstance().getVisibleObjects(_ship, 600))
		{
			if (obj.isNpc())
			{
				for (int id : CONTROLLERS)
				{
					if (((L2Npc) obj).getNpcId() == id)
					{
						return (L2Npc) obj;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public final String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		if (player.isTransformed())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_TRANSFORMED);
			return null;
		}
		else if (player.isParalyzed())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_PETRIFIED);
			return null;
		}
		else if (player.isDead() || player.isFakeDeath())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_DEAD);
			return null;
		}
		else if (player.isFishing())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_FISHING);
			return null;
		}
		else if (player.isInCombat())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_BATTLE);
			return null;
		}
		else if (player.isInDuel())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_IN_A_DUEL);
			return null;
		}
		else if (player.isSitting())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_SITTING);
			return null;
		}
		else if (player.isCastingNow())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_CASTING);
			return null;
		}
		else if (player.isCursedWeaponEquipped())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_CURSED_WEAPON_IS_EQUIPPED);
			return null;
		}
		else if (player.isCombatFlagEquipped())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_HOLDING_A_FLAG);
			return null;
		}
		else if (player.hasSummon() || player.isMounted())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_BOARD_AN_AIRSHIP_WHILE_A_PET_OR_A_SERVITOR_IS_SUMMONED);
			return null;
		}
		else if (_ship.isInDock() && _ship.isInsideRadius(player, 600, true, false))
		{
			_ship.addPassenger(player);
		}
		
		return null;
	}
	
	@Override
	public final String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return npc.getNpcId() + ".htm";
	}
	
	@Override
	public void run()
	{
		try
		{
			switch (_cycle)
			{
				case 0:
					broadcastInGludio(NpcStringId.THE_REGULARLY_SCHEDULED_AIRSHIP_THAT_FLIES_TO_THE_GRACIA_CONTINENT_HAS_DEPARTED);
					_ship.setInDock(0);
					_ship.executePath(GLUDIO_TO_WARPGATE);
					break;
				case 1:
					// _ship.teleToLocation(-167874, 256731, -509, 41035, false);
					_ship.setOustLoc(OUST_GRACIA);
					ThreadPoolManager.getInstance().scheduleGeneral(this, 5000);
					break;
				case 2:
					_ship.executePath(WARPGATE_TO_GRACIA);
					break;
				case 3:
					broadcastInGracia(NpcStringId.THE_REGULARLY_SCHEDULED_AIRSHIP_HAS_ARRIVED_IT_WILL_DEPART_FOR_THE_ADEN_CONTINENT_IN_1_MINUTE);
					_ship.setInDock(GRACIA_DOCK_ID);
					_ship.oustPlayers();
					ThreadPoolManager.getInstance().scheduleGeneral(this, 60000);
					break;
				case 4:
					broadcastInGracia(NpcStringId.THE_REGULARLY_SCHEDULED_AIRSHIP_THAT_FLIES_TO_THE_ADEN_CONTINENT_HAS_DEPARTED);
					_ship.setInDock(0);
					_ship.executePath(GRACIA_TO_WARPGATE);
					break;
				case 5:
					// _ship.teleToLocation(-157261, 255664, 221, 64781, false);
					_ship.setOustLoc(OUST_GLUDIO);
					ThreadPoolManager.getInstance().scheduleGeneral(this, 5000);
					break;
				case 6:
					_ship.executePath(WARPGATE_TO_GLUDIO);
					break;
				case 7:
					broadcastInGludio(NpcStringId.THE_REGULARLY_SCHEDULED_AIRSHIP_HAS_ARRIVED_IT_WILL_DEPART_FOR_THE_GRACIA_CONTINENT_IN_1_MINUTE);
					_ship.setInDock(GLUDIO_DOCK_ID);
					_ship.oustPlayers();
					ThreadPoolManager.getInstance().scheduleGeneral(this, 60000);
					break;
			}
			_cycle++;
			if (_cycle > 7)
			{
				_cycle = 0;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean unload(boolean removeFromList)
	{
		if (_ship != null)
		{
			_ship.oustPlayers();
			_ship.deleteMe();
		}
		return super.unload(removeFromList);
	}
	
	public static void main(String[] args)
	{
		new AirShipGludioGracia(-1, AirShipGludioGracia.class.getSimpleName(), "vehicles");
	}
}
