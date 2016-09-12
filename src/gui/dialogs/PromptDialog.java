/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;

import gui.SimpleDialog;
import gui.Images;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import gui.IBaseGUI;

/**
 *
 * Szablon obiektu wywołującego okienko dialogowe do wprowadzenia wiadomości przez użytkownika
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class PromptDialog extends SimpleDialog {
   
   /** Treść wiadomości wprowadzona przez użytkownika */ 
   private String message; 
   /** Pytanie do wyświetlenia w okienku */
   private final String question;
   /** Domyślna odpowiedź */
   private final String defaultMsg;
       
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
    * i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
     * @param question Pytanie do wyświetlenia w okienku
    */       
   public PromptDialog(IBaseGUI frame, String question) {
       
     this(frame, question, "");
            
   } 
    
  
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
    * i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    * @param question Pytanie do wyświetlenia w okienku
     * @param defaultMsg Domyślna odpowiedź
    */       
   public PromptDialog(IBaseGUI frame, String question, String defaultMsg) {
       
     super(frame);
     this.question = question;
     this.defaultMsg = defaultMsg;
     super.showDialog(300, 125); 
            
   }    
   
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */   
   @Override
   protected void getContent()  {   
    
      setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)); 
      
      JPanel p = new JPanel();
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
      
      
      //JLabel ico = new JLabel(UIManager.getIcon("OptionPane.questionIcon")); 
      JLabel ico = new JLabel(Images.getIcon("question.png"));
      ico.setBorder(new EmptyBorder(25, 5, 0, 5));
      p.add(ico);
      
      
      JPanel p2 = new JPanel();
      p2.setLayout(new FlowLayout(FlowLayout.LEFT));
      p2.add(new JLabel(question));
      
      // przygotowanie pola tekstowego do wprowadzenia wiadomości
      final JTextField msg = new JTextField(defaultMsg);
      msg.setPreferredSize(new Dimension(200, 26));
      p2.setBorder(new EmptyBorder(5, 5, 0, 10)); 
      p2.add(msg);
       
      p.add(p2);
      p.setBorder(new EmptyBorder(5, 5, 0, 5)); 
      add(p);
     
      // przygotowanie przycisków OK i Anuluj
      JButton buttonSend = new JButton(" OK ");
      buttonSend.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) { 
            message = msg.getText();
            dispose();
         }
      });

      JButton buttonCancel = new JButton("Anuluj");
      buttonCancel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
            dispose();
         }
      });
      
      p = new JPanel(new FlowLayout());
      p.setBorder(new EmptyBorder(0, 30, 5, 0)); 
      p.add(buttonSend);
      p.add(buttonCancel);
      add(p);
      
      
   }
    
   /**
    * Metoda pobierająca treść wiadomości wprowadzonej przez użytkownika
    * @return Treść wprowadzonej wiadomości
    */
   public String getMessage() {
       
      return message; 
       
   }
   
   /**
    * Statyczna metoda formatująca wiadomość przed przesłaniem do konsoli
    * @param msg Treść wprowadzonej wiadomości
    * @return Sformatowana treść wiadomości
    * @deprecated 
    */
   @Deprecated
   public static String formatMsg(String msg) {
       
      final String sep = System.getProperty("line.separator")+ "---------------------------------------"
                        + "---------------------------------------" + System.getProperty("line.separator");
      return sep+msg+sep;
       
       
   }
   
    
}
