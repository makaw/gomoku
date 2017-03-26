/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gomoku.Lang;

/**
 *
 * Szablon obiektu opisującego stan gry
 * 
 * @author Maciej Kawecki
 * 
 */
public enum GameMode {
    

    /** Gracz kontra komputer */
   SINGLE_GAME(1), 
	   
   /** Gracz kontra gracz (hot-seat) */
   HOTSEAT_GAME(2), 
	   
   /** Gracz kontra gracz (gra sieciowa) */
   NETWORK_GAME(3);
	
	
   private final int code;
   
   public static final GameMode DEFAULT = SINGLE_GAME;
   
   
   GameMode(int code) {
	   this.code = code;
   }
   
   
   public boolean isEnabled() {
	   return true;
   }
	
	
   @Override
   public String toString() {
	   
	 switch (this) {
	 	   
	   case SINGLE_GAME: return Lang.get("StartSingleGame");
	   case HOTSEAT_GAME: return Lang.get("StartHotSeatGame");
	   case NETWORK_GAME: return Lang.get("StartNetworkGame");
	 
	 }
	   
	 return DEFAULT.toString();
	   
   }
   
   
   public int getCode() {  return code; }
   
   
   /**
    * Metoda zwraca odpowiedni obiekt dla kodu
    * @param code Kod
    * @return Obiekt stanu
    */
   public static GameMode get(int code) {
       
     for(GameMode e : values()) if(e.getCode() == code) return e;
     
     return null;
        
   }      
   
}
