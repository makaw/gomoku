/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;


import java.util.Observable;

/**
 *
 * Szablon obiektu służącego do przekazywania informacji pomiędzy wątkami 
 * przy wykorzystaniu wzorca "Obserwatora" (Observer)
 * 
 * @author Maciej Kawecki
 * 
 */
public class AppObserver extends Observable  {
    
   /** Klucz */ 
   protected String key;
   /** Przesyłany obiekt */
   protected Object object;
    
   
   /**
    * Metoda przesyłająca dany obiekt do obserwujących obiektów
    * @param key Opis(klucz) przesyłanego obiektu
    * @param object Przesyłany obiekt
    */
   public void sendObject(String key, Object object) {

      this.key = key;
      this.object = object;
      setChanged();
      notifyObservers(this);
      
   }
   
   
   public String getKey() {
       
     return key;  
       
   }
   
   
   public Object getObject() {
       
     return object;  
       
   }
   
 
    
}
