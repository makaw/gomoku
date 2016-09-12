/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 *
 * TO_DO! 
 * To tylko próby z algorytmem Minimax (AI gracza-komputera)
 * Na ten moment klasa NIE DZIAŁA nawet w przybliżeniu poprawnie i nie jest wykorzystywana. 
 * 
 */
public class Minimax {
    
   /** Kolor kamieni komputera */ 
   private final byte computerColor;   
   /** Referencja do logicznej warstwy planszy */
   private final BoardLogic board;
   /** Maks-min  wynik dla algorytmu */
   static private final int MAX_SCORE = 1000;
   /** Maksymalna głębokość rekurencji dla algorytmu */
   static private final int MAX_DEPTH = 5;
   /** Lista pustych pól na planszy (możliwe ruchy) */ 
   private final List<BoardField> emptyFields;
   /** Lista ruchów */
   private final LinkedList<BoardField> simulatedMoves;
   
   /**
    * Konstruktor obiektu reprezentującego algorytm Mini-max
    * @param board Referencja do logicznej warstwy planszy
    * @param computerColor Kolor kamieni gracza-komputera
    */
   public Minimax(BoardLogic board, byte computerColor) {
       
      this.computerColor = computerColor;  
      this.board = board;
      emptyFields = board.getEmptyFields();
      simulatedMoves = new LinkedList<>();
      
   }
   
   /**
    * Metoda zwracająca wygenerowany ruch komputera, wywołuje rekurencję Mini-max
    * @return tablica {a,b} współrzędnych pola na planszy
    */
   public BoardField getMove() {
       
    int m;
    // sugerowany ruch (współrzędne a i b pola planszy)
    BoardField move = null;
    // maksimum / minimum
    int mmx = 0;
    
    // przeglądanie wolnych pól na planszy
    for(BoardField field:emptyFields) 
      if (!simulatedMoves.contains(field)) {
            
        // symulacja dostępnego ruchu  
        board.setFieldState(field.getA(), field.getB(), computerColor);
        simulatedMoves.add(field);
        // rekurencja - ocena sytuacji po wykonaniu ruchu
        m = minimax(computerColor, 0);
        // przywrócenie poprzedniego stanu planszy
        board.setFieldState(field.getA(), field.getB(), BoardField.EMPTY);
        simulatedMoves.remove(field);
       
        // ewentualna zmiana sugerowanego ruchu
        if (m>mmx) {
          mmx = m;
          move = new BoardField(field.getA(), field.getB());
        }     
      
    }
      
    return move;
       
       
   }
   
   
   /**
    * Metoda implementująca algorytm Mini-max, rekurencja
    * @param currentPlayer kolor kamieni gracza, którego ruchy są aktualnie symulowane
    * @param depth głębokość rekurencji
    * @return Wynik, punktowa ocena sytuacji na planszy po wykonanej symulacji
    */
   private int minimax(byte currentPlayer, int depth)  {
       
     int m;
     int p = getColorIndex(currentPlayer);
      
    // System.out.println((playerColor==BoardLogic.WHITE ? "white" : "black") + "deep "+depth+", lastMove: "+lastMove[p][0]+"x"+lastMove[p][1]);
     
     // jeżeli remis, to wynik 0
     if (board.freeFieldsAmount==0) return 0;
     
     //if (simulatedMoves != null && !simulatedMoves.isEmpty())  
     BoardField lastMove = simulatedMoves.getLast();
    
     // ocena sytuacji w pobliżu ostatnio postawionego na planszy kamienia
     int score = (board.getScore(lastMove.getA(), lastMove.getB(), currentPlayer));

     if (Math.abs(score)!=0) return score;
     
     // jeżeli przekroczona maksymalna głębokość rekurencji, wynik 0
     if (depth>MAX_DEPTH) return 0;
     
     // zmiana bieżącego gracza na przeciwnika
     currentPlayer = (currentPlayer == BoardField.WHITE) ? BoardField.BLACK : BoardField.WHITE;
     

     // inicjalizacja minimum (dla komputera) lub maksimum (dla przeciwnika)
     int mmx = (currentPlayer == computerColor) ? -MAX_SCORE : MAX_SCORE;

     // przeglądanie wolnych pól
     for(BoardField field:new ArrayList<BoardField>(emptyFields)) 
       if (!simulatedMoves.contains(field)) {
          
          // symulacja dostępnego ruchu
          board.setFieldState(field.getA(), field.getB(), currentPlayer);
          simulatedMoves.add(field);
          // rekurencja - ocena sytuacji po wykonaniu ruchu
          m = minimax(currentPlayer, ++depth);
          // przywrócenie poprzedniego stanu planszy
          board.setFieldState(field.getA(), field.getB(), BoardField.EMPTY);
          simulatedMoves.remove(field);
          
          // ewentualna zmiana minimum/maksimum
          if(((currentPlayer == computerColor) && (m < mmx)) || 
              ((currentPlayer != computerColor) && (m > mmx))) mmx = m;
            
     }
      
     return mmx;
   
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
