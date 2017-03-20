/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package gomoku;

/**
 *
 * Interfejs konfiguracyjny
 * 
 * @author Maciej Kawecki
 * 
 */
public interface IConf {
  
   /** Domyślna ilość wierszy i kolumn planszy */
   int DEFAULT_COLS_AND_ROWS = 11;
   /** Domyślna ilość kamieni w rzędzie wymagana do wygranej */
   int DEFAULT_PIECES_IN_ROW = 5;
   /** Domyślnie - czy włączony jest dźwięk */
   boolean DEFAULT_ENABLE_SOUND = true;      
   
   /** Czy komputer zaczyna grę (czarne) */
   boolean DEFAULT_COMPUTER_STARTS = true;
   
   /** Domyślny adres IP serwera */
   String DEFAULT_HOST = "127.0.0.1";          
   /** Używany port serwera */  
   int SERVER_PORT = 4444;  
   
    
   /** Nr wersji aplikacji */
   String VERSION = "0.6a"; 
   /** Nr wersji serwera aplikacji */
   String VERSION_SERVER = "0.5";
   /** Rok(lata) stworzenia aplikacji */
   String YEARS = "2015-17";
      
    
}
