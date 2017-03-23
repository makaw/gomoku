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
 * Okienko dialogowe z formularzem wyboru trybu nowej gry
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class NewGameDialog extends SimpleDialog {

  /** Wybrany przez użytkownika tryb nowej gry */
  private GameMode gameMode;
  
  /**
    * Konstruktor
    * @param frame Interfejs GUI
    */      
  public NewGameDialog(IBaseGUI frame) {
      
     super(frame);
     gameMode = GameMode.DEFAULT;
     super.showDialog(320, 220);
       
  }
  

  
  /**
   * Metoda wyświetlająca zawartość okienka
   */
  @Override
  protected final void getContent() {

     setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
     setTitle("Gomoku - nowa gra");
     
     JLabel title = new JLabel("Wybierz tryb nowej rozgrywki:");
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
  
  
  public GameMode getGameMode() {
      
    return gameMode;  
      
  }


    

}  


