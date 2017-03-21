/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * Podstawowa konsola (GUI serwera)
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class BaseConsole extends JTextPane  {
      
   /** Flaga blokująca wyprowadzanie na konsolę komunikatów */ 
   protected final Boolean lockedFlag; 

   
   /**
    * Konstruktor obiektu reprezentującego konsolę, przygotowuje komponent do użycia
    */
   protected BaseConsole() {
       
      super();
      setEditable(false);
      setBackground(new Color(0xff, 0xef, 0xcd));
      // pozycja kursora zawsze na koncu pola tekstowego
      ((DefaultCaret)this.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      lockedFlag = false;
      
      
       
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
         System.err.println(e);
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
   
   
    
}

