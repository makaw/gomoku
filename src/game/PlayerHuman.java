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
    * Konstruktor - wywołanie konstruktora z bazowej klasy abstrakcyjnej, 
    * przypisanie wartości/referencji do wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    */
   public PlayerHuman(byte pieceColor, BoardGraphics gBoard, BoardLogic lBoard, String name) {
       
     super(pieceColor, gBoard, lBoard, name);
       
   }  
    
  
   /**
    * Metoda obsługuje wykonanie ruchu przez gracza, wykorzystując obsługę zdarzeń myszy
    * @see MouseHandler
    */   
   @Override
   public void makeMove() {
    
     // ustawienie obsługi zdarzeń myszy
     final MouseHandler mouseHandler1
              = new MouseHandler(gBoard, lBoard, pieceColor);   
     final MouseHandler mouseHandler2
              = new MouseHandler(gBoard, lBoard, pieceColor);      
     gBoard.addMouseListener(mouseHandler1);
     gBoard.addMouseMotionListener(mouseHandler2);

     // oczekiwanie na ustawienie kamienia lub wymuszenie zakończenia ruchu
     Integer tmp = lBoard.freeFieldsAmount;
     do {} while (!gameRestarted && tmp.equals(lBoard.freeFieldsAmount));
    
     //  koniec kolejki, więc trzeba usunąć listenery myszy
     gBoard.removeMouseListener(mouseHandler1);
     gBoard.removeMouseMotionListener(mouseHandler2);
     
        
   }
    

    
}