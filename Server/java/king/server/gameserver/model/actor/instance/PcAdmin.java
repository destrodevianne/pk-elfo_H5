package king.server.gameserver.model.actor.instance;

import king.server.Config;
import king.server.gameserver.model.actor.instance.L2PcInstance;
import king.server.gameserver.util.Util;

public class PcAdmin extends PcExtension
{
	private boolean _safeadmin = false;
	private String _adminConfirmCmd = null;
	private boolean _inCameraMode = false;
	
	public PcAdmin(L2PcInstance activeChar)
	{
		super(activeChar);
	}
	
	public void setIsSafeAdmin(boolean b)
	{
		_safeadmin = b;
	}
	
	public boolean isSafeAdmin()
	{
		return _safeadmin;
	}
	
	public boolean canUseAdminCommand()
	{
		if (Config.ENABLE_SAFE_ADMIN_PROTECTION && !getPlayer().getPcAdmin().isSafeAdmin())
		{
			_log.warning("Character " + getPlayer().getName() + "(" + getPlayer().getObjectId() + ") tentou usar comandos de admin.");
			punishUnSafeAdmin();
			return false;
		}
		return true;
	}
	
	public void punishUnSafeAdmin()
	{
		if (getPlayer() != null)
		{
			getPlayer().setAccessLevel(0);
			Util.handleIllegalPlayerAction(getPlayer(), "Voce nao esta autorizado a ter status de GM e sera punido. Tenha um bom dia ;]!", Config.SAFE_ADMIN_PUNISH);
		}
	}
	
	public String getAdminConfirmCmd()
	{
		return _adminConfirmCmd;
	}
	
	public void setAdminConfirmCmd(String adminConfirmCmd)
	{
		_adminConfirmCmd = adminConfirmCmd;
	}
	
	public void setCameraMode(boolean val)
	{
		_inCameraMode = val;
	}
	
	public boolean inCameraMode()
	{
		return _inCameraMode;
	}
}