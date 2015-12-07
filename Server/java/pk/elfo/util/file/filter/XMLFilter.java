package pk.elfo.util.file.filter;

import java.io.File;
import java.io.FileFilter;

/**
 * Specialized {@link FileFilter} class.<br>
 * Accepts files ending with ".xml" only. PkElfo
 */

public class XMLFilter implements FileFilter
{
	@Override
	public boolean accept(File f)
	{
		if ((f == null) || !f.isFile())
		{
			return false;
		}
		return f.getName().toLowerCase().endsWith(".xml");
	}
}