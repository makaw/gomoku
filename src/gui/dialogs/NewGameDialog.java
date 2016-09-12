/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;


import gomoku.IConf;
import gui.SimpleDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import gui.IBaseGUI;

/**
 *
 * Szablon obiektu wywołującego okienko dialogowe z formularzem wyboru trybu nowej gry
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class NewGameDialog extends SimpleDialog {

  /** Wartość true, jeżeli jest to start aplikacji, żeby móc się przywitać ;-) */  
  private final boolean firstTime; 
  /** Wybrany przez użytkownika tryb nowej gry */
  private byte gameMode;
  
  /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
    * i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    * @param firstTime Wartość true, jeżeli jest to start aplikacji
    */      
  public NewGameDialog(IBaseGUI frame, boolean firstTime) {
      
     super(frame);
     this.firstTime = firstTime;
     gameMode = 0;
     super.showDialog(320, 220);
       
  }
  
  /**
   * Konstruktor wołany jeżeli nie jest to start aplikacji. 
   * Wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól i wyświetlenie okienka
   * @param frame Referencja do interfejsu GUI
   */
  public NewGameDialog(IBaseGUI frame) {
      
     this(frame, false);    
      
  }
  
  /**
   * Metoda wyświetlająca zawartość okienka
   */
  @Override
  protected final void getContent() {

     setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
     
     JLabel title = new JLabel((firstTime ? "Witaj! " : "") + "Wybierz tryb " + 
                               (firstTime ? "" : "nowej ") + "rozgrywki:");
     title.setBorder(new EmptyBorder(20, 0, 20, 0));
     title.setAlignmentX(Component.CENTER_ALIGNMENT);
     add(title);
     
     // lista pól "radiowych" do wybrania trybu nowej gry
     JRadioButton jRadio1 = new JRadioButton("Rozpocznij gr\u0119 z komputerem *dumb AI", false);
     JRadioButton jRadio2 = new JRadioButton("Rozpocznij gr\u0119 2-osobow\u0105 (hot-seat)", false);  
     JRadioButton jRadio3 = new JRadioButton("Do\u0142\u0105cz do gry sieciowej (jako klient)", true);
     jRadio1.setFont(formsFont);
     jRadio2.setFont(formsFont);
     jRadio3.setFont(formsFont);
     jRadio1.setFocusPainted(false);
     jRadio2.setFocusPainted(false);
     jRadio3.setFocusPainted(false);
     // przypisanie kluczy do pól wyboru
     jRadio1.setActionCommand("1");
     jRadio2.setActionCommand("2");
     jRadio3.setActionCommand("3");
     // ustanowienie grupy pól wyboru
     final ButtonGroup bGroup = new ButtonGroup();
     bGroup.add(jRadio1);
     bGroup.add(jRadio2);
     bGroup.add(jRadio3);
    
     JPanel p = new JPanel(new GridLayout(3,1));
     
     p.add(jRadio1);
     p.add(jRadio2);
     p.add(jRadio3);
     p.setBorder(new EmptyBorder(0, 30, 0, 0));
     
     add(p);
    
     p = new JPanel(new FlowLayout());
         
     // przygotowanie przycisków OK i Anuluj
     JButton b1 = new JButton(" OK ");
     JButton b2 = new JButton("Anuluj");
     
     b1.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
          
          // pobranie wybranego trybu nowej gry 
          byte gameModeTmp = Byte.valueOf(bGroup.getSelection().getActionCommand());
          
          String serverIP = "";
              
          
          // jeżeli to klient, to pobranie od użytkownika adresu IP serwera
          if (gameModeTmp==3) {

             // usunięcie komponentów i ustawienie przezroczystego tła
             // żeby okienko wyboru nowej gry zniknęło, ale dalej blokowało wątki
             removeAll();
             setBackground(new Color(0, 0, 0, 0));
             
             serverIP = new PromptDialog(frame, "Adres IP / host serwera gry:",
                                    IConf.DEFAULT_HOST).getMessage();
             
          }
          
           // zakończenie obecnej rozgrywki i rozpoczęcie nowej 
          if (gameModeTmp!=3 || (serverIP!=null && !serverIP.isEmpty())) {
          
             gameMode = gameModeTmp;
             frame.restartGame(gameMode, serverIP);
          }
          
          
          dispose();
          
       }
     });
     
    
     b2.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {   
          dispose();
        }
     });
          
     p.add(b1); 
     p.add(b2);
     p.setBorder(new EmptyBorder(20, 0, 0, 0));    
     add(p);

     
  }
  
  /**
   * Metoda pobierająca wybrany przez użytkownika tryb nowej gry
   * @return Odpowiedź użytkownika 
   */   
  public byte getGameMode() {
      
    return gameMode;  
      
  }


    

}  


