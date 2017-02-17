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
public class GameState {
    

   /** Stała używana do oznaczenia stanu rozgrywki: trwa */
   public static final byte RUN = 0;
   /** Stała używana do oznaczenia stanu rozgrywki: rozpoczęcie nowej gry */
   public static final byte RESTART = 1;
   /** Stała używana do oznaczenia stanu rozgrywki: oczekiwanie na nową grę */
   public static final byte WAIT = 2;
    
   /** Stan gry */
   private final Byte state;
   /** IP serwera */
   private final String serverIP; 
   
   
   public GameState(Byte state, String serverIP) {
       
     this.state = state;
     this.serverIP = serverIP;
       
   }
   
   public GameState(Byte state) {
       
     this(state, "");  
       
   }
   

   public Byte getState() {
       return state;
   }

   public String getServerIP() {
       return serverIP;
   }
   
   
    
    
    
}
