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
   * @throws InterruptedException Problem z uruchomieniem wątku GUI
   * @throws InvocationTargetException Problem z uruchomieniem wątku GUI
   */
  private Gomoku() throws InterruptedException, InvocationTargetException {

    gameSpy = new AppObserver();
    settings = new Settings();
    settings.load();
      
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        gui = new GUI(gameSpy,settings); 
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
          settings.setGameSettings(s.getColsAndRows(), s.getPiecesInRow(), s.isComputerStarts());          
          gui.getSettings().setGameSettings(s.getColsAndRows(), s.getPiecesInRow(), s.isComputerStarts());  
          
          break;
     
     
          
     }
     
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
        
     } catch (InterruptedException | InvocationTargetException e) {
        System.err.println(Lang.get("StartGraphicsProblem", e));
     }
     
  }
  
   
}
