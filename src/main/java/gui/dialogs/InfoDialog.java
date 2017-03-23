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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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
   private final DialogType type;	
   /** Treść wiadomości */
   private final String text;
	
 
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
      
      JTextArea textArea = new JTextArea(text);
      textArea.setLineWrap(true);
      textArea.setWrapStyleWord(true);
      textArea.setFont(formsFontB);
      textArea.setOpaque(false); 
      textArea.setEditable(false);
      JScrollPane sc = new JScrollPane(textArea);
      sc.setBorder(new EmptyBorder(10, 10, 5, 5));
      sc.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
      p.add(sc);
     
      JButton button = new JButton(" OK ");
      button.setFocusPainted(false);
      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
            dispose();
         }
      });
      
      p = new JPanel(new FlowLayout());
      p.setBorder(new EmptyBorder(0, 30, 5, 0)); 
      p.add(button);
      add(p);
      
      
   }
    

}
