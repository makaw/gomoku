/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

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
   /** Opcja w menu - nowa gra */
   private final JMenuItem newGameItem;
    
   /**
    * Konstruktor 
    * @param msgButton Przycisk do wysyłania wiadomości w grze sieciowej
    * @param disconButton Przycisk do rozłączenia z serwerem
    * @param newGameItem Opcja w menu - nowa gra
    */
   protected Console(JButton msgButton, JButton disconButton, JMenuItem newGameItem) {
       
      super();
      
      this.msgButton = msgButton;
      this.disconButton = disconButton;
      this.newGameItem = newGameItem;
      
   } 
   
   
   /**
    * Metoda usuwająca całą zawartość konsoli
    */
   public void clear() {
    
     // krótka przerwa na ewentualne dokończenie innych operacji na konsoli  
     try {
      Thread.sleep(10);  
     }
     catch(InterruptedException e) {
       System.err.println(e);
     }       
     
     // synchronizacja, żeby uniknąć jednoczesnego dostępu metod piszących w konsoli
     synchronized(this) { 
         
       if (lockedFlag) return;
         
       StyledDocument doc =  getStyledDocument();  
       try {       
           doc.remove(0, doc.getLength());
       } catch (BadLocationException e) {
           System.err.println(e.getMessage());
       }
     
       
     }

     
   }
   
      
   /**
    * Komunikat - jak rozpocząć nową grę
    */
   public void newGameMsg() {
	   
	  setMessageLn("Wybierz \"Gra\"  \u279C \"Nowa gra\" aby rozpocz\u0105\u0107.", Color.GRAY); 
	   
   }   
   
   
  /**
   * Metoda blokuje/odblokowuje przyciski wiadomości i odłączenia
   * @param enabled True jeżeli przyciski mają być odblokowane
   */
  public void networkButtonsEnable(boolean enabled) {
      
    msgButton.setEnabled(enabled); 
    disconButton.setEnabled(enabled); 
    newGameItem.setEnabled(!enabled);
      
  }
  
  
}
