/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;


/**
 *
 * Klasa abstrakcyjna służąca do konstruowania szablonów obiektów,
 * reprezentujących graczy różnych typów (człowiek, komputer 
 * oraz ludzie-gracze zdalni)
 * 
 * @author Maciej Kawecki
 * 
 */
public abstract class Player {
    
   /** Kolor kamieni gracza - biały lub czarny */
   protected final BoardFieldState pieceColor;  
   /** Nazwa gracza */
   protected final String name;
   /** Referencja do obiektu będącego graficzną reprezentacją planszy */
   protected final BoardGraphics gBoard;
   /** Referencja do obiektu logicznej warstwy planszy */
   protected final Board lBoard;
   /** Zmienna wymuszająca zakończenie ruchu w razie zakończenia gry */
   protected boolean gameRestarted;
   /** Ostatni ruch gracza */
   protected BoardField lastMove;
   
   
   /** 
    * Konstruktor (dla rozszerzających klas), przypisanie wartości/referencji do 
    * wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    */
   public Player(BoardFieldState pieceColor, BoardGraphics gBoard, Board lBoard, String name) {
       
     this.pieceColor = pieceColor;
     this.gBoard = gBoard;
     this.lBoard = lBoard;
     this.name = name;
     gameRestarted = false;
       
   }  
   
   
   /**
    * Metoda wymuszająca zakończenie ruchu gracza
    */
   public void forceEndTurn() {
       
      gameRestarted = true; 
       
   }
   
   /**
    * Metoda abstrakcyjna, wykonanie ruchu, specyficzne dla graczy różnych typów
    */
   public abstract void makeMove();

   

   public BoardFieldState getPieceColor() {  
       
     return pieceColor; 
   
   }   
  
    
   /**
    * Metoda pobierająca nazwę gracza
    * @return Nazwa gracza
    */
   public String getName() {
       
      return name + " (" + pieceColor.getName() + ")";
       
   }
   
   
   public BoardField getLastMove() {	   
	   
	  return lastMove; 
	   
   }
      
    
}
