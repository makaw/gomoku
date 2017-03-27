/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gomoku.Lang;
import gui.IBaseGUI;
import gui.SimpleDialog;

/**
 *
 * Okienko informacyjne
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class InfoDialog extends SimpleDialog {
   
   /** Typ okna */	
   protected final DialogType type;	
   /** Treść wiadomości */
   protected final String text;
	
 
   /**
    * Konstruktor
    * @param frame Interfejs GUI
    * @param text Treść wiadomości
    * @param type Typ okna
    */
   public InfoDialog(IBaseGUI frame, String text, DialogType type) {
            
     super(frame);
	 this.text = text;
     this.type = type;
     super.showDialog(300, 165); 
            
   }    
   
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */   
   @Override
   protected void getContent()  {   
    
      setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); 
      
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
      
      JLabel ico = new JLabel(type.getIcon());
      ico.setBorder(new EmptyBorder(25, 5, 0, 5));
      p.add(ico);
            
      p.setBorder(new EmptyBorder(5, 5, 0, 5)); 
      add(p);

      p.add(new TextField(text));
           
      add(getButtonsPanel());
      
      
   }
   
   
   /**
    * Przygotowanie przycisków 
    * @return Panel z przyciskami
    */
   protected JPanel getButtonsPanel() {
	   
	  JButton button = new JButton(" " + Lang.get("OK") + " ");
	  button.setFocusPainted(false);
	  button.addActionListener(new ActionListener() {
	       @Override
	       public void actionPerformed(final ActionEvent e) {   
	          dispose();
	       }
	    });
	      
	  JPanel p = new JPanel(new FlowLayout());
	  p.setBorder(new EmptyBorder(0, 30, 5, 0)); 
	  p.add(button);
	  
	  return p;
	  
   }
    

}
