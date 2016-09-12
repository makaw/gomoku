/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import gui.BoardGraphics;
import java.util.Observable;

/**
 *
 * Szablon obiektu służącego do przekazywania informacji nt. zmiany ustawień gry, 
 * przy wykorzystaniu mechanizmu "Obserwatora" (Observer); służy do komunikacji z wątkiem GUI
 * 
 * @author Maciej Kawecki
 * 
 */
public class SettingsSpy extends Observable {
    
   /** Ilość wierszy i kolumn planszy */      
   protected int colsAndRows;  
   /** Ilość kamieni w rzędzie wymagana do wygranej */
   protected int piecesInRow;
   /** Czy do wygranej wymagane jest DOKŁADNIE n kamieni w rzędzie  */
   protected boolean piecesInRowStrict; 
   /** Referencja do obiektu, będącego graficzną reprezentacją planszy */
   protected BoardGraphics board;  
   /** Czy zmiana ma powodować restart rozgrywki */
   protected boolean restart;
 
   /**
    * Metoda używana przez wątek GUI, przesyła nowe ustawienia gry do obserwujących obiektów
    * @param settings Nowe ustawienia gry
    * @param board Referencja do nowej graficznej reprezentacji planszy
    * @param restart Czy ma być restartowana rozgrywka
    */
   public void setSettings(Settings settings, BoardGraphics board, boolean restart) {
       
     colsAndRows = settings.getColsAndRows();
     piecesInRow =  settings.getPiecesInRow();
     piecesInRowStrict = settings.getPiecesInRowStrict();
     this.board = board;
     this.restart = restart;
     
     setChanged();
     notifyObservers(this);
       
   }
   
   
   /**
    * Metoda używana przez wątek GUI, przesyła nowe ustawienia gry do obserwujących obiektów, 
    * powoduje zrestartowanie rozgrywki.
    * @param settings Nowe ustawienia gry
    * @param board Referencja do nowej graficznej reprezentacji planszy
    */
   public void setSettings(Settings settings, BoardGraphics board) {
       
      setSettings(settings, board, true); 
       
   }

   
    public boolean isRestart() {
        return restart;
    }

    public BoardGraphics getBoard() {
        return board;
    }
   
    
    
   
   
}
