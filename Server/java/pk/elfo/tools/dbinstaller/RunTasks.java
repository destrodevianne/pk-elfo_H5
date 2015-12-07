package pk.elfo.tools.dbinstaller;

import java.io.File;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import pk.elfo.tools.dbinstaller.util.mysql.DBDumper;
import pk.elfo.tools.dbinstaller.util.mysql.ScriptExecutor;
import pk.elfo.util.file.filter.SQLFilter;

/**
 * PkElfo
 */
 
public class RunTasks extends Thread
{
	DBOutputInterface _frame;
	boolean _cleanInstall;
	String _db;
	String _sqlDir;
	String _cleanUpFile;
	
	public RunTasks(DBOutputInterface frame, String db, String sqlDir, String cleanUpFile, boolean cleanInstall)
	{
		_frame = frame;
		_db = db;
		_cleanInstall = cleanInstall;
		_sqlDir = sqlDir;
		_cleanUpFile = cleanUpFile;
	}
	
	@Override
	public void run()
	{
		new DBDumper(_frame, _db);
		ScriptExecutor exec = new ScriptExecutor(_frame);
		
		File clnFile = new File(_cleanUpFile);
		File updDir = new File(_sqlDir, "updates");
		File[] files = updDir.listFiles(new SQLFilter());
		
		Preferences prefs = Preferences.userRoot();
		
		if (_cleanInstall)
		{
			if (clnFile.exists())
			{
				_frame.appendToProgressArea("Limpeza da DataBase...");
				exec.execSqlFile(clnFile);
				_frame.appendToProgressArea("Database Limpa!");
			}
			else
			{
				_frame.appendToProgressArea("Limpeza do script da DataBase nao efetuado!");
			}
			
			if (updDir.exists())
			{
				StringBuilder sb = new StringBuilder();
				for (File cf : files)
				{
					sb.append(cf.getName() + ';');
				}
				prefs.put(_db + "_upd", sb.toString());
			}
		}
		else
		{
			if (!_cleanInstall && updDir.exists())
			{
				_frame.appendToProgressArea("Instalando Atualizacoes...");
				
				for (File cf : files)
				{
					if (!prefs.get(_db + "_upd", "").contains(cf.getName()))
					{
						exec.execSqlFile(cf, true);
						prefs.put(_db + "_upd", prefs.get(_db + "_upd", "") + cf.getName() + ";");
					}
				}
				_frame.appendToProgressArea("Instalacao da atualizacao da Database!");
			}
		}
		
		_frame.appendToProgressArea("Instalando conteudo da Database...");
		exec.execSqlBatch(new File(_sqlDir));
		_frame.appendToProgressArea("Database Instalacao completa!");
		
		File cusDir = new File(_sqlDir, "custom");
		if (cusDir.exists())
		{
			int ch = _frame.requestConfirm("Install Custom", "Voce quer instalar as tabelas personalizadas?", JOptionPane.YES_NO_OPTION);
			if (ch == 0)
			{
				_frame.appendToProgressArea("Instalando Tabelas personalizadas...");
				exec.execSqlBatch(cusDir);
				_frame.appendToProgressArea("Tabelas personalizadas instaladas!");
			}
		}
		
		File modDir = new File(_sqlDir, "mods");
		if (modDir.exists())
		{
			int ch = _frame.requestConfirm("Install Mods", "Voce quer instalar a tabela mod?", JOptionPane.YES_NO_OPTION);
			if (ch == 0)
			{
				_frame.appendToProgressArea("Instalando Tabela Mods...");
				exec.execSqlBatch(modDir);
				_frame.appendToProgressArea("Tabela Mods instalada");
			}
		}
		
		try
		{
			_frame.getConnection().close();
		}
		catch (SQLException e)
		{
			JOptionPane.showMessageDialog(null, "Cannot close MySQL Connection: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
		}
		
		_frame.setFrameVisible(false);
		_frame.showMessage("Feito!", "Database Instalacao Completa!", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}
}