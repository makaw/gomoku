/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.WindowConstants;

/**
 *
 * Klasa abstrakcyjna wykorzystywana do budowy wszystkich okienek dialogowych. Definiuje 
 * proste pozbawione dekoracji okienko w bieżącym motywie.
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public abstract class SimpleDialog extends JDialog {
   
   /** Referencja do interfejsu GUI */
   protected final IBaseGUI frame;
   /** Czcionka wykorzystywana wewnątrz formularzy i pól tekstowych w okienkach dialogowych */
   protected final Font formsFont;
    
   /**
    * Konstruktor (dla rozszerzających klas), przypisanie referencji do GUI 
    * do wewnętrznego pola klasy, definicja czcionki
    * @param frame Referencja do interfejsu GUI
    */
   
   protected SimpleDialog(IBaseGUI frame) {
       
      super((JFrame)frame, true);
      this.frame = frame;
      formsFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
      
   }
   
   
   /**
    * Metoda abstrakcyjna, zawartość poszczególnych okienek dialogowych
    */
   protected abstract void  getContent();
    
   /**
    * Metoda wywołująca okienko dialogowe, wspólna dla wszystkich okienek (klas dziedziczących)
    * @param width Szerokość okienka w pikselach
    * @param height Wysokość okienka w pikselach
    */
   protected final void showDialog(int width, int height) {
       
     setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); 
     setUndecorated(true);
     getRootPane().setWindowDecorationStyle(JRootPane.NONE); 
     getRootPane().setBorder( BorderFactory.createLineBorder(new Color(0x57,0x7A,0xD5)) );
       
     getContent();  
       
     pack();
     setSize(width, height);
     setLocationRelativeTo((JFrame)frame);
     setResizable(false);
     setVisible(true); 

   }
   
   
   /**
    * Klasa wewn. - komponent przycisku zamykajacego okienko dialogowe
    */
   protected class CloseButton extends JButton {
   
    
      public CloseButton(String title) {
       
        super(title);
        setFocusPainted(false);
        addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
           
           dispose();
          
         }
       });
                    
     } 
    

     public CloseButton() {
           
        this("Zamknij"); 
           
     }       
    
  }      
   

}
