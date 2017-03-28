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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gomoku.Lang;
import gui.IBaseGUI;
import gui.SimpleDialog;

/**
 *
 * Okienko dialogowe do wprowadzenia wiadomości przez użytkownika
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class PromptDialog extends SimpleDialog {
   
   /** Treść odpowiedzi wprowadzona przez użytkownika */ 
   private String answer; 
   /** Pytanie do wyświetlenia w okienku */
   private final String question;
   /** Domyślna odpowiedź */
   private final String defaultAnswer;
       
   /**
    * Konstruktor 
    * @param frame Interfejs GUI
    * @param question Pytanie do wyświetlenia w okienku
    */       
   public PromptDialog(IBaseGUI frame, String question) {
       
     this(frame, question, "");
            
   } 
    
  
   /**
    * Konstruktor 
    * @param frame Interfejs GUI
    * @param question Pytanie do wyświetlenia w okienku
    * @param defaultAnswer Domyślna odpowiedź
    */       
   public PromptDialog(IBaseGUI frame, String question, String defaultAnswer) {
       
     super(frame);
     this.question = question;
     this.defaultAnswer = defaultAnswer;
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
      
      JLabel ico = new JLabel(DialogType.PROMPT.getIcon());
      ico.setBorder(new EmptyBorder(25, 5, 0, 5));
      p.add(ico);
      
      
      JPanel p2 = new JPanel();
      p2.setLayout(new FlowLayout(FlowLayout.LEFT));
      p2.add(new JLabel(question));
      
      final JTextField msg = new JTextField(defaultAnswer);
      msg.setPreferredSize(new Dimension(200, 26));
      p2.setBorder(new EmptyBorder(5, 5, 0, 10)); 
      p2.add(msg);
       
      p.add(p2);
      p.setBorder(new EmptyBorder(5, 5, 0, 5)); 
      add(p);
     
      JButton buttonOk = new JButton(" " + Lang.get("OK") + " ");
      buttonOk.setFocusPainted(false);

      JButton buttonCancel = new JButton(Lang.get("Cancel"));
      buttonCancel.setFocusPainted(false);
      buttonCancel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
            dispose();
         }
      });
      
      ActionListener listener = new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) { 
             answer = msg.getText();
             dispose();
          }
      };

      
      msg.addActionListener(listener);
      buttonOk.addActionListener(listener);      
      
      p = new JPanel(new FlowLayout());
      p.setBorder(new EmptyBorder(0, 30, 5, 0)); 
      p.add(buttonOk);
      p.add(new JLabel(" "));
      p.add(buttonCancel);
      add(p);
      
      
   }
    

   public String getAnswer() {
       
      return answer; 
       
   }
   
   
    
}
