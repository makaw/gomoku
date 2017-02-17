/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * Szablon obiektu timera wysyłającego pingi do serwera
 * 
 * @author Maciej Kawecki
 * 
 */
public class Ping extends Timer {
    
  /** Co ile sekund wysyłać ping do serwera */  
  private final static double PING_SEC = 0.5;  
  /** true jeżeli wystąpił błąd przy pingowaniu serwera */
  private boolean pingOut;
  /** Referencja do obiektu klienta */
  private final Client client;
  
  
  /**
   * Konstruktor, inicjalizacja wewnętrznych atrybutów
   * @param client Referencja do obiektu klienta
   */
  public Ping(Client client) {
      
    this.client = client;  
      
  }
  
  /**
   * Metoda uruchamiająca cykliczne zadanie (ping do serwera)
   */
  public void startPinging() {
      
    scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        
         // wysyła ping na serwer
         try {
             
           client.sendCommand(new Command(Command.CMD_PING));
             
         } catch (Exception e) {
             
            pingOut = true;   
            client.endGame();
             
         }
         
     }
    }, Math.round(PING_SEC*1000), Math.round(PING_SEC*1000));   
  
  }
  

  /**
   * Metoda informuje czy już wystąpił brak odpowiedzi na ping
   * @return true jeżeli już wystąpił brak odpowiedzi na ping
   */
  public boolean getPingOut() {
      
    return pingOut;  
      
  }
  
  /**
   * Metoda zatrzymuje pingowanie
   */
  public void stopPinging() {
      
    cancel();
    purge();  
      
  }
    
  
  
  

}
