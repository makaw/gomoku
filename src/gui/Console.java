/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import javax.swing.*;
import java.awt.Color;
import javax.swing.text.*;

/**
 *
 * Szablon obiektu reprezentującego konsolę do wyświetlania komunikatów 
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class Console extends JTextPane {
      
   /** Flaga blokująca wyprowadzanie na konsolę komunikatów */ 
   private final Boolean lockedFlag; 
  /** Obiekt przycisku do wysyłania wiadomości w grze sieciowej, uchwyt jest potrzebny do
   * blokowania i odblokowywania przycisku */
  private final JButton msgButton;   
    
   /**
    * Konstruktor obiektu reprezentującego konsolę, przygotowuje komponent do użycia
    * @param msgButton
    */
   protected Console(JButton msgButton) {
       
      super();
      setEditable(false);
      setBackground(new Color(0xff, 0xef, 0xcd));
      // pozycja kursora zawsze na koncu pola tekstowego
      ((DefaultCaret)this.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      lockedFlag = false;
      
      this.msgButton = msgButton;
       
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
    * Metoda wypisująca komunikat na konsoli
    * @param msg Treść komunikatu
    * @param textColor Kolor tekstu
    * @param bgColor Kolor tła tekstu
    */
   public void setMessage(String msg, Color textColor, Color bgColor) {

     // synchronizacja, żeby uniknąć jednoczesnego dostępu metod piszących w konsoli
     synchronized(this) {
     
       if (lockedFlag) return;
       
       
       StyledDocument doc =  getStyledDocument();  
       
       Style style = StyleContext.getDefaultStyleContext().getStyle(
                     StyleContext.DEFAULT_STYLE);

       StyleConstants.setFontSize(style, 12);     
       StyleConstants.setForeground(style, textColor);
       StyleConstants.setBackground(style, bgColor);       

       
        // umieszczenie linii tekstu
       try {
         doc.insertString(doc.getLength(), msg, style);
       }
       catch(Exception e) {
        // System.err.println(e);
       }
       
     }
     
     // krótka przerwa na dokończenie operacji
     try {
      Thread.sleep(30);  
     }
     catch(InterruptedException e) {}
     
 
   }
   
   /**
    * Metoda wypisująca komunikat na konsoli na domyślnym tle
    * @param msg Treść komunikatu
    * @param textColor Kolor tekstu
    * @see Console#setMessage(java.lang.String, java.awt.Color, java.awt.Color) 
    */
   public void setMessage(String msg,  Color textColor) {
   
      setMessage(msg, textColor, this.getBackground());
      
   }      
   
   /**
    * Metoda wypisująca komunikat na konsoli i przechodząca 
    * do nowego wiersza
    * @param msg Treść komunikatu
    * @param textColor Kolor tekstu
    * @param bgColor Kolor tła tekstu
    * @see Console#setMessage(java.lang.String, java.awt.Color, java.awt.Color) 
    */   
   public void setMessageLn(String msg,  Color textColor, Color bgColor) {
   
      setMessage(msg+System.getProperty("line.separator"), textColor, bgColor);
      
   }
   
   /**
    * Metoda wypisująca komunikat na konsoli na domyślnym tle i przechodząca 
    * do nowego wiersza
    * @param msg Treść komunikatu
    * @param textColor Kolor tekstu
    * @see Console#setMessage(java.lang.String, java.awt.Color) 
    */   
   public void setMessageLn(String msg,  Color textColor) {
   
      setMessage(msg+System.getProperty("line.separator"), textColor);
      
   }   
   
   /**
    * Metoda przechodząca do nowego wiersza konsoli
    * @see Console#setMessage(java.lang.String, java.awt.Color) 
    */
   public void newLine() {
       
      setMessage(System.getProperty("line.separator"), Color.BLACK);
       
   }
   
   
  /**
   * Metoda blokuje/odblokowuje przycisk wiadomości
   * @param enabled True jeżeli przycisk ma być odblokowany, false jeżeli nie
   * @since 1.1
   */
  public void msgButtonEnable(boolean enabled) {
      
    msgButton.setEnabled(enabled);      
      
  }
  
    
}
