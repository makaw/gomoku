/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

/**
 *
 * Klasa abstrakcyjna wykorzystywana do budowy wszystkich okienek dialogowych.
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public abstract class SimpleDialog extends JDialog {
   
   /** Interfejs GUI */
   protected final IBaseGUI frame;
   /** Czcionka wykorzystywana w okienkach dialogowych */
   protected final Font formsFont, formsFontB;
    
   /**
    * Konstruktor (dla rozszerzających klas) 
    * @param frame Interfejs GUI
    */
   
   protected SimpleDialog(IBaseGUI frame) {
       
      super((JFrame)frame, true);
      setTitle("Gomoku");
      this.frame = frame;
      formsFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
      formsFontB = new Font(Font.SANS_SERIF, Font.BOLD, 12);
      
   }
   
   
   /**
    * Metoda abstrakcyjna, zawartość poszczególnych okienek dialogowych
    */
   protected abstract void getContent();
    
   /**
    * Metoda wywołująca okienko dialogowe, wspólna dla wszystkich okienek (klas dziedziczących)
    * @param width Szerokość okienka w pikselach
    * @param height Wysokość okienka w pikselach
    */
   protected final void showDialog(int width, int height) {
       	 
     setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); 
     
     getContent();
       
     pack();
     setSize(width, height);
     setLocationRelativeTo((JFrame)frame);
     setResizable(false);
     setVisible(true); 

   }      
   
   
   
   /**
    * Przewijane pole tekstowe
    *
    */
   protected class TextField extends JScrollPane {
	   
	 public TextField(String text) {
		 
	   super();
	   
	   JTextArea textArea = new JTextArea(text);
	   textArea.setLineWrap(true);
	   textArea.setWrapStyleWord(true);
	   textArea.setFont(formsFontB);
	   textArea.setOpaque(false); 
	   textArea.setEditable(false);
	   
	   setViewportView(textArea);
	   setBorder(new EmptyBorder(10, 10, 5, 5));
	   setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);   
		 
	 }
	 
   }
	   

}
