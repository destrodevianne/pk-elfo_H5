package pk.elfo.gameserver.network.gameserverpackets;

import java.security.interfaces.RSAPublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import pk.elfo.util.network.BaseSendablePacket;

public class BlowFishKey extends BaseSendablePacket
{
	private static Logger _log = Logger.getLogger(BlowFishKey.class.getName());
	
	/**
	 * @param blowfishKey
	 * @param publicKey
	 */
	public BlowFishKey(byte[] blowfishKey, RSAPublicKey publicKey)
	{
		writeC(0x00);
		try
		{
			final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encrypted = rsaCipher.doFinal(blowfishKey);
			writeD(encrypted.length);
			writeB(encrypted);
		}
		catch (Exception e)
		{
			_log.log(Level.SEVERE, "Error While encrypting blowfish key for transmision (Crypt error): " + e.getMessage(), e);
		}
	}
	
	@Override
	public byte[] getContent()
	{
		return getBytes();
	}
}