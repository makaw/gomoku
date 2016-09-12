/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gui;


import game.Game;
import game.GameStateSpy;
import gomoku.IConf;
import gomoku.Settings;
import gomoku.SettingsSpy;
import gui.dialogs.NewGameDialog;
import gui.dialogs.PromptDialog;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import network.Client;
import network.Command;
import sun.net.www.content.text.plain;
import sun.rmi.server.UnicastRef;

/**
 *
 * Szablon obiektu budującego graficzny interfejs użytkownika
 * 
 * @author Maciej Kawecki
 * 
 */
@SuppressWarnings("serial")
public class GUI extends JFrame implements IBaseGUI {
  
  
  /** Szerokość okna aplikacji w pikselach */
  private static final int F_WIDTH = 395;
  /** Wysokość okna aplikacji w pikselach */
  private static final int F_HEIGHT = 560;       
  
  /** Obiekt będący graficzną reprezentacją planszy */  
  private BoardGraphics board;
  /** Obiekt reprezentujący konsolę */
  private final Console console;
  /** Wybrany tryb gry */
  private byte gameMode;
  /** Obiekt służący do odtwarzania dźwięków */  
  private Sounds sounds;
  /** Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym przebieg gry  w zakresie zmiany stanu gry */
  private final GameStateSpy gameStateSpy;
  /** Obiekt przechowujący ustawienia gry */
  private final Settings settings;
  /** Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym przebieg gry w zakresie zmiany ustawień */
  private final SettingsSpy settingsSpy;
  /** Panel w którym zawiera się graficzna reprezentacja planszy */
  private final JPanel panelBoard;
  /** Obiekt przycisku do wysyłania wiadomości w grze sieciowej, uchwyt jest potrzebny do
   * blokowania i odblokowywania przycisku */
  private final JButton msgButton;
  
  /**
   * Konstruktor budujący graficzny interfejs użytkownika i wywołujący 
   * okno z wyborem trybu nowej gry
   * @param gameStateSpy Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym 
   * przebieg gry w zakresie zmiany stanu gry
   * @param settingsSpy Referencja do obiektu służącego do komunikacji z wątkiem kontrolującym 
   * przebieg gry w zakresie zmiany ustawień
   */  
  public GUI(final GameStateSpy gameStateSpy, SettingsSpy settingsSpy) {
      
    super("Gomoku");  
    
    this.gameStateSpy = gameStateSpy;
    this.settingsSpy = settingsSpy;
    
    settings = new Settings();
    
    setIconImage(Images.getImage("icon_small.png"));  
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
    msgButton = new JButton("", Images.getIcon("message.png"));
    msgButton.setFocusPainted(false);
    msgButton.setToolTipText("Wy\u015blij wiadomo\u015b\u0107");
    msgButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        String msg = new PromptDialog(GUI.this, 
                                      "Tre\u015b\u0107 wiadomo\u015bci:").getMessage();
        if (msg!=null && !msg.isEmpty()) {
          gameStateSpy.sendMessage(msg);
        } 
        
       }
    });   
    // domyślnie zablokowany, ma być aktywny tylko podczas gry sieciowej, włączanie w interfejsie Console
    msgButton.setEnabled(false);
     
    // przycisk do włączania/wyłączania dźwięku
    final JButton sndButton = new JButton("", Images.getIcon(IConf.DEFAULT_ENABLE_SOUND ? "sound.png" : "mute.png"));
    sndButton.setFocusPainted(false);
    sndButton.setToolTipText("D\u017awi\u0119k w\u0142./wy\u0142.");
    sndButton.addActionListener(new ActionListener() {
       @Override
       public void actionPerformed(final ActionEvent e) {
         sndButton.setIcon(sounds.getEnabled() ? Images.getIcon("mute.png") : Images.getIcon("sound.png"));
         sounds.toggleSound();
       }
    });        

    panelButtons.add(msgButton);
    panelButtons.add(sndButton);
    // puste miejsce
    panelButtons.add(new JPanel());

    panelButtons.setPreferredSize(new Dimension(40, 104));    
    gb.setConstraints(panelButtons, gbc);
    panelConsole.add(panelButtons);
     
    gbc.weighty = 1.0;
    gbc.weightx = 2.0;
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.gridheight = 1;
 
    console = new Console(msgButton);
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
    menu.add(new MenuGame(this));
    menu.add(new MenuHelp(this));
    setJMenuBar(menu);
    
    pack();
    setSize(F_WIDTH, F_HEIGHT);
    setResizable(false);
    setVisible(true);

    // witamy sie :-)
    console.setMessageLn("Dzie\u0144 dobry :-)", Color.BLACK); 

  } 

  
  /**
   * Metoda obsługująca uruchomienie nowej gry: zmiana stanu (i ew. adresu IP serwera), powiadomienie wątku 
   *  kontrolującego przebieg gry o zmianie, wyczyszczenie graficznej planszy i konsoli
   * @param gameMode Wybrany przez użytkownika tryb nowej gry
   * @param serverIP Adres IP serwera
   * @see game.GameStateSpy
   * @see game.Game#update(java.util.Observable, java.lang.Object) 
   */
  @Override
  public void restartGame(byte gameMode, String serverIP) {    
      
    if (this.gameMode == Game.NETWORK_GAME) {
        
       try {
          Client client = new Client(serverIP, gameStateSpy, console);  
          client.sendCommand(new Command(Command.CMD_EXIT));
       }
       catch (Exception e) { System.err.println(e); }
       gameStateSpy.setState(Game.WAIT, serverIP);
        
    }
      
    this.gameMode = gameMode;
    
    // przesłanie do wątka gry informacji o zmianie stanu
    gameStateSpy.setState(Game.RESTART, serverIP);
    board.clear();
    console.clear();   
    
    if (gameMode == Game.NETWORK_GAME) 
      console.setMessageLn("Wybrano tryb sieciowy. Oczekiwanie na do\u0142\u0105czenie 2. gracza ....", Color.DARK_GRAY);
              
    else
      console.setMessageLn("Wybrano tryb gry.", Color.GRAY);
    
  }
  
  /**
   * Metoda obsługująca zmianę ustawień przez użytkownika: zatrzymanie bieżącej rozgrywki, 
   * powiadomienie wątku kontrolującego przebieg gry o zmianie stanu, wyczyszczenie konsoli, przygotowanie 
   * nowej graficznej planszy, oraz przesłanie do wątku kontrolującego przebieg gry nowych ustawień i 
   * referencji do nowej graficznej reprezentacji planszy
   * @see gomoku.SettingsSpy
   * @see game.GameStateSpy
   * @see game.Gomoku#update(java.util.Observable, java.lang.Object) 
   * @see game.Game#update(java.util.Observable, java.lang.Object) 
   */
  @Override
  public void restartGameSettings() {

   // przesłanie do wątka gry informacji o zmianie stanu
    gameStateSpy.setState(Game.WAIT);
    
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
    settingsSpy.setSettings(settings, board);
    gameMode = new NewGameDialog(this).getGameMode();

  }  
  
  
 /**
   * Metoda obsługująca zmianę ustawień dla trybu klienta (ustawienia pobrane z serwera):
   * wyczyszczenie konsoli, przygotowanie nowej graficznej planszy,
   * oraz przesłanie do wątku kontrolującego przebieg gry referencji do nowej
   * graficznej reprezentacji planszy
   * @param colsAndRows Ilość wierszy i kolumn dla klienta
   * @see gomoku.SettingsSpy
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
    settingsSpy.setSettings(settings, board, false);

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
  public byte getGameMode() {
      
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


    
}
  
