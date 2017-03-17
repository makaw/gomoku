/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;


/**
 *
 * Szablon obiektu opisującego stan gry
 * 
 * @author Maciej Kawecki
 * 
 */
public enum GameState {
    

   /** Rozgrywka trwa */
   RUN, 
   /**Rozpoczęcie nowej gry */
   RESTART, 
   /** Oczekiwanie na nową grę */
   WAIT;
    
   /** Adres IP serwera */
   private String serverIP = "";
   
   
   public void setServerIP(String serverIP) {
	   this.serverIP = serverIP;
   }

   public String getServerIP() {
       return serverIP;
   }
   
   
}
