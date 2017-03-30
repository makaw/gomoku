/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import game.GameMode;
import gomoku.AppObserver;
import gomoku.Lang;
import gomoku.Settings;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.SettingsDialog;


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
  private static final int F_WIDTH = 450;
  /** Wysokość okna aplikacji w pikselach */
  private static final int F_HEIGHT = 310;   
  /** Referencja do obiektu konsoli w GUI serwera */
  private final BaseConsole console;
  /** Ustawienia gry po stronie serwera */
  private final Settings serverSettings;
  /** Obserwator - komunikacja z wątkami serwera */
  private final AppObserver serverSpy;
  /** przyciski */
  private final JButton settingsButton, restartButton, exitButton, hideButton;
  /** system tray */
  private ServerTrayIcon tray;
  
  
  /**
   * Konstruktor
   * @param serverSpy  Obserwator - komunikacja z wątkami serwera
   * @param serverSettings Ustawienia gry po stronie serwera
   */      
  public ServerGUI(final AppObserver serverSpy, Settings serverSettings) {
     
    super("Gomoku Server");    
    
    this.serverSettings = serverSettings;
    this.serverSpy = serverSpy;
    
    setIconImage(ImageRes.getImage("icon_small.png")); 
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // umieszczenie okna programu na środku ekranu
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((int)((screenSize.getWidth() - F_WIDTH)/2), 
              (int)((screenSize.getHeight() - F_HEIGHT)/2), F_WIDTH, F_HEIGHT);
      
    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
  
    console = new BaseConsole(); 
      
    getContentPane().add(console);
      
    console.setPreferredSize(new Dimension(F_WIDTH-30, 250));
       
    getContentPane().add(new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
 
    
    settingsButton = new JButton(Lang.get("Settings"));
    settingsButton.setFocusPainted(false);
    
    settingsButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
          
          new SettingsDialog(ServerGUI.this);  

       }
    });    

    restartButton = new JButton(Lang.get("Restart"));
    restartButton.setFocusPainted(false);
    restartButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   

          boolean res = new ConfirmDialog(ServerGUI.this,
        		  Lang.get("ServerRestartConfirm")).isConfirmed();  
          if (res) serverSpy.sendObject("state", "restart");
       }
    });
    
    exitButton = new JButton(Lang.get("Quit"));
    exitButton.setFocusPainted(false);
    exitButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
         boolean res = new ConfirmDialog(ServerGUI.this,
        		 Lang.get("ServerExitConfirm")).isConfirmed();           
         if (res) System.exit(0);
       }
    });    
    
    hideButton = new JButton(Lang.get("Hide"));
    hideButton.setFocusPainted(false);
    hideButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {   
         ServerGUI.this.setVisible(false);
       }
    });    
        
      
    JPanel p = new JPanel(new GridLayout(1,4));
    
    p.add(settingsButton);
    p.add(restartButton);
    p.add(exitButton);
    p.add(hideButton);
    
    getContentPane().add(p);
    
    pack();
    setSize(F_WIDTH, F_HEIGHT);
    setResizable(false);
    setVisible(true);  
    
  }
  
  
  /**
   * Dodaje menu do zasobnika systemowego (system tray)
   */
  public void setTray() {

 	 tray = new ServerTrayIcon(this);
 
	 try {
	   tray.add();
	 }
	 catch (Exception e) {
	   System.err.println(Lang.get("UnableToLoadSystemTray", e.getMessage()));
	 } 
	 
  }
  
  
  /**
   * Powiadomienie w system tray
   * @param msg Treść
   */
  public void trayMessage(String msg) {
	  
	tray.displayMessage(msg);  
	  
  }



  public BaseConsole getConsole() {
        
    return console;  
        
  }
  
  
  
  @Override
  public Settings getSettings() {
        
    return serverSettings;  
        
  }    
    
  
  @Override
  public boolean isServer() {
      
     return true; 
      
  }  
  
  
  
  public AppObserver getServerSpy() {
	return serverSpy;
  }


  @Override
  public void restartGame(GameMode gameMode, String serverIP) {}
  
     
  @Override
  public void restartGameSettings() {
  
	 console.setMessageLn(Lang.get("SettingsChanged"), Color.BLUE); 
     serverSpy.sendObject("settings", serverSettings);
     
  }
    
  
  @Override
  public void translate() {
	  
	settingsButton.setText(Lang.get("Settings"));  
	restartButton.setText(Lang.get("Restart"));
	exitButton.setText(Lang.get("Quit"));
	hideButton.setText(Lang.get("Hide"));
	
	try {
	  tray.translate();
	}
	catch (NullPointerException e) { }
	
	console.setMessageLn("[" + Lang.get("Language") + ": " + Lang.getName() + "]", Color.GRAY);
	 
  }
  
  
  @Override
  public void setVisible(boolean visible) {
	  
	 super.setVisible(visible);
	 try {
	    tray.enableShowItem(!visible); 
	 }
	 catch (NullPointerException e) {}

  }
  
}

