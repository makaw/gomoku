/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.util.Observable;
import network.Client;

/**
 *
 * Szablon obiektu służącego do przekazywania informacji nt. zmiany stanu gry, 
 * przy wykorzystaniu mechanizmu "Obserwatora" (Observer); służy do komunikacji z wątkiem GUI
 * 
 * @author Maciej Kawecki
 * @see gui.GUI#restartGame(byte, java.lang.String) 
 * @see Game#update(java.util.Observable, java.lang.Object) 
 * 
 */
public class GameStateSpy extends Observable {
    
   protected Byte state;
   protected String serverIP;
 
   
   /**
    * Metoda używana przez wątek GUI, przesyła nową wartość stanu gry do obserwujących obiektów
    * @param state Nowy stan rozgrywki (trwa, restart, oczekiwanie)
    * @param serverIP Adres IP serwera gry
    */
   public void setState(Byte state, String serverIP) {

      this.state = state;
      this.serverIP = serverIP;
      setChanged();
      notifyObservers(this);
      
   }

   
   /**
    * Metoda używana przez wątek GUI, przesyła nową wartość stanu gry do obserwujących obiektów
    * @param state Nowy stan rozgrywki (trwa, restart, oczekiwanie)
    */
   public void setState(Byte state) {

      setState(state, "");
      
   }   
   
   
   /**
    * Metoda używana przez wątek GUI, przesyła treść wiadomości do obserwujących obiektów
    * @param message Treść wiadomości
    * @since 1.1
    */ 
   public void sendMessage(String message) {

      setChanged();
      notifyObservers(message);
      
   }   
   
 
    
}
