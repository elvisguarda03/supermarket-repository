package br.com.guacom.supermarket.internationalization;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class InternationalizationUtil {
	private static ResourceBundle bundle;
	private static final String ARCHIVE = "messages";
	
	public static String getBundle(String key) {
		if(key == null)
			throw new IllegalArgumentException("Campo vázio!");
		Path path = Paths.get(ARCHIVE);
		try {
			bundle = ResourceBundle.getBundle(path.toString(), Locale.US);
			return bundle.getString(key);
		} catch (MissingResourceException ex) {
			System.err.println(ex.getClass().getSimpleName() + " inválido!");
		}
		return null;
	}
}
