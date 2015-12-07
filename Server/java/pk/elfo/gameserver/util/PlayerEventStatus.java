package pk.elfo.gameserver.util;

import java.util.List;

import pk.elfo.gameserver.instancemanager.InstanceManager;
import pk.elfo.gameserver.model.Location;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;
import javolution.util.FastList;

public class PlayerEventStatus
{
	public L2PcInstance player = null;
	public Location initLoc = new Location(0, 0, 0);
	public int initInstanceId = 0;
	public int initKarma = 0;
	public int initPvpKills = 0;
	public int initPkKills = 0;
	public String initTitle = "";
	public List<L2PcInstance> kills = new FastList<>();
	public boolean eventSitForced = false;
	
	public PlayerEventStatus(L2PcInstance player)
	{
		this.player = player;
		initLoc = new Location(player.getX(), player.getY(), player.getZ(), player.getHeading());
		initInstanceId = player.getInstanceId();
		initKarma = player.getKarma();
		initPvpKills = player.getPvpKills();
		initPkKills = player.getPkKills();
		initTitle = player.getTitle();
	}
	
	public void restoreInits()
	{
		player.teleToLocation(initLoc, true);
		if ((initInstanceId > 0) && (InstanceManager.getInstance().getInstance(initInstanceId) != null))
		{
			player.setInstanceId(initInstanceId);
		}
		player.setKarma(initKarma);
		player.setPvpKills(initPvpKills);
		player.setPkKills(initPkKills);
		player.setTitle(initTitle);
	}
}