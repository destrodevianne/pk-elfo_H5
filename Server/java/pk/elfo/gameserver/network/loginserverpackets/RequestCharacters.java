package pk.elfo.gameserver.network.loginserverpackets;

import pk.elfo.util.network.BaseRecievePacket;

public class RequestCharacters extends BaseRecievePacket
{
	private final String _account;
	
	public RequestCharacters(byte[] decrypt)
	{
		super(decrypt);
		_account = readS();
	}
	
	/**
	 * @return Return account name
	 */
	public String getAccount()
	{
		return _account;
	}
}