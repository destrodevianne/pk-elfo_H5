package instances.Disciple;

import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.instancezone.InstanceWorld;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.model.quest.QuestState;
import pk.elfo.gameserver.network.SystemMessageId;
import pk.elfo.gameserver.network.serverpackets.SystemMessage;

public class Disciple extends Quest
{
	private class DiSWorld extends InstanceWorld
	{
		public long[] storeTime =
		{
			0,
			0
		};
		
		public DiSWorld()
		{
		}
	}
	
	private static final String qn = "Disciple";
	private static final int INSTANCEID = 112;
	
	private static final int PROMISE = 32585;
	private static final int LEON = 32587;
	private static final int DOOR = 17240111;
	private static final int GATEKEEPER = 32657;
	
	protected int enterInstance(L2PcInstance player, String template, Location loc)
	{
		int instanceId = 0;
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		if (world != null)
		{
			if (!(world instanceof DiSWorld))
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER));
				return 0;
			}
			teleportPlayer(player, loc, world.getInstanceId());
			return world.getInstanceId();
		}
		else
		{
			instanceId = InstanceManager.getInstance().createDynamicInstance(template);
			world = new DiSWorld();
			world.setInstanceId(instanceId);
			world.setTemplateId(INSTANCEID);
			world.setStatus(0);
			((DiSWorld) world).storeTime[0] = System.currentTimeMillis();
			InstanceManager.getInstance().addWorld(world);
			_log.info("Disciple started " + template + " Instance: " + instanceId + " created by player: " + player.getName());
			teleportPlayer(player, loc, world.getInstanceId());
			world.addAllowed(player.getObjectId());
			return instanceId;
		}
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		QuestState st = player.getQuestState(qn);
		if (st == null)
		{
			st = newQuestState(player);
		}
		
		switch (npc.getNpcId())
		{
			case PROMISE:
				enterInstance(player, "[011] Disciple.xml", new Location(-89559, 216030, -7488));
				break;
			case LEON:
				InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
				world.removeAllowed(player.getObjectId());
				teleportPlayer(player, new Location(171782, -17612, -4901), 0);
				break;
			case GATEKEEPER:
				final InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
				if (tmpworld instanceof DiSWorld)
				{
					openDoor(DOOR, tmpworld.getInstanceId());
					for (int objId : tmpworld.getAllowed())
					{
						final L2PcInstance pl = L2World.getInstance().getPlayer(objId);
						if (pl != null)
						{
							pl.showQuestMovie(12);
							// pl.showQuestMovie(ExStartScenePlayer.SSQ_SEALING_EMPEROR_1ST);
						}
					}
				}
				break;
		}
		
		return "";
	}
	
	public Disciple(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(PROMISE);
		addTalkId(PROMISE);
		addTalkId(LEON);
		addTalkId(GATEKEEPER);
	}
	
	public static void main(String[] args)
	{
		new Disciple(-1, qn, "instances");
	}
}