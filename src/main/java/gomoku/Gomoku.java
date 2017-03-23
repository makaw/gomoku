/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import java.lang.reflect.InvocationTargetException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.SwingUtilities;

import game.Game;
import gui.BoardGraphics;
import gui.GUI;
import gui.dialogs.RulesDialog;
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
  /** Obserwator - komunikacja z wątkiem GUI w zakresie zmiany stanu gry */
  private AppObserver gameSpy;
  /** Ustawienia gry */
  private final Settings settings;
  
  
  /**
   * Konstruktor  
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
    
    
    new RulesDialog(gui);
    

    gameSpy.addObserver(this);	
  
    do {    	
    	
       game = new Game(gui.getBoard(), gui.getConsole(), gui.getSounds(), gameSpy);
        
       // ustawienie obserwatorów - dot. zmiany stanu gry oraz ustawień
       gameSpy.addObserver(game);

       // uruchomienie nowej gry 
       game.setSettings(settings);
       game.setGameMode(gui.getGameMode());
       game.start();
       game.join();
       gameSpy.deleteObserver(game);
       
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
          settings.setSettings(s.getColsAndRows(), s.getPiecesInRow(), s.isComputerStarts());
          gui.getSettings().setSettings(s.getColsAndRows(), s.getPiecesInRow(), s.isComputerStarts());                    
          
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
   * Metoda main, uruchomienie okna klienta.
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
        System.err.println("Problem z wywo\u0142aniem interfejsu graficznego: "+e);        
     }
     
  }
  
   
}
