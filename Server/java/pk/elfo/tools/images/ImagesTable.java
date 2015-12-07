package pk.elfo.tools.images;

import java.util.Map;

import javax.swing.ImageIcon;

import javolution.util.FastMap;

/**
 * Usage of this class causes images to be loaded and kept in memory, and therefore should only be used by helper applications.<br>
 * Some icons from famfamfam (http://www.famfamfam.com/) credit *MUST* be given. PkElfo
 */
 
public class ImagesTable
{
	private static final Map<String, ImageIcon> IMAGES = new FastMap<>();
	
	public static final String IMAGES_DIRECTORY = "../images/";
	
	public static ImageIcon getImage(String name)
	{
		if (!IMAGES.containsKey(name))
		{
			IMAGES.put(name, new ImageIcon(IMAGES_DIRECTORY + name));
		}
		return IMAGES.get(name);
	}
}