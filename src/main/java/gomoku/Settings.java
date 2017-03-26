/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

/**
 *
 * Szablon obiektu przechowywującego ustawienia gry.
 * 
 * @author Maciej Kawecki
 * 
 */
public class Settings extends SettingsVar {
   
  /** Czy komputer zaczyna grę */
  private boolean computerStarts = IConf.DEFAULT_COMPUTER_STARTS;


  private static final long serialVersionUID = 1L;  
    
  /**
   * Konstruktor, ustawiający domyślne wartości
   */
  public Settings() {
      
     super();
      
  }        
    
  
  /**
   * Konstruktor 
   * @param colsAndRows Ilość wierszy i kolumn planszy
   * @param piecesInRow Ilość kamieni w rzędzie wymagana do wygranej 
   */
  public Settings(int colsAndRows, int piecesInRow, boolean computerStarts) {
      
     super(colsAndRows, piecesInRow);
     this.computerStarts = computerStarts; 
     
  }        
    
  /**
   * Konstruktor 
   * @param settings Podane ustawienia jako obiekt klasy bazowej
   */
  public Settings(SettingsVar settings) {
      
     this.colsAndRows = settings.colsAndRows;
     this.piecesInRow = settings.piecesInRow;
     this.computerStarts = IConf.DEFAULT_COMPUTER_STARTS;
     
  }        
    
  
  /**
   * Metoda zmieniająca bieżące ustawienia na przekazane w parametrach (o ile się różnią)
   * @param colsAndRows Nowa ilość wierszy i kolumn planszy
   * @param piecesInRow Nowa ilość kamieni w rzędzie wymaganych do wygranej
   * @param computerStarts Czy komputer rozpoczyna
   * @return true jeżeli coś zmieniono, false jeżeli nic się nie zmieniło
   */
  public boolean setGameSettings(int colsAndRows, int piecesInRow, boolean computerStarts) {
      
     // sprawdzenie czy cos zmieniono 
     if (colsAndRows == this.colsAndRows && piecesInRow == this.piecesInRow
    		 && computerStarts == this.computerStarts)  return false;
      
     this.colsAndRows = colsAndRows;
     this.piecesInRow = piecesInRow;
     this.computerStarts = computerStarts;
      
     return true;
     
  }
  
  public boolean setGameSettings(int colsAndRows, int piecesInRow) {
	  return setGameSettings(colsAndRows, piecesInRow, IConf.DEFAULT_COMPUTER_STARTS);
  }
  

  public int getColsAndRows() {
      
     return colsAndRows;  
      
  }
  

  public int getFieldsAmount() {
  
     return colsAndRows*colsAndRows;
     
  }
  

  public int getPiecesInRow() {
      
     return piecesInRow;
     
  }


  public boolean isComputerStarts() {
	return computerStarts;
  }


  public void setComputerStarts(boolean computerStarts) {
	this.computerStarts = computerStarts;
  }
  
  
    
}
