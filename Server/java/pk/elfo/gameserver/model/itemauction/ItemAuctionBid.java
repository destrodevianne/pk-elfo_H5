package pk.elfo.gameserver.model.itemauction;

import pk.elfo.gameserver.model.L2World;
import pk.elfo.gameserver.model.actor.instance.L2PcInstance;

public final class ItemAuctionBid
{
	private final int _playerObjId;
	private long _lastBid;
	
	public ItemAuctionBid(final int playerObjId, final long lastBid)
	{
		_playerObjId = playerObjId;
		_lastBid = lastBid;
	}
	
	public final int getPlayerObjId()
	{
		return _playerObjId;
	}
	
	public final long getLastBid()
	{
		return _lastBid;
	}
	
	final void setLastBid(final long lastBid)
	{
		_lastBid = lastBid;
	}
	
	final void cancelBid()
	{
		_lastBid = -1;
	}
	
	final boolean isCanceled()
	{
		return _lastBid <= 0;
	}
	
	final L2PcInstance getPlayer()
	{
		return L2World.getInstance().getPlayer(_playerObjId);
	}
}