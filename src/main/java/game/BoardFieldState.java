/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

/**
 *
 * Stan pola planszy
 * 
 * @author Maciej Kawecki
 * 
 */
public enum BoardFieldState {
    

    /** Puste pole */
   EMPTY('0'), 
	   
   /** Biały kamień */
   WHITE('W'), 
	   
   /** Czarny kamień */
   BLACK('B');
	
	
   private final char code;
   

   
   BoardFieldState(char code) {
	   this.code = code;
   }
   
   
   public char getCode() {
	   return code;
   }
   
   
   /**
    * Kolor kamieni przeciwnika
    * @return Kolor przeciwnika
    */
   public BoardFieldState getOpposite() {
	   
	  switch (this) {
	  
	    default: return EMPTY;
	    case WHITE : return BLACK;
	    case BLACK : return WHITE;
	
	  }
	   	   
   }
   
   
   /**
    * Nazwa koloru/stanu
    * @return Nazwa
    */
   public String getName() {
	   
	   switch (this) {
	     default: return "-";
	     case WHITE: return "bia\u0142e";
	     case BLACK: return "czarne";	  
	   }
	   
   }
   
	
   public String toString() {
	   
	 return String.valueOf(code);
	   
   }
   
   
   
}