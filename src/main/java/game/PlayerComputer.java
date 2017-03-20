/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;

/**
 *
 * Szablon obiektu reprezentującego gracza-komputer
 * 
 * @author Maciej Kawecki
 * 
 * 
 */
public class PlayerComputer extends Player  {
     
	
   /**
    * Konstruktor - wywołanie konstruktora z bazowej klasy abstrakcyjnej, 
    * przypisanie wartości/referencji do wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy 
    * @param name Nazwa gracza
    */
   public PlayerComputer(BoardFieldState pieceColor, BoardGraphics gBoard, BoardLogic lBoard, String name) {
       
      super(pieceColor, gBoard, lBoard, name);
       
   }
   
   
   
   /**
    * Wykonanie ruchu przez komputer 
    * 
    */
   @Override
   public void makeMove() {
          
       gBoard.setWaitMouseCursor();
       
       // pobranie wygenerowanego nowego ruchu 
       BoardField move = MoveGenerator.getMove(lBoard, pieceColor);   
    		   
       // rysowanie kamienia
       gBoard.setPiece(lBoard, move.getA(), move.getB(), getColor());
       gBoard.repaint();    
       // zmiana wartosci pola (logicznego)
       lBoard.setFieldState(move.getA(), move.getB(), getColor());

       try {
         Thread.sleep(10);
       } catch (InterruptedException e) {
         System.err.println(e);
       }       
       
   }
   
  
}

