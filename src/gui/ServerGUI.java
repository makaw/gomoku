/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import gomoku.Settings;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.SettingsDialog;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import network.ServerSettingsSpy;
import network.ServerSpy;


/**
 *
 * Szablon obiektu budującego graficzny interfejs użytkownika dla serwera
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class ServerGUI extends JFrame implements IBaseGUI {
    
  /** Szerokość okna aplikacji w pikselach */
  private static final int F_WIDTH = 395;
  /** Wysokość okna aplikacji w pikselach */
  private static final int F_HEIGHT = 320;   
  /** Referencja do obiektu konsoli w GUI serwera */
  private final ServerGUIConsole console;
  /** Referencja do obiektu zawierającego ustawienia gry po stronie serwera */
  private final Settings serverSettings;
  /** Referencja do obiektu służącego do komunikacji z wątkami 
   * odpowiedzialnymi za pracę serwera w razie zmiany ustawień */
  private final ServerSettingsSpy settingsSpy;
  
  /**
   * Konstruktor budujący graficzny interfejs użytkownika i wywołujący 
   * okno z wyborem trybu nowej gry
   * @param serverSpy Referencja do obiektu służącego do komunikacji z wątkami 
   * odpowiedzialnymi za pracę serwera 
   * @param settingsSpy Referencja do obiektu służącego do komunikacji z wątkami 
   * odpowiedzialnymi za pracę serwera w razie zmiany ustawień
   * @param serverSettings  Referencja do obiektu zawierającego ustawienia gry 
   * po stronie serwera
   */      
  public ServerGUI(final ServerSpy serverSpy, final ServerSettingsSpy settingsSpy, Settings serverSettings) {
     
    super("Gomoku Server");
    
    
    this.serverSettings = serverSettings;
    this.settingsSpy = settingsSpy;
    
    setIconImage(Images.getImage("icon_small.png")); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // umieszczenie okna programu na środku ekranu
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((int)((screenSize.getWidth() - F_WIDTH)/2), 
              (int)((screenSize.getHeight() - F_HEIGHT)/2), F_WIDTH, F_HEIGHT);
      
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
  
    console = new ServerGUIConsole(); 
      
    getContentPane().add(console);
      
    console.setPreferredSize(new Dimension(F_WIDTH-30, 250));
       
    getContentPane().add(new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
 
    
    // obsługa przycisku Ustawienia
    JButton settingsButton = new JButton(" Ustawienia ");
    settingsButton.setFocusPainted(false);
    
    final IBaseGUI frame = this;

    settingsButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
          
         // wywołanie okna z wyborem ustawień 
          new SettingsDialog(frame);  
            
       }
    });    

    // obsługa przycisku Restart
    JButton restartButton = new JButton(" Restart ");
    restartButton.setFocusPainted(false);
    restartButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
          // potwierdzenie przez użytkownika 
          boolean res = new ConfirmDialog(frame, "Zrestartowa\u0107 serwer ?").getResponse();  
          if (res) serverSpy.setState(true);
       }
    });
    
    // obsługa przycisku Koniec
    JButton exitButton = new JButton(" Koniec ");
    exitButton.setFocusPainted(false);
    exitButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
         boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 ?").getResponse();           
         if (res) System.exit(0);
       }
    });    
        
      
    JPanel p = new JPanel(new GridLayout(1,3));
    
    p.add(settingsButton);
    p.add(restartButton);
    p.add(exitButton);
    
    getContentPane().add(p);
    
    pack();
    setSize(F_WIDTH, F_HEIGHT);
    setResizable(false);
    setVisible(true);    

  }
    
  /**
   * Metoda zwraca referencję do konsoli w GUI serwera
   * @return Referencja do obiektu konsoli GUI serwera
   */
  public ServerGUIConsole getServerConsole() {
        
    return console;  
        
  }

  /**
   * Metoda zwraca obiekt ustawień gry po stronie serwera
   * @return Obiekt ustawień gry po stronie serwera
   */
  @Override
  public Settings getSettings() {
        
    return serverSettings;  
        
  }    
    
  
  /**
   * Metoda odpowiada: tak, to serwer
   * @return true
   */
  @Override
  public boolean isServer() {
      
     return true; 
      
  }  
  
  
  /**
   * Tylko nadpisanie pustą metodą
   * @param gameMode nie obsługiwane
   * @param serverIP nie obsługiwane
   */
  @Override
  public void restartGame(byte gameMode, String serverIP) {}
   
  /**
   * Tylko nadpisanie pustą metodą
   */
  @Override
  public void restartGameSettings() {
  
     settingsSpy.setSettings(serverSettings);
     console.setMessageLn("Ustawienia zostały zmienione.", Color.BLUE);
     
  }
  
  
}

