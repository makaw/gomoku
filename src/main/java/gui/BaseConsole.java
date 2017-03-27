/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
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
      
   /** Obiekt dokumentu do wprowadzania tekstu */
   protected final StyledDocument doc;	
   /** Styl tekstu */
   protected final Style style;
   
   
   protected BaseConsole() {
       
      super();
      setEditable(false);
      setBackground(new Color(0xff, 0xef, 0xcd));
      // pozycja kursora zawsze na koncu pola tekstowego
      ((DefaultCaret)this.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      
      doc = getStyledDocument(); 
      style = StyleContext.getDefaultStyleContext().getStyle(
              StyleContext.DEFAULT_STYLE);
      
   } 
      
   /**
    * Metoda wypisująca komunikat na konsoli
    * @param msg Treść komunikatu
    * @param textColor Kolor tekstu
    * @param bgColor Kolor tła tekstu
    */
   public synchronized void setMessage(String msg, Color textColor, Color bgColor) {

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

     try {
      Thread.sleep(30);  
     }
     catch(InterruptedException e) {}     
 
   }
   
   

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
   
      setMessage(msg + System.getProperty("line.separator"), textColor, bgColor);
      
   }
   

   public void setMessageLn(String msg,  Color textColor) {
   
      setMessageLn(msg, textColor, this.getBackground());
      
   }   
   
   
   /**
    * Metoda przechodząca do nowego wiersza konsoli
    * @see Console#setMessage(java.lang.String, java.awt.Color) 
    */
   public void newLine() {
       
      setMessage(System.getProperty("line.separator"), Color.BLACK);
       
   }
   
   
   /**
    * Metoda usuwająca całą zawartość konsoli
    */
   public synchronized void clear() {
        
     try {
      Thread.sleep(10);  
     }
     catch(InterruptedException e) {
       System.err.println(e);
     }       
     
     try {       
         doc.remove(0, doc.getLength());
     } catch (BadLocationException e) {
         System.err.println(e.getMessage());
     }
     
     
   }   
   
    
}

