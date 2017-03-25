/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;

/**
 *
 * Szablon obiektu reprezentującego gracza-człowieka w grze z komputerem lub hot-seat
 * 
 * @author Maciej Kawecki
 * @see MouseHandler
 * 
 */
public class PlayerHuman extends Player {
    

   /**
    * Konstruktor
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    */
   public PlayerHuman(BoardFieldState pieceColor, BoardGraphics gBoard, Board lBoard, String name) {
       
     super(pieceColor, gBoard, lBoard, name);
       
   }  
    
  
   /**
    * Metoda obsługuje wykonanie ruchu przez gracza, wykorzystując obsługę zdarzeń myszy
    * @see MouseHandler
    */   
   @Override
   public void makeMove() {
    
     // ustawienie obsługi zdarzeń myszy
     final MouseHandler moveHandler
              = new MouseHandler(gBoard, lBoard, pieceColor);   
     final MouseHandler cursorHandler
              = new MouseHandler(gBoard, lBoard, pieceColor);      
     gBoard.addMouseListener(moveHandler);
     gBoard.addMouseMotionListener(cursorHandler);

     // oczekiwanie na ustawienie kamienia lub wymuszenie zakończenia ruchu
     Integer tmp = lBoard.freeFieldsAmount;
     do {} while (!gameRestarted && tmp.equals(lBoard.freeFieldsAmount));
    
     lastMove = moveHandler.getMove();
     
     //  koniec kolejki, więc trzeba usunąć listenery myszy
     gBoard.removeMouseListener(moveHandler);
     gBoard.removeMouseMotionListener(cursorHandler);
     
        
   }
    

    
}