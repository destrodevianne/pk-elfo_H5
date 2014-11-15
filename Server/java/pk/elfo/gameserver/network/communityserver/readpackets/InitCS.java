package pk.elfo.gameserver.network.communityserver.readpackets;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

import org.netcon.BaseReadPacket;
import org.netcon.crypt.NewCrypt;

import pk.elfo.gameserver.network.communityserver.CommunityServerThread;
import pk.elfo.gameserver.network.communityserver.writepackets.BlowFishKey;
import pk.elfo.gameserver.network.communityserver.writepackets.GameServerAuth;
import pk.elfo.util.Rnd;

public final class InitCS extends BaseReadPacket
{
	protected static final Logger _log = Logger.getLogger(InitCS.class.getName());
	private final CommunityServerThread _cst;
	
	public InitCS(final byte[] data, final CommunityServerThread cst)
	{
		super(data);
		_cst = cst;
	}
	
	@Override
	public final void run()
	{
		final int length = super.readD();
		final byte[] key = super.readB(length);
		
		try
		{
			final KeyFactory kfac = KeyFactory.getInstance("RSA");
			final RSAPublicKeySpec kspec1 = new RSAPublicKeySpec(new BigInteger(key), RSAKeyGenParameterSpec.F4);
			final RSAPublicKey publicKey = (RSAPublicKey) kfac.generatePublic(kspec1);
			
			final byte[] privateKey = new byte[40];
			Rnd.nextBytes(privateKey);
			
			final Cipher rsaCipher = Cipher.getInstance("RSA/ECB/nopadding");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			final byte[] tempKey = rsaCipher.doFinal(privateKey);
			
			_cst.sendPacket(new BlowFishKey(tempKey), false);
			_cst.setCrypt(new NewCrypt(privateKey));
			_cst.sendPacket(new GameServerAuth(), false);
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "", e);
		}
	}
}