/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import game.GameMode;
import game.GameState;
import gomoku.AppObserver;
import gomoku.IConf;
import gomoku.Settings;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.PromptDialog;

/**
 *
 * Szablon obiektu budującego graficzny interfejs użytkownika
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class GUI extends JFrame implements IBaseGUI, Observer {
  
  
  /** Szerokość okna aplikacji w pikselach */
  private static final int F_WIDTH = 395;
  /** Wysokość okna aplikacji w pikselach */
  private static final int F_HEIGHT = 560;       
  
  /** Obiekt będący graficzną reprezentacją planszy */  
  private BoardGraphics board;
  /** Obiekt reprezentujący konsolę */
  private final Console console;
  /** Wybrany tryb gry */
  private GameMode gameMode;
  /** Obiekt służący do odtwarzania dźwięków */  
  private Sounds sounds;
  /** Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym przebieg gry  w zakresie zmiany stanu gry */
  private final AppObserver gameSpy;
  /** Obiekt przechowujący ustawienia gry */
  private final Settings settings;
  /** Panel w którym zawiera się graficzna reprezentacja planszy */
  private final JPanel panelBoard;
  /** Obiekt przycisku do wysyłania wiadomości w grze sieciowej */
  private final JButton msgButton;
  /** Przycisk rozłączenia z serwerem */
  private final JButton disconButton;
  /** Menu gra */
  private final MenuGame menuGame;
  /** Gniazdko dla klienta */
  private Socket socket;  
   
   
  
  /**
   * Konstruktor budujący graficzny interfejs użytkownika i wywołujący 
   * okno z wyborem trybu nowej gry
   * @param gameSpy Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym 
   * przebieg gry
   */  
  public GUI(final AppObserver gameSpy) {
      
    super("Gomoku");  
    
    this.gameSpy = gameSpy;
    
    settings = new Settings();
    
    setIconImage(ImageRes.getImage("icon_small.png"));  
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // umieszczenie okna programu na środku ekranu
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setBounds((int)((screenSize.getWidth() - F_WIDTH)/2), 
              (int)((screenSize.getHeight() - F_HEIGHT)/2), F_WIDTH, F_HEIGHT);
      
    getContentPane().setLayout(new BorderLayout());

    board = new BoardGraphics();
    sounds = new Sounds();

    // przygotowanie dolnej części ekranu - konsola i przyciski
    JPanel panelConsole = new JPanel();
    GridBagLayout gb = new GridBagLayout();
    GridBagConstraints gbc = new GridBagConstraints();
    panelConsole.setLayout(gb);

    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridwidth = GridBagConstraints.RELATIVE;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
  
    JPanel panelButtons = new JPanel(new GridLayout(3,1));
  
    // przycisk do wysyłania wiadomości
    msgButton = new JButton("", ImageRes.getIcon("message.png"));
    msgButton.setFocusPainted(false);
    msgButton.setToolTipText("Wy\u015blij wiadomo\u015b\u0107");
    msgButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        String msg = new PromptDialog(GUI.this, "Tre\u015b\u0107 wiadomo\u015bci:").getAnswer();
        if (msg!=null && !msg.isEmpty()) {
          gameSpy.sendObject("message", msg);
        } 
        
       }
    });   
    // domyślnie zablokowany, ma być aktywny tylko podczas gry sieciowej, włączanie w interfejsie Console
    msgButton.setEnabled(false);
     
    // przycisk do włączania/wyłączania dźwięku
    final JButton sndButton = new JButton("", ImageRes.getIcon(IConf.DEFAULT_ENABLE_SOUND ? "sound.png" : "mute.png"));
    sndButton.setFocusPainted(false);
    sndButton.setToolTipText("D\u017awi\u0119k w\u0142./wy\u0142.");
    sndButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {
         sndButton.setIcon(sounds.getEnabled() ? ImageRes.getIcon("mute.png") : ImageRes.getIcon("sound.png"));
         sounds.toggleSound();
       }
    });        
    
    
    menuGame = new MenuGame(this);
    
    // przycisk rozłączenia z serwerem
    disconButton = new JButton("", ImageRes.getIcon("disconnect.png"));
    disconButton.setFocusPainted(false);
    disconButton.setToolTipText("Roz\u0142\u0105cz z serwerem");
    disconButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
            
         boolean res = new ConfirmDialog(GUI.this, "Czy na pewno roz\u0142\u0105czy\u0107 ?").isConfirmed(); 
          
         if (res) 
         try {
           socket.close();
           disconButton.setEnabled(false);
           menuGame.getNewGameItem().setEnabled(true);
         } 
         catch (IOException ex) {
           System.err.println(ex);  
         }          
          
      }
       
    });
            
    disconButton.setEnabled(false);

    
    panelButtons.add(sndButton);
    panelButtons.add(msgButton);
    panelButtons.add(disconButton);

    panelButtons.setPreferredSize(new Dimension(40, 104));    
    gb.setConstraints(panelButtons, gbc);
    panelConsole.add(panelButtons);
     
    gbc.weighty = 1.0;
    gbc.weightx = 2.0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridheight = 1;
    
 
    console = new Console(msgButton, disconButton, menuGame.getNewGameItem());
    console.setPreferredSize(new Dimension(F_WIDTH-70, 104));
    console.setSize(new Dimension(F_WIDTH-70, 64));
  
    gb.setConstraints(console, gbc);
    panelConsole.add(console);
     
    panelConsole.add(new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    
    // zagnieżdżenie graficznej planszy w panelu
    panelBoard = new JPanel(new GridLayout(1,1));
    panelBoard.add(board);
    
    getContentPane().add(panelBoard, BorderLayout.NORTH);
    getContentPane().add(panelConsole, BorderLayout.SOUTH);

    // stworzenie glownego menu aplikacji
    JMenuBar menu = new JMenuBar();
    menu.add(menuGame);
    menu.add(new MenuHelp(this));
    setJMenuBar(menu);
    
    pack();
    setSize(F_WIDTH, F_HEIGHT);
    setResizable(false);
    setVisible(true);

    // witamy sie :-)
    console.setMessageLn("Dzie\u0144 dobry :-)", Color.BLACK); 
    
    gameSpy.addObserver(this);

  } 

  
  /**
   * Metoda obsługująca uruchomienie nowej gry: zmiana stanu (i ew. adresu IP serwera), powiadomienie wątku 
   *  kontrolującego przebieg gry o zmianie, wyczyszczenie graficznej planszy i konsoli
   * @param gameMode Wybrany przez użytkownika tryb nowej gry
   * @param serverIP Adres IP serwera
   * @see game.Game#update(java.util.Observable, java.lang.Object) 
   */
  @Override
  public void restartGame(GameMode gameMode, String serverIP) {      
      
	GameState gs = GameState.RESTART;
	gs.setServerIP(serverIP);  
	  
    if (this.gameMode == GameMode.NETWORK_GAME && socket != null) {
         
      try {
         socket.close();
      } catch (IOException ex) {
         System.err.println(ex);
      }
      
      gameSpy.sendObject("state", gs);
          
    } 
      
      
    this.gameMode = gameMode;
    
    // przesłanie do wątka gry informacji o zmianie stanu    
    gameSpy.sendObject("state", gs);
    board.clear();
    console.clear();   
    
    if (gameMode == GameMode.NETWORK_GAME) 
      console.setMessageLn("Wybrano tryb sieciowy. Oczekiwanie na do\u0142\u0105czenie 2. gracza ....", Color.DARK_GRAY);
              
    else {
      console.setMessageLn("Wybrano tryb gry.", Color.GRAY);    
    }
    
    
  }
  
  /**
   * Metoda obsługująca zmianę ustawień przez użytkownika: zatrzymanie bieżącej rozgrywki, 
   * powiadomienie wątku kontrolującego przebieg gry o zmianie stanu, wyczyszczenie konsoli, przygotowanie 
   * nowej graficznej planszy, oraz przesłanie do wątku kontrolującego przebieg gry nowych ustawień i 
   * referencji do nowej graficznej reprezentacji planszy
   * @see game.Game#update(java.util.Observable, java.lang.Object) 
   */
  @Override
  public void restartGameSettings() {

    // przesłanie do wątka gry informacji o zmianie stanu
    gameSpy.sendObject("state", GameState.WAIT);
    
    console.clear();
    console.setMessageLn("Przerwano gr\u0119 (zmiana ustawie\u0144).", Color.RED);
    
    board = new BoardGraphics(settings.getColsAndRows());
    panelBoard.removeAll();
    panelBoard.add(board);
    revalidate();
    
    try {
      Thread.sleep(100);  
    }
    catch(InterruptedException e) {}
    
    // przesłanie do wątka gry nowych ustawien i referencji do nowej planszy
    gameSpy.sendObject("settings-main", settings);
    gameSpy.sendObject("board", board);
    
    console.setMessageLn("Wybierz \"Gra\"  \u279C \"Nowa gra\" aby " +
                         "rozpocz\u0105\u0107.", Color.GRAY);

  }  
  
  
 /**
   * Metoda obsługująca zmianę ustawień dla trybu klienta (ustawienia pobrane z serwera):
   * wyczyszczenie konsoli, przygotowanie nowej graficznej planszy,
   * oraz przesłanie do wątku kontrolującego przebieg gry referencji do nowej
   * graficznej reprezentacji planszy
   * @param colsAndRows Ilość wierszy i kolumn dla klienta
   * @see game.Game#update(java.util.Observable, java.lang.Object) 
   */
  public void restartClientGameSettings(int colsAndRows) {
    
    console.clear();
    console.setMessageLn("Po\u0142\u0105czono z 2. klientem. Ustawienia gry dla trybu klienta "
                        +"zosta\u0142y pobrane z serwera.", new Color(0xa5, 0x2a, 0x2a));
    
    board = new BoardGraphics(colsAndRows);
    panelBoard.removeAll();
    panelBoard.add(board);
    revalidate();
    
    try {
      Thread.sleep(100);  
    }
    catch(InterruptedException e) {}
    
    // przesłanie do wątka gry nowych ustawien i referencji do nowej planszy
    gameSpy.sendObject("settings", settings);
    gameSpy.sendObject("board", board);

  }    

 
  /**
   * Metoda pobierająca referencję do obiektu konsoli
   * @return Referencja do obiektu konsoli
   */
  public Console getConsole() {
      
     return console; 
      
  }
  
  /**
   * Metoda pobierająca referencję do graficznej reprezentacji planszy
   * @return Referencja do graficznej reprezentacji planszy
   */
  public BoardGraphics getBoard() {
      
    return board; 
      
  }
  
  /**
   * Metoda pobierająca tryb bieżącej rozgrywki
   * @return Tryb bieżącej rozgrywki
   */
  public GameMode getGameMode() {
      
    return gameMode; 
      
  }  
  
  /**
   *Metoda pobierająca referencję do obiektu odtwarzającego dźwięki
   * @return Referencja do obiektu odtwarzającego dźwięki
   */
  public Sounds getSounds() {
      
    return sounds;  
      
  }

  /**
   * Metoda pobierająca referencję do ustawień gry
   * @return Referencja do ustawień gry
   */
  @Override
  public Settings getSettings() {
      
     return settings;  
      
  }
  
  /**
   * Metoda odpowiada: to nie serwer
   * @return false
   */
  @Override
  public boolean isServer() {
      
     return false;  
      
  }
  
  /**
   * Statyczna metoda ustawiająca temat(Ocean) LookAndFeel 
   */
  public static void setLookAndFeel() {
      
    MetalLookAndFeel.setCurrentTheme(new OceanTheme());
    try {
      UIManager.setLookAndFeel(new MetalLookAndFeel());
    }
    catch(Exception e) {
      System.err.println(e);
    }
    
    JFrame.setDefaultLookAndFeelDecorated(true);   
    JDialog.setDefaultLookAndFeelDecorated(true);   
      
  }  

  
   /**
   * Metoda ustawia referencje przekazane przez obserwatora
   * @param o Obserwowany obiekt 
   * @param object Przekazany obiekt
   */
   @Override
   public void update(Observable o, Object object) {
      
     AppObserver obs = (AppObserver)object;
     
     // przekazanie referencji do utworzonego gniazdka klienta
     switch (obs.getKey()) {
         
         case "socket": 
             
           socket = (Socket)obs.getObject();
           disconButton.setEnabled(true);
           menuGame.getNewGameItem().setEnabled(false);           
           
           break;
      
         
     }  
     
   }

    
}
  
