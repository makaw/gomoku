/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui.dialogs;


import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import game.GameMode;
import gomoku.IConf;
import gui.IBaseGUI;
import gui.SimpleDialog;

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
  private GameMode gameMode;
  
  /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej, wypełnienie wewn. pól 
    * i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    * @param firstTime Wartość true, jeżeli jest to start aplikacji
    */      
  public NewGameDialog(IBaseGUI frame, boolean firstTime) {
      
     super(frame);
     this.firstTime = firstTime;
     gameMode = GameMode.SINGLE_GAME;
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
     JPanel p = new JPanel(new GridLayout(GameMode.values().length, 1));
     final ButtonGroup bGroup = new ButtonGroup();
     
     for (GameMode mode : GameMode.values()) {
    	 
    	 JRadioButton jRadio = new JRadioButton(mode.toString(), mode == GameMode.DEFAULT); 
    	 jRadio.setFont(formsFont);
    	 jRadio.setEnabled(mode.isEnabled());
    	 jRadio.setFocusPainted(false);
    	 jRadio.setActionCommand(String.valueOf(mode.getCode()));
    	 bGroup.add(jRadio);
    	 p.add(jRadio);
    	 
     }
     
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
          GameMode modeTmp = GameMode.get(Integer.parseInt(bGroup.getSelection().getActionCommand()));
          
          String serverIP = "";
              
          // jeżeli to klient, to pobranie od użytkownika adresu IP serwera
          if (modeTmp == GameMode.NETWORK_GAME) {

             // usunięcie komponentów i ustawienie przezroczystego tła
             // żeby okienko wyboru nowej gry zniknęło, ale dalej blokowało wątki
             removeAll();
             setBackground(new Color(0, 0, 0, 0));
             
             serverIP = new PromptDialog(frame, "Adres IP / host serwera gry:",
            		 IConf.DEFAULT_HOST).getAnswer();
          }
          
           // zakończenie obecnej rozgrywki i rozpoczęcie nowej 
          if (modeTmp != GameMode.NETWORK_GAME || (serverIP!=null && !serverIP.isEmpty())) {
          
             gameMode = modeTmp;
             
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
  public GameMode getGameMode() {
      
    return gameMode;  
      
  }


    

}  


