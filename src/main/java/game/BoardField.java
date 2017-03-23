/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.io.Serializable;

import fr.pixelprose.minimax4j.Move;


/**
 *
 * Pojedyncze pole na planszy (w warstwie logicznej)
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
    * Konstruktor 
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
    * Konstruktor 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    */
   public BoardField(int a, int b) {
       
     this(a, b, BoardFieldState.EMPTY);
       
   }   
   
   
   public int getA() {
       
     return a;
       
   }
   
   public int getB() {
       
     return b;  
       
   }
   
   public BoardFieldState getState() {
       
      return state; 
       
   }
   
   public void setState(BoardFieldState state) {
       
      this.state = state; 
       
   }
   
   

}
