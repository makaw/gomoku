/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.util.LinkedList;
import java.util.List;


/**
 *
 * TO_DO! 
 * 
 */
public class Minimax {
    
   /** Kolor kamieni komputera */ 
   private final byte computerColor;   
   /** Referencja do logicznej warstwy planszy */
   private final BoardLogic board;
   /** Maks-min  wynik dla algorytmu */
   public final static int MAX_SCORE = 10000;   
   /** Maksymalna głębokość rekurencji dla algorytmu */
   private final static int MAX_DEPTH = 3;
   /** Lista pustych pól na planszy (możliwe ruchy) */ 
   private final List<BoardField> emptyFields;
   /** Lista ruchów */
   private final LinkedList<BoardField> simulatedMoves;
   
   
   /**
    * Konstruktor obiektu reprezentującego algorytm Mini-max
    * @param board Referencja do logicznej warstwy planszy
    * @param computerColor Kolor kamieni gracza-komputera
    */
   private Minimax(BoardLogic board, byte computerColor) {
       
      this.computerColor = computerColor;  
      this.board = board;
      emptyFields = board.getEmptyFields();
      simulatedMoves = new LinkedList<>();
      
   }
   
   
   /**
    * Nowa instancja
    * @param board Referencja do logicznej warstwy planszy
    * @param computerColor Kolor kamieni gracza-komputera
    * @return Nowa instancja
    */   
   public static Minimax getInstance(BoardLogic board, byte computerColor) {
   
      return new Minimax(board, computerColor);  
       
   }
   
   
   /**
    * Metoda zwracająca wygenerowany ruch komputera, wywołuje rekurencję Mini-max
    * @return tablica {a,b} współrzędnych pola na planszy
    */
   public BoardField getMove() {
       
     throw new UnsupportedOperationException("Not supported yet."); 
   
  }
   

   
   
  /**
   * Metoda zwraca przyjęty indeks danego koloru w wykorzystywanej tablicy 2-wymiarowej
   * @param color Kolor kamieni
   * @return Przyjęty indeks koloru
   */
  private static int getColorIndex(byte color) {
      
    return (color==BoardField.WHITE) ? 0 : 1;  
      
  }

    
}
