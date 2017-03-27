/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

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
   * Konstruktor (domyślne wartości)
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
      
     this(settings.colsAndRows, settings.piecesInRow, IConf.DEFAULT_COMPUTER_STARTS);
     
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
  
  
  /**
   * Zapis ustawień do pliku
   * @param server True jeżeli ustawienia serwera
   */  
  public void save(boolean server) {
	 
	try {
	  Properties props = new Properties();
	  props.setProperty("colsAndRows", String.valueOf(colsAndRows));
	  props.setProperty("piecesInRow", String.valueOf(piecesInRow));
	  props.setProperty("computerStarts", String.valueOf(computerStarts));
	  props.setProperty("localeIndex", String.valueOf(Lang.getLocaleIndex()));
	  File f = new File("gomoku" + (server ? "-server" : "") + "-settings.properties");
	  OutputStream out = new FileOutputStream(f);
	  props.store(out, "Gomoku " + (server ? "server " : "") + "settings");
	}
	catch (Exception e ) {
	  System.err.println(e);
	}
  
  }
  
  
  /**
   * Załadowanie ustawień z pliku
   * @param server True jeżeli ustawienia serwera
   */
  public void load(boolean server) {
	  
	 Properties props = new Properties();
	 
	 try {
		File f = new File("gomoku" + (server ? "-server" : "") + "-settings.properties");
	    props.load(new FileInputStream(f));
	 }
	 catch (Exception e) { }
	  
	 try {
		int val = Integer.parseInt(props.getProperty("colsAndRows"));
		if (val < IConf.MIN_COLS_AND_ROWS || val > IConf.MAX_COLS_AND_ROWS) throw new Exception();
		if (val%2 == 0) throw new Exception();
		colsAndRows = val;
	 }
	 catch (Exception e) { }
	 
	 try {
		int val = Integer.parseInt(props.getProperty("piecesInRow"));
		if (val < IConf.MIN_PIECES_IN_ROW || val > IConf.MAX_PIECES_IN_ROW) throw new Exception();
		piecesInRow = val;
	 }
	 catch (Exception e) { }	 
	 
	 if (props.containsKey("computerStarts"))
		 computerStarts = Boolean.valueOf(props.getProperty("computerStarts"));
	 
	 try {
		int val = Integer.parseInt(props.getProperty("localeIndex"));
		Lang.setLocale(val);
	 }
	 catch (Exception e) { }	
	 
  }
  
  
	
}
