/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.util.Observable;


/**
 *
 * Szablon obiektu służącego do przekazywania informacji nt. zmiany stanu serwera 
 * przy wykorzystaniu mechanizmu "Obserwatora" (Observer)
 * 
 * @author Maciej Kawecki
 * 
 */
public class ServerSpy extends Observable {
  
  
   /**
   * Metoda używana przez wątki serwera, przesyła nowe wartości do obserwujących obiektów
   * @param restart czy robić restart serwera
   */
   public void setState(boolean restart) {

      setChanged();
      notifyObservers(this);
      
   }
    
    
}
