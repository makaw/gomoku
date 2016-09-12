/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import gomoku.Settings;
import java.util.Observable;

/**
 *
 * Szablon obiektu służącego do przekazywania informacji nt. zmiany ustawień gry, 
 * przy wykorzystaniu mechanizmu "Obserwatora" (Observer); służy do komunikacji z wątkiem GUI 
 * serwera
 * 
 * @author Maciej Kawecki
 * 
 */
public class ServerSettingsSpy extends Observable {
    
   /** Ilość wierszy i kolumn planszy */      
   protected int colsAndRows;  
   /** Ilość kamieni w rzędzie wymagana do wygranej */
   protected int piecesInRow;
   /** Czy do wygranej wymagane jest DOKŁADNIE n kamieni w rzędzie  */
   protected boolean piecesInRowStrict; 
   /** Referencja do obiektu, będącego graficzną reprezentacją planszy */
 
   /**
    * Metoda używana przez wątek GUI, przesyła nowe ustawienia gry do obserwujących obiektów
    * @param settings Nowe ustawienia gry
    */
   public void setSettings(Settings settings) {
       
     colsAndRows = settings.getColsAndRows();
     piecesInRow =  settings.getPiecesInRow();
     piecesInRowStrict = settings.getPiecesInRowStrict();
     
     setChanged();
     notifyObservers(this);
       
   }
   
   
   
}
