package pk.elfo.tools.dbinstaller;

import java.awt.HeadlessException;

import javax.swing.UIManager;

import pk.elfo.tools.dbinstaller.console.DBInstallerConsole;
import pk.elfo.tools.dbinstaller.gui.DBConfigGUI;

/**
 * Contains main class for Database Installer If system doesn't support the graphical UI, start the installer in console mode. PkElfo
 */
 
public class LauncherCS
{
	public static void main(String[] args)
	{
		String mode = "l2jcs";
		String dir = "../sql/community/";
		String cleanUp = "cs_cleanup.sql";
		
		try
		{
			// Set OS Look And Feel
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		
		try
		{
			new DBConfigGUI(mode, dir, cleanUp);
		}
		catch (HeadlessException e)
		{
			new DBInstallerConsole(mode, dir, cleanUp);
		}
	}
}