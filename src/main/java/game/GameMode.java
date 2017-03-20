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
	
	
   public String toString() {
	   
	 switch (this) {
	 	   
	   case SINGLE_GAME: return "Rozpocznij gr\u0119 z komputerem";
	   case HOTSEAT_GAME: return "Rozpocznij gr\u0119 2-osobow\u0105 (hot-seat)";
	   case NETWORK_GAME: return "Do\u0142\u0105cz do gry sieciowej (jako klient)";
	 
	 }
	   
	 return DEFAULT.toString();
	   
   }
   
   
   public int getCode() {  return code; }
   
   
   /**
    * Metoda zwraca odpowiedni obiekt dla kodu
    * @param Kod
    * @return Obiekt stanu
    */
   public static GameMode get(int code) {
       
     for(GameMode e : values()) if(e.getCode() == code) return e;
     
     return null;
        
   }      
   
}
