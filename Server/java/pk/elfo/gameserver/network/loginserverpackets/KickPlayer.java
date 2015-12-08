package pk.elfo.gameserver.network.loginserverpackets;

import pk.elfo.util.network.BaseRecievePacket;

public class KickPlayer extends BaseRecievePacket
{
	
	private final String _account;
	
	/**
	 * @param decrypt
	 */
	public KickPlayer(byte[] decrypt)
	{
		super(decrypt);
		_account = readS();
	}
	
	/**
	 * @return Returns the account.
	 */
	public String getAccount()
	{
		return _account;
	}
}