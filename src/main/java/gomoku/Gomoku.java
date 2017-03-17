/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import game.Game;
import game.GameState;
import gui.BoardGraphics;
import gui.GUI;
import gui.dialogs.RulesDialog;
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;
import network.Server;


/**
 *
 * Klasa główna zawierająca metodę main() - wywołanie interfejsu graficznego, 
 * uruchamianie rozgrywki, obsługa ewentualnej zmiany ustawień.
 * 
 * @author Maciej Kawecki
 * 
 */
public final class Gomoku implements Observer {
  
  /** Obiekt reprezentujący graficzny interfejs */
  private GUI gui;  
  /** Obiekt reprezentujący rozgrywkę */
  private Game game;
  /** Obiekt służący do komunikacji z wątkiem GUI w zakresie zmiany stanu gry */
  private AppObserver gameSpy;
  /** Obiekt przechowujący ustawienia gry */
  private final Settings settings;
  
  
  /**
   * Konstruktor klasy głównej, inicjalizacja obiektów, w tym bezpieczne wywołanie 
   * interfejsu graficznego, ustawienie obserwatorów do komunikacji wątków, pętla 
   * uruchamiająca rozgrywkę
   * @throws InterruptedException
   * @throws InvocationTargetException 
   */
  private Gomoku() throws InterruptedException, InvocationTargetException {

    gameSpy = new AppObserver();
    settings = new Settings();
      
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        gui = new GUI(gameSpy); 
      }
    });
    
    
    // na starcie odpalenie okna z wyborem trybu gry
    //new NewGameDialog(gui, true);
    new RulesDialog(gui);
  
    do {
    	

       game = new Game(gui.getBoard(), gui.getConsole(), gui.getSounds(), gameSpy);
        
       // ustawienie obserwatorów - dot. zmiany stanu gry oraz ustawień
       gameSpy.addObserver(game);
       gameSpy.addObserver(this);	

       // uruchomienie nowej gry 
       game.setSettings(settings);
       game.setGameMode(gui.getGameMode());
       game.run();
       // po zakończeniu gry, oczekiwanie na rozpoczęcie nowej
       do { } while (game.getGameState()!=GameState.RESTART);
       game.interrupt();
       
    }  while (true);
    
      
  }
  
  
   /**
   * Metoda ustawia referencje przekazane przez obserwatora
   * @param o Obserwowany obiekt 
   * @param object Przekazany obiekt
   */
   @Override
   public void update(Observable o, Object object) {
      
     AppObserver obs = (AppObserver)object;
     
     switch (obs.getKey()) {
   
          
        case "board":
            
          game.setBoard((BoardGraphics)obs.getObject());
        
          break;
          
        case "settings-main":
            
          Settings s = (Settings)obs.getObject();
          settings.setSettings(s.getColsAndRows(), s.getPiecesInRow(), s.getPiecesInRowStrict());
          gui.getSettings().setSettings(s.getColsAndRows(), s.getPiecesInRow(), s.getPiecesInRowStrict());
          break;
     
     
          
     }
     
  }  
  
  /**
   *   Statyczna metoda kończąca działanie programu
   */
  public static void quitGomoku() {
      
     System.exit(0); 
      
  }
  

  /** 
   * Metoda main wołana przez system w trakcie uruchamiania aplikacji, uruchomienie okna klienta.
   * @param args Argumenty przekazane do aplikacji. Wywołanie z parametrem -s 
   * spowoduje uruchomienie statycznej metody main() z klasy Server, czyli uruchomienie serwera.
   * @see network.Server
   */
  public static void main(final String[] args) {
    
     GUI.setLookAndFeel(); 
     try {
         
        if (args.length!=0 && args[0].startsWith("-s")) Server.main(args); 
        else new Gomoku();
        
     } catch (InterruptedException e) {
        System.err.println("Problem podczas wywo\u0142ania interfejsu graficznego: "+e);
     } catch (InvocationTargetException e) {
        System.err.println("Problem z wywo\u0142ania interfejsu graficznego: "+e);
     }
     
  }
  
   
}
