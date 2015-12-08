package pk.elfo.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import pk.elfo.gameserver.model.PartyMatchWaitingList;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public class ExListPartyMatchingWaitingRoom extends L2GameServerPacket
{
	private final L2PcInstance _activeChar;
	// private final int _page;
	private final int _minlvl;
	private final int _maxlvl;
	private final int _mode;
	private final List<L2PcInstance> _members;
	
	public ExListPartyMatchingWaitingRoom(L2PcInstance player, int page, int minlvl, int maxlvl, int mode)
	{
		_activeChar = player;
		// _page = page;
		_minlvl = minlvl;
		_maxlvl = maxlvl;
		_mode = mode;
		_members = new ArrayList<>();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x36);
		if (_mode == 0)
		{
			writeD(0);
			writeD(0);
			return;
		}
		
		for (L2PcInstance cha : PartyMatchWaitingList.getInstance().getPlayers())
		{
			if ((cha == null) || (cha == _activeChar))
			{
				continue;
			}
			
			if (!cha.isPartyWaiting())
			{
				PartyMatchWaitingList.getInstance().removePlayer(cha);
				continue;
			}
			
			else if ((cha.getLevel() < _minlvl) || (cha.getLevel() > _maxlvl))
			{
				continue;
			}
			_members.add(cha);
		}
		
		writeD(0x01); // Page?
		writeD(_members.size());
		for (L2PcInstance member : _members)
		{
			writeS(member.getName());
			writeD(member.getActiveClass());
			writeD(member.getLevel());
		}
	}
}