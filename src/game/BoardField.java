/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.io.Serializable;


/**
 *
 * Szablon obiektu reprezentującego pojedyncze pole na planszy (w warstwie logicznej)
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardField implements Serializable {
    
    
   /** Stała używana do oznaczenia stanu pola planszy: puste pole */
   public final static byte EMPTY = 0;
   /** Stała używana do oznaczenia stanu pola planszy: biały kamień */
   public final static byte WHITE = 1;
   /** Stała używana do oznaczenia stanu pola planszy: czarny kamień */
   public final static byte BLACK = 2;      
    
   /** Indeks a (kolumna) pola planszy */ 
   private final int a;
   /** Indeks b (wiersz) pola planszy */
   private final int b;
   /** Stan pola planszy */
   private byte state;
   
   private final static long serialVersionUID = 1L;
   
   
   /** 
    * Konstruktor obiektu reprezentującego pojedyncze pole na planszy 
    * @param a Indeks a (kolumna) pola planszy
    * @param b Indeks b (wiersz) pola planszy
    * @param state Początkowy stan pola
    */
   public BoardField(int a, int b, byte state) {
       
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
       
     this(a, b, EMPTY);
       
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
   public byte getState() {
       
      return state; 
       
   }
   
   /**
    * Metoda zmieniająca stan pola planszy (puste, biały kamień, czarny kamień)
    * @param state Nowy stan pola planszy
    */   
   public void setState(byte state) {
       
      this.state = state; 
       
   }
   
   
    
}
