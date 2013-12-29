package pk.elfo.gameserver.events.model;

import pk.elfo.gameserver.datatables.NpcTable;
import pk.elfo.gameserver.datatables.SpawnTable;
import pk.elfo.gameserver.events.container.NpcContainer;
import pk.elfo.gameserver.model.L2Object;
import pk.elfo.gameserver.model.L2Spawn;
import pk.elfo.gameserver.model.actor.L2Npc;
import pk.elfo.gameserver.model.actor.templates.L2NpcTemplate;
import pk.elfo.gameserver.network.serverpackets.MagicSkillLaunched;
import pk.elfo.gameserver.network.serverpackets.MagicSkillUse;
import javolution.util.FastList;

public class EventNpc
{
	
	private L2Npc _owner;
	private int _team;
	
	public int getTeam()
	{
		return _team;
	}
	
	public void setTeam(Integer _team)
	{
		this._team = _team;
	}
	
	public EventNpc(int x, int y, int z, int npcId, int instance)
	{
		_team = 0;
		final L2NpcTemplate template = NpcTable.getInstance().getTemplate(npcId);
		try
		{
			final L2Spawn spawn = new L2Spawn(template);
			spawn.setLocx(x);
			spawn.setLocy(y);
			spawn.setLocz(z);
			spawn.setInstanceId(instance);
			spawn.setAmount(1);
			spawn.setHeading(0);
			spawn.setRespawnDelay(1);
			_owner = spawn.doSpawn();
		}
		catch (Exception e)
		{
			System.out.println("Erro ao nascer o NPC: " + npcId);
		}
	}
	
	public void unspawn()
	{
		_owner.getSpawn().getLastSpawn().deleteMe();
		_owner.getSpawn().stopRespawn();
		SpawnTable.getInstance().deleteSpawn(_owner.getSpawn(), true);
		NpcContainer.getInstance().deleteNpc(this);
	}
	
	public int getId()
	{
		return _owner.getSpawn().getLastSpawn().getObjectId();
	}
	
	public void setTitle(String title)
	{
		_owner.getSpawn().getLastSpawn().setTitle(title);
	}
	
	public L2Npc getNpc()
	{
		return _owner.getSpawn().getLastSpawn();
	}
	
	public void broadcastStatusUpdate()
	{
		_owner.broadcastStatusUpdate();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		EventNpc other = (EventNpc) obj;
		if (_owner == null)
		{
			if (other._owner != null)
			{
				return false;
			}
		}
		else if (!_owner.equals(other._owner))
		{
			return false;
		}
		return true;
	}
	
	public void showBombEffect(FastList<EventPlayer> victims)
	{
		FastList<L2Object> temp = new FastList<>();
		
		for (EventPlayer victim : victims)
		{
			temp.add(victim.getOwner());
		}
		
		_owner.broadcastPacket(new MagicSkillUse(_owner, victims.head().getNext().getValue().getOwner(), 903, 1, 0, 0));
		
		_owner.broadcastPacket(new MagicSkillLaunched(_owner, 903, 1, temp.toArray(new L2Object[temp.size()])));
	}
	
	public void doDie()
	{
		_owner.doDie(_owner);
	}
}