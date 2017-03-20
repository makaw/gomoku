/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.io.Serializable;

import fr.pixelprose.minimax4j.Move;


/**
 *
 * Szablon obiektu reprezentującego pojedyncze pole na planszy (w warstwie logicznej)
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardField implements Serializable, Move {    
    
   /** Indeks a (kolumna) pola planszy */ 
   private final int a;
   /** Indeks b (wiersz) pola planszy */
   private final int b;
   /** Stan pola planszy */
   private BoardFieldState state;
   
   
   private final static long serialVersionUID = 1L;
   
   
   /** 
    * Konstruktor obiektu reprezentującego pojedyncze pole na planszy 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    * @param state Początkowy stan pola
    */
   public BoardField(int a, int b, BoardFieldState state) {
       
     this.a = a;
     this.b = b;
     this.state = state;
       
   }   
   
   /** 
    * Konstruktor obiektu reprezentującego pojedyncze pole na planszy 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    */
   public BoardField(int a, int b) {
       
     this(a, b, BoardFieldState.EMPTY);
       
   }   
   
   /**
    * Metoda zwracająca indeks a (kolumnę) pola planszy
    * @return Indeks a (kolumna) pola planszy
    */
   public int getA() {
       
     return a;
       
   }
   
   /**
    * Metoda zwracająca indeks b (wiersz) pola planszy
    * @return Indeks b (wiersz) pola planszy
    */      
   public int getB() {
       
     return b;  
       
   }
   
   /**
    * Metoda zwracająca stan pola planszy (puste, biały kamień, czarny kamień)
    * @return Stan pola planszy
    */  
   public BoardFieldState getState() {
       
      return state; 
       
   }
   
   /**
    * Metoda zmieniająca stan pola planszy (puste, biały kamień, czarny kamień)
    * @param state Nowy stan pola planszy
    */   
   public void setState(BoardFieldState state) {
       
      this.state = state; 
       
   }
   
   

}
