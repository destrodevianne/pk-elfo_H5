package instances.SecretArea;

import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import pk.elfo.gameserver.model.entity.Instance;
import pk.elfo.gameserver.model.instancezone.InstanceWorld;
import pk.elfo.gameserver.model.quest.Quest;
import pk.elfo.gameserver.network.SystemMessageId;

public class SecretArea extends Quest
{
	private static final int INSTANCE_ID = 117;
	// TODO Unharcode htmls.
	private static final String _ENTER = "<html><head><body>Soldier Ginby:<br>Hurry! Come back before anybody sees you!</body></html>";
	private static final String _EXIT = "<html><head><body>Shilen Priest Lelrikia:<br>Doomed creature, either you obey the power of Shilen or you fight.Regardless of your decision, the shadow of death will not simply fade away...</body></html>";
	private static final int GINBY = 32566;
	private static final int LELRIKIA = 32567;
	private static final int ENTER = 0;
	private static final int EXIT = 1;
	private static final Location[] TELEPORTS =
	{
		new Location(-23758, -8959, -5384),
		new Location(-185057, 242821, 1576)
	};
	
	protected void enterInstance(L2PcInstance player)
	{
		InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
		if (world != null)
		{
			if (world.getInstanceId() != INSTANCE_ID)
			{
				player.sendPacket(SystemMessageId.ALREADY_ENTERED_ANOTHER_INSTANCE_CANT_ENTER);
				return;
			}
			Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
			if (inst != null)
			{
				teleportPlayer(player, TELEPORTS[ENTER], world.getInstanceId());
			}
		}
		else
		{
			final int instanceId = InstanceManager.getInstance().createDynamicInstance("[028] Secret Area in the Keucereus Fortress.xml");
			world = new InstanceWorld();
			world.setInstanceId(INSTANCE_ID);
			world.setTemplateId(instanceId);
			world.setStatus(0);
			InstanceManager.getInstance().addWorld(world);
			
			world.addAllowed(player.getObjectId());
			teleportPlayer(player, TELEPORTS[ENTER], instanceId);
		}
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		if ((npc.getNpcId() == GINBY) && event.equalsIgnoreCase("enter"))
		{
			enterInstance(player);
			return _ENTER;
		}
		else if ((npc.getNpcId() == LELRIKIA) && event.equalsIgnoreCase("exit"))
		{
			teleportPlayer(player, TELEPORTS[EXIT], 0);
			return _EXIT;
		}
		return htmltext;
	}
	
	public SecretArea(int questId, String name, String descr)
	{
		super(questId, name, descr);
		
		addStartNpc(GINBY);
		addTalkId(GINBY);
		addTalkId(LELRIKIA);
	}
	
	public static void main(String[] args)
	{
		new SecretArea(-1, SecretArea.class.getSimpleName(), "instances");
	}
}