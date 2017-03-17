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
   
  private static final long serialVersionUID = 1L;  
  
    
    
  /**
   * Konstruktor obiektu, ustawiający domyślne wartości
   */
  public Settings() {
      
     super();
      
  }        
    
  
  /**
   * Konstruktor obiektu, ustawiający podane wartości
   * @param colsAndRows Ilość wierszy i kolumn planszy
   * @param piecesInRow Ilość kamieni w rzędzie wymagana do wygranej 
   * @param piecesInRowStrict Czy do wygranej wymagane jest DOKŁADNIE n kamieni w rzędzie
   */
  public Settings(int colsAndRows, int piecesInRow, boolean piecesInRowStrict) {
      
     super(colsAndRows, piecesInRow, piecesInRowStrict);
      
  }        
    
  /**
   * Konstruktor obiektu, ustawiający podane wartości
   * @param settings Podane ustawienia jako obiekt klasy bazowej
   */
  public Settings(SettingsVar settings) {
      
     this.colsAndRows = settings.colsAndRows;
     this.piecesInRow = settings.piecesInRow;
     this.piecesInRowStrict = settings.piecesInRowStrict;
      
  }        
    
  
  /**
   * Metoda zmieniająca bieżące ustawienia na przekazane w parametrach (o ile się różnią)
   * @param colsAndRows Nowa ilość wierszy i kolumn planszy
   * @param piecesInRow Nowa ilość kamieni w rzędzie wymaganych do wygranej
   * @param piecesInRowStrict Nowe ustawienie, czy do wygranej wymagane jest dokładnie n kamieni
   * @return true jeżeli coś zmieniono, false jeżeli nic się nie zmieniło
   */
  public boolean setSettings(int colsAndRows, int piecesInRow, boolean piecesInRowStrict) {
      
     // sprawdzenie czy cos zmieniono 
     if (colsAndRows==this.colsAndRows && piecesInRow==this.piecesInRow 
         && piecesInRowStrict==this.piecesInRowStrict)  return false;
      
     this.colsAndRows = colsAndRows;
     this.piecesInRow = piecesInRow;
     this.piecesInRowStrict = piecesInRowStrict;
      
     return true;
     
  }
  
  /**
   * Metoda zwracająca aktualną ilość kolumn i wierszy planszy
   * @return Ilość wierszy i kolumn planszy
   */
  public int getColsAndRows() {
      
     return colsAndRows;  
      
  }
  
  /**
   * Metoda zwracająca ilość wszystkich pól planszy
   * @return Ilość wszystkich pól planszy
   */
  public int getFieldsAmount() {
  
     return colsAndRows*colsAndRows;
     
  }
  
  /**
   * Metoda zwracająca aktualną ilość kamieni w rzędzie wymaganą do wygranej
   * @return Ilość kamieni w rzędzie wymagana do wygranej
   */  
  public int getPiecesInRow() {
      
     return piecesInRow;
     
  }
  
  /**
   * Metoda zwracająca ustawienie, czy do wygranej wymagane jest dokładnie n kamieni
   * @return true jeżeli musi być dokładnie n kamieni w rzędzie, false jeżeli może być ich więcej 
   */
  public boolean getPiecesInRowStrict() {
   
     return piecesInRowStrict; 
      
  }

  
  
    
}
