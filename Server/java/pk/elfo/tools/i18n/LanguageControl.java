package pk.elfo.tools.i18n;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

/**
 * PkElfo
 */
 
public class LanguageControl extends Control
{
	public static final String LANGUAGES_DIRECTORY = "../languages/";
	
	public static final LanguageControl INSTANCE = new LanguageControl();
	
	/**
	 * prevent instancing, allows sub-classing
	 */
	protected LanguageControl()
	{
	}
	
	@Override
	public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IOException
	{
		if ((baseName == null) || (locale == null) || (format == null) || (loader == null))
		{
			throw new NullPointerException();
		}
		ResourceBundle bundle = null;
		if (format.equals("java.properties"))
		{
			format = "properties";
			String bundleName = toBundleName(baseName, locale);
			String resourceName = LANGUAGES_DIRECTORY + toResourceName(bundleName, format);
			
			try (FileInputStream fis = new FileInputStream(resourceName);
				BufferedInputStream bis = new BufferedInputStream(fis))
			{
				bundle = new PropertyResourceBundle(bis);
			}
		}
		return bundle;
	}
}