package utils;

import java.awt.Image;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public final class ResourceLoader {
	
	// PROPERTIES
	private static String locale = "en";
	private static String cardSet;
	
	// METHODS
	/**
	 * Changes locale of the application.
	 * @param id	The ID of the language.
	 */
	public static void setLocale(int id) {
		switch (id) {
			case 0: locale = "en"; break;
			case 1: locale = "pl"; break;
			case 2: locale = "ja"; break;
			default: locale = "en"; break;
		}
		 System.out.println("[LOADER] Locale set to: " + locale);
	}
	
	public static String getCardSet() {
		return cardSet;
	}
	
	public static void setCardSet(String set) {
		cardSet = set;
	}
	
	/**
	 * Returns a string corresponding to key in a set locale. 
	 * If key is not found returns an error string.
	 * @param key	The provided key.
	 * @return		The requested string.
	 */
	public static String localize(String key) {
		String value;
		try {
			ResourceBundle strings = ResourceBundle.getBundle("i18n/strings", new Locale(locale));
			value = strings.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
			value = "[?missing-" + locale + "?]";
		}
        return value;
    }
	
	/**
	 * Finds and loads a requested image file.
	 * @param uri	The location of the file.
	 * @return
	 */
	public static ImageIcon requestImage(String uri, int width, int height) {
		try {
			Image img = ImageIO.read(ResourceLoader.class.getClassLoader().getResource(uri));
			Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			ImageIcon scaledIcon = new ImageIcon(scaled);
			return scaledIcon;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

