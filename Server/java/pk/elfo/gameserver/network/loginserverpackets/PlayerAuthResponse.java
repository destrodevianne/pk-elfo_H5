package pk.elfo.gameserver.network.loginserverpackets;

import pk.elfo.util.network.BaseRecievePacket;

public class PlayerAuthResponse extends BaseRecievePacket
{
	
	private final String _account;
	private final boolean _authed;
	
	/**
	 * @param decrypt
	 */
	public PlayerAuthResponse(byte[] decrypt)
	{
		super(decrypt);
		
		_account = readS();
		_authed = (readC() == 0 ? false : true);
	}
	
	/**
	 * @return Returns the account.
	 */
	public String getAccount()
	{
		return _account;
	}
	
	/**
	 * @return Returns the authed state.
	 */
	public boolean isAuthed()
	{
		return _authed;
	}
}