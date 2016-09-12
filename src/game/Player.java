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
   protected final byte pieceColor;  
   /** Nazwa gracza */
   protected final String name;
   /** Referencja do obiektu będącego graficzną reprezentacją planszy */
   protected final BoardGraphics gBoard;
   /** Referencja do obiektu logicznej warstwy planszy */
   protected final BoardLogic lBoard;
   /** Zmienna wymuszająca zakończenie ruchu w razie zakończenia gry */
   protected boolean gameRestarted;
   
   
   /** 
    * Konstruktor (dla rozszerzających klas), przypisanie wartości/referencji do 
    * wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    */
   public Player(byte pieceColor, BoardGraphics gBoard, BoardLogic lBoard, String name) {
       
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

   /**
    * Metoda pobierająca kolor kamieni gracza
    * @return Kolor kamieni gracza
    */
   public byte getColor() {  
       
     return pieceColor; 
   
   }   
  
    
   /**
    * Metoda pobierająca nazwę gracza
    * @return Nazwa gracza
    */
   public String getName() {
       
      return name + " (" + getColorName() + ")";
       
   }
      
   
   /**
    * Metoda zwracająca nazwę koloru kamieni gracza
    * @return Nazwa koloru kamieni gracza
    */
   private String getColorName() {
       
     return (pieceColor==BoardField.WHITE) ? "bia\u0142e" : "czarne";
       
   }
   
    
}
