/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * Szablon obiektu timera, wysyłającego pingi do serwera
 * 
 * @author Maciej Kawecki
 * 
 */
public class Ping extends Timer {
    
  /** Co ile sekund wysyłać ping */  
  private final static double PING_SEC = 0.5;  
  /** true jeżeli wystąpił błąd przy pingowaniu */
  private boolean pingOut;
  /** Ref. do klienta */
  private final Client client;
  /** Ref. do serwera */
  private final Server server;
  /** Nr klienta */
  private final int clientNo;
  
  /**
   * Konstruktor (ping klienta do serwera)
   * @param client Ref. do klienta
   */
  public Ping(Client client) {
      
    this.client = client;  
    this.server = null;
    this.clientNo = 0;
      
  }
  
  
  /**
   * Konstruktor (ping serwera do klienta)
   * @param server Ref. do serwera
   * @param clientNo Nr klienta
   */
  public Ping(Server server, int clientNo) {
	  
	this.server = server;
	this.clientNo = clientNo;
	this.client = null;
	  
  }
  
  
  /**
   * Metoda uruchamiająca cykliczne zadanie (ping do serwera)
   */
  public void startPinging() {
      
    scheduleAtFixedRate(new TimerTask() {
    @Override
    public void run() {
        
       // wysyła ping na serwer
       if (client != null) {	
         try {
             
           client.sendCommand(new Command(Command.CMD_PING));
             
         } catch (Exception e) {
             
            pingOut = true;   
            client.endGame();
             
         }
       }
       // wysyła ping do klienta
       else if (server != null) {
    	   
    	 ObjectOutputStream output = server.getOutputStream(clientNo);  
    	   
    	 try {
    		 
            output.writeObject(new Command(Command.CMD_START));
            output.flush();
            
         } catch (IOException ex) {
        	 
        	pingOut = true; 
            server.getServerSpy().sendObject("ping-out", clientNo);
            
         }      	       	   
       }
         
     }
    }, Math.round(PING_SEC*1000), Math.round(PING_SEC*1000));   
  
  }
  

  public boolean isPingOut() {
      
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
