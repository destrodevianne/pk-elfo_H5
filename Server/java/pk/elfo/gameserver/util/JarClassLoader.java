package pk.elfo.gameserver.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This is a class loader for the dynamic extensions used by DynamicExtension class.
 */
public class JarClassLoader extends ClassLoader
{
	private static Logger _log = Logger.getLogger(JarClassLoader.class.getCanonicalName());
	private final HashSet<String> _jars = new HashSet<>();
	
	public void addJarFile(String filename)
	{
		_jars.add(filename);
	}
	
	@Override
	public Class<?> findClass(String name) throws ClassNotFoundException
	{
		try
		{
			byte[] b = loadClassData(name);
			return defineClass(name, b, 0, b.length);
		}
		catch (Exception e)
		{
			throw new ClassNotFoundException(name);
		}
	}
	
	private byte[] loadClassData(String name) throws IOException
	{
		byte[] classData = null;
		final String fileName = name.replace('.', '/') + ".class";
		for (String jarFile : _jars)
		{
			final File file = new File(jarFile);
			try (ZipFile zipFile = new ZipFile(file);)
			{
				final ZipEntry entry = zipFile.getEntry(fileName);
				if (entry == null)
				{
					continue;
				}
				classData = new byte[(int) entry.getSize()];
				try (DataInputStream zipStream = new DataInputStream(zipFile.getInputStream(entry)))
				{
					zipStream.readFully(classData, 0, (int) entry.getSize());
				}
				break;
			}
			catch (IOException e)
			{
				_log.log(Level.WARNING, jarFile + ": " + e.getMessage(), e);
				continue;
			}
		}
		if (classData == null)
		{
			throw new IOException("class not found in " + _jars);
		}
		return classData;
	}
}