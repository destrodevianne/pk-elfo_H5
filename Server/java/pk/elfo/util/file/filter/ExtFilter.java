package pk.elfo.util.file.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * PkElfo
 */

public class ExtFilter implements FileFilter
{
	private final String _ext;
	
	public ExtFilter(String ext)
	{
		_ext = ext;
	}
	
	@Override
	public boolean accept(File f)
	{
		return f.getName().toLowerCase().endsWith(_ext);
	}
}