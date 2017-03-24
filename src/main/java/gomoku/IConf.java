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
  
   /** Najmniejsza możliwa liczba wierszy (nieparzyste) */
   int MIN_COLS_AND_ROWS = 7;
   /** Największa możliwa liczba wierszy (nieparzyste) */
   int MAX_COLS_AND_ROWS = 15;   
   /** Domyślna liczba wierszy i kolumn planszy */
   int DEFAULT_COLS_AND_ROWS = 11;
   
   /** Najmniejsza możliwa liczba kamieni w rzędzie wymagana do wygranej */
   int MIN_PIECES_IN_ROW = 3;
   /** Największa możliwa liczba kamieni w rzędzie */
   int MAX_PIECES_IN_ROW = 5;
   /** Domyślna liczba kamieni w rzędzie */
   int DEFAULT_PIECES_IN_ROW = 5;
   
   /** Czy komputer zaczyna grę (czarne) */
   boolean DEFAULT_COMPUTER_STARTS = true;  

   /** Domyślnie - czy włączony jest dźwięk */
   boolean DEFAULT_ENABLE_SOUND = true;                 
   
   /** Domyślny adres IP serwera */
   String DEFAULT_HOST = "127.0.0.1";          
   /** Używany port serwera */  
   int SERVER_PORT = 4444;  
   
    
   /** Nr wersji aplikacji */
   String VERSION = "0.6"; 
   /** Nr wersji serwera aplikacji */
   String VERSION_SERVER = "0.6";
   /** Rok(lata) stworzenia aplikacji */
   String YEARS = "2015-17";
      
    
}
