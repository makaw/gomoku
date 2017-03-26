/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
*
* Tłumaczenia (singleton)
* 
* @author Maciej Kawecki
* 
*/
public class Lang {

   /** Instancja klasy */
   private static Lang instance = new Lang();
   
   /** Lokalizacja - indeks */
   private int localeIndex = IConf.DEFAULT_LOCALE_INDEX;
   /** Lokalizacja */  
   private Locale locale = IConf.LOCALES[localeIndex];
   /** Lista tłumaczeń */
   private ResourceBundle bundle;   
   
   
   private Lang() {
	   
	 Locale.setDefault(locale);
	 bundle = ResourceBundle.getBundle("ApplicationMessages");  
	   
   }
   
   

   /**
    * Ustawia wskazaną lokalizację
    * @param index Indeks lokalizacji
    * @return True jeżeli zmieniono
    */
   public static boolean setLocale(int index) {
 	  
 	 if (index != instance.localeIndex)  
 	 try { 
 	   instance.localeIndex = index;
 	   instance.locale = IConf.LOCALES[index];
 	   Locale.setDefault(instance.locale);
 	   instance.bundle = ResourceBundle.getBundle("ApplicationMessages");
 	   return true;
 	 }
 	 catch (IndexOutOfBoundsException e) {
 	   System.err.println(e);
 	 }
 	 
 	 return false;
 	 
   }
   
   
   public static int getLocaleIndex() {
 	 return instance.localeIndex;
   }
   

   
   /**
    * Zwraca tłumaczenie z zawartymi parametrami
    * @param key Klucz frazy
    * @param params Parametry
    * @return Tłumaczenie
    */
   public static String get(String key, Object... params) {
 	  
	 try {    
 	   return MessageFormat.format(instance.bundle.getString(key), params);
	 }
	 catch (MissingResourceException e) {
	   System.err.println(e);
	   return key;
	 }
 	  
   }
      
   
   /**
    * Zwraca symbol lokalizacji (język + kraj)
    * @return Symbol lokalizacji
    */
   public static String getLocaleSymbol() {
	 
	 return instance.locale.getLanguage() + "_" + instance.locale.getCountry();
	   
   }
	
	
}
