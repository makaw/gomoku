/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.IBaseGUI;
import gui.ImageRes;
import gui.SimpleDialog;


/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z prośbą o potwierdzenie
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class ConfirmDialog extends SimpleDialog {
    
   /** Odpowiedź użytkownika: true jeżeli potwierdził */ 
   private boolean confirmed;
   /** Pytanie do wyświetlenia w okienku */
   private final String question;
    
  /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
    * i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    * @param question Pytanie do wyświetlenia w okienku
    */    
   public ConfirmDialog(IBaseGUI frame, String question) {
       
     super(frame);
     confirmed = false;
     this.question = question;
     super.showDialog(300, 120); 
            
   } 
   

    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   
    
      setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); 
      
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
      p.setPreferredSize(new Dimension(130, 70));
      
      //JLabel ico = new JLabel(UIManager.getIcon("OptionPane.questionIcon")); 
      JLabel ico = new JLabel(ImageRes.getIcon("question.png"));
      ico.setBorder(new EmptyBorder(25, 0, 0, 20));
      p.add(ico);
      
      // wyświetlenie pytania
      p.add(new JLabel(question, JLabel.CENTER));
      
      add(p); 
       
      // przygotowanie przycisków Tak/Nie
      JButton buttonYes = new JButton("Tak");
      buttonYes.setFocusPainted(false);
      buttonYes.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) { 
            confirmed = true;
            dispose();
         }
      });
       
      JButton buttonNo = new JButton("Nie");
      buttonNo.setFocusPainted(false);
      buttonNo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
            dispose();
         }
      });
      
      p = new JPanel(new FlowLayout());
      p.setBorder(new EmptyBorder(0, 40, 0, 0)); 
      p.add(buttonYes);
      p.add(buttonNo);
      add(p);
            
   }
    

   public boolean isConfirmed() {
       
      return confirmed; 
       
   }
   
    
}

