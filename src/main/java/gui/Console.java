/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import gomoku.Lang;

/**
 *
 * Konsola do wyświetlania komunikatów w oknie gry 
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class Console extends BaseConsole {
      
   /** Przycisk do wysyłania wiadomości w grze sieciowej */
   private final JButton msgButton;   
   /** Przycisk do rozłączenia z serwerem */
   private final JButton disconButton;
   /** Menu gry */
   private final MenuGame menuGame;
    
   /**
    * Konstruktor 
    * @param msgButton Przycisk do wysyłania wiadomości w grze sieciowej
    * @param disconButton Przycisk do rozłączenia z serwerem
    * @param menuGame menu "Gra"
    */
   protected Console(JButton msgButton, JButton disconButton, MenuGame menuGame) {
       
      super();
      
      this.msgButton = msgButton;
      this.disconButton = disconButton;
      this.menuGame = menuGame;
      
   } 
   
   
   /**
    * Metoda usuwająca całą zawartość konsoli
    */
   public synchronized void clear() {
    
     // krótka przerwa na ewentualne dokończenie innych operacji na konsoli  
     try {
      Thread.sleep(10);  
     }
     catch(InterruptedException e) {
       System.err.println(e);
     }       
     
     StyledDocument doc =  getStyledDocument();  
     try {       
         doc.remove(0, doc.getLength());
     } catch (BadLocationException e) {
         System.err.println(e.getMessage());
     }
     
     
   }
   
      
   /**
    * Komunikat - jak rozpocząć nową grę
    */
   public void newGameMsg() {
	   
	  setMessageLn(Lang.get("UseGameMenuToStart"), Color.GRAY); 
	   
   }   
   
   
  /**
   * Metoda blokuje/odblokowuje przyciski wiadomości i odłączenia
   * @param enabled True jeżeli przyciski mają być odblokowane
   */
  public void networkButtonsEnable(boolean enabled) {
      
    msgButton.setEnabled(enabled); 
    disconButton.setEnabled(enabled); 
    menuGame.enableItems(!enabled);
      
  }
  
  
}
