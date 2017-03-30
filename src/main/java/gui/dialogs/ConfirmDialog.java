/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gomoku.Lang;
import gui.IBaseGUI;


/**
 *
 * Okienko dialogowe z prośbą o potwierdzenie
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class ConfirmDialog extends InfoDialog {
    
  /** Odpowiedź użytkownika: true jeżeli potwierdził */ 
  private boolean confirmed;
  
	
  /**
    * Konstruktor (okno po zakończeniu gry)
    * @param frame Interfejs GUI
    * @param question Pytanie do wyświetlenia w okienku
    * @param type Typ okienka
    */
   public ConfirmDialog(IBaseGUI frame, String question, DialogType type) {
      
	 super(frame, question, type);	     
	            
   } 
   
   
   /**
    * Konstruktor (okno potwierdzenia)
    * @param frame Interfejs GUI
    * @param question Pytanie
    */
   public ConfirmDialog(IBaseGUI frame, String question) {
       
     this(frame, question, DialogType.CONFIRM);
            
   } 
   
   

   @Override
   protected JPanel getButtonsPanel()  {   
           
	  confirmed = false; 
	   
      JButton buttonYes = new JButton(type == DialogType.CONFIRM
    		  ? Lang.get("Yes") : Lang.get("PlayAgain"));
      buttonYes.setFocusPainted(false);
      buttonYes.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) { 
            confirmed = true;
            dispose();
         }
      });
       
      JButton buttonNo = new JButton(type == DialogType.CONFIRM
    		  ? Lang.get("No") : Lang.get("OK"));
      buttonNo.setFocusPainted(false);
      buttonNo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
            dispose();
         }
      });
      
      JPanel p = new JPanel(new FlowLayout());
      p.setBorder(new EmptyBorder(0, 30, 5, 0)); 
      
      if (type == DialogType.CONFIRM) p.add(buttonYes);
      p.add(new JLabel(" "));
      p.add(buttonNo);
      p.add(new JLabel(" "));
      if (type != DialogType.CONFIRM) p.add(buttonYes);
      
      return p;
            
   }
    

   public boolean isConfirmed() {
       
      return confirmed; 
       
   }
   
    
}

