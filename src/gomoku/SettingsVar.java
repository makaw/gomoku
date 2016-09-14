/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import java.io.Serializable;


/**
 *
 * Szablon bazowego podstawowego obiektu przechowującego ustawienia gry
 * 
 * @author Maciej Kawecki
 * 
 */
public class SettingsVar implements Serializable {
      
  /** Ilość wierszy i kolumn planszy */  
  protected int colsAndRows;  
  /** Ilość kamieni w rzędzie wymagana do wygranej */
  protected int piecesInRow;
  /** Czy do wygranej wymagane jest DOKŁADNIE n kamieni w rzędzie  */
  protected boolean piecesInRowStrict; 

    
  private final static long serialVersionUID = 1L;
  
  /**
   * Konstruktor obiektu, ustawiający domyślne wartości
   */
  public SettingsVar() {
      
     this(IConf.DEFAULT_COLS_AND_ROWS, IConf.DEFAULT_PIECES_IN_ROW, IConf.DEFAULT_PIECES_IN_ROW_STRICT);
      
  }    
  
  
  /**
   * Konstruktor obiektu, ustawiający podane wartości
   * @param colsAndRows Ilość wierszy i kolumn planszy
   * @param piecesInRow Ilość kamieni w rzędzie wymagana do wygranej 
   * @param piecesInRowStrict Czy do wygranej wymagane jest DOKŁADNIE n kamieni w rzędzie
   */
  public SettingsVar(int colsAndRows, int piecesInRow, boolean piecesInRowStrict) {
      
     this.colsAndRows = colsAndRows;
     this.piecesInRow = piecesInRow;
     this.piecesInRowStrict = piecesInRowStrict;
      
  }      
    
    
}
