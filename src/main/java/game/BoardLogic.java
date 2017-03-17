/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.util.ArrayList;
import java.util.List;

import gomoku.Settings;


/**
 *
 * Szablon obiektu odpowiedzialnego za warstwę logiczną planszy, tj. model planszy, 
 * oraz warunki wygranej
 * 
 * @author Maciej Kawecki
 * 
 */
public class BoardLogic {

  /** Ilość pozostałych na planszy wolnych pól */
  protected int freeFieldsAmount;  
  /** Referencja do ustawień gry */
  private final Settings settings;
  /** Wewnętrzna tablica zawierająca stan pól planszy */
  private final BoardField[] fields;
  /** Referencja do ostatnio zapełnionego pola planszy */
  protected BoardField lastField;
  
  /**
   * Konstruktor obiektu logicznej warstwy planszy, tworzący wewnętrzną tablicę 
   * stanu pól planszy, i inicjujący pozostałe zmienne i obiekty
   * @param settings Referencja do obiektu zawierającego ustawienia gry
   */
  public BoardLogic(Settings settings) {
    
    this.settings = settings;
    
    freeFieldsAmount = settings.getFieldsAmount();
    
    fields = new BoardField[freeFieldsAmount];
    
    for (int a=0; a<settings.getColsAndRows(); a++) {
        
      int indeks = a*settings.getColsAndRows();
     
      for (int b=0; b<settings.getColsAndRows(); b++) 
       fields[indeks+b] = new BoardField(a, b);
     
    }    
    
  }
   
  
  
  /**
   * Metoda pobierająca stan wskazanego pola planszy
   * @param a Indeks a (kolumna) sprawdzanego pola
   * @param b Indeks b (wiersz) sprawdzanego pola
   * @return wartość (stan) pola (puste/biały/czarny/błąd)
   */
  public byte getFieldState(int a, int b) {
    
    byte tmp;  
      
    try {
      tmp = fields[getIndex(a, b)].getState();
    }
    // wyjątek - a lub b poza zakresem
    catch (Exception e) {
      tmp = -1; 
    }
    
    return tmp;
      
  }
  
  /**
   * Metoda próbująca zmienić stan wskazanego pola planszy; w razie sukcesu 
   * zapamiętująca indeksy i stan do czasu następnej zmiany.
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param state Docelowa wartość(stan) pola
   * @return true, jeżeli stan pola został zmieniony, false jeżeli nie
   */
  public boolean setFieldState(int a, int b, byte state) {
    
    boolean changed = false;  

    try {
         
      int index = getIndex(a, b);  
      fields[index].setState(state); 
      
      freeFieldsAmount--;
      changed = true;
      lastField = new BoardField(a, b, state);      
      
    }
    
    // wyjątek - a lub b poza zakresem
    catch (Exception e) {   }

    
    return changed;

    
  }    
  
  

  /**
   * Metoda sprawdzająca, czy wskazane pole zawiera kamień, który wchodzi 
   * w skład "wygrywającego" rzędu w danym kolorze. Sprawdzane są od góry 
   * 4 kierunki: skos góra,  pion, poziom, skos dół. Sprawdzane jest też (w zależności 
   * od ustawień gry), czy rząd zawiera dokładnie wymaganą ilość kamieni.
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Sprawdzany kolor
   * @return null jeżeli nie ma wygranej, lub w razie wygranej lista par indeksów 
   * pól wchodzących w skład  wygrywającego rzędu, w celu oznaczenia ich na planszy.
   */
  public List<BoardField> getPieceRows(int a, int b, Byte pColor) {
      
     if (settings.getFieldsAmount() - freeFieldsAmount < settings.getPiecesInRow())
       return null;
      
     int i,j,dir;
     List<BoardField> winRow = new ArrayList<>();
     Integer cnt = 0;   

     // przeglądanie od góry w 4 kierunkach: 0-skos góra, 1-pion, 2-poziom, 3-skos dół
     for (dir=0;dir<=3;dir++) {
      
       for (i=-settings.getPiecesInRow()+1;i<settings.getPiecesInRow();i++) {
       
         // jeżeli pion to a jest stałe, jezeli poziom to b jest stałe
         if (pColor.equals(getFieldState(dir!=1?a+i:a, dir!=2?(dir!=3?b+i:b-i):b))) cnt++;
         else cnt = 0;
         // jeżeli jest już odpowiednia ilość kamieni, to wyjście z pętli         
         if (cnt.equals(settings.getPiecesInRow())) break;
   
       }
     
                 
       // jeszcze sprawdzenie czy w rzędzie nie ma więcej niż trzeba, bo ma być dokładnie np.5
       if (settings.getPiecesInRowStrict()) {
         i++; j = -settings.getPiecesInRow();
         if (pColor.equals(getFieldState(dir!=1?a+i:a, dir!=2?(dir!=3?b+i:b-i):b))
           || pColor.equals(getFieldState(dir!=1?a+j:a, dir!=2?(dir!=3?b+j:b-j):b))) {
           cnt = 0;
         }
         i--;
       }
       
       // jeżeli wygrana to zwrócenie listy indeksów pol
       if (cnt.equals(settings.getPiecesInRow())) {
      
         for (j=0;j<settings.getPiecesInRow();j++)
           { winRow.add(fields[getIndex((dir!=1?a+i-j:a), (dir!=2?(dir!=3?b+i-j:b-i+j):b))]); }
         
         return winRow;
       
       }

       winRow.clear();    
       cnt = 0;
       
     }

     return null;
      
  }
  
  
  /**
   * Metoda sprawdzająca, czy ostatnio wypełnione pole zawiera kamień,
   * który wchodzi w skład "wygrywającego" rzędu w danym kolorze
   * @return null jeżeli nie ma wygranej, lub w razie wygranej lista par indeksów 
   * pól wchodzących w skład  wygrywającego rzędu, w celu oznaczenia ich na planszy.
   * @see BoardLogic#getPieceRows(int, int, java.lang.Byte) 
   */
  public List<BoardField> getPieceRows() {
      
    return getPieceRows(lastField.getA(), lastField.getB(), lastField.getState()); 
      
  }
  
  /**
   * Funkcja zwraca indeks w wewnętrznej tablicy pól na podstawie współrzędnych pola na planszy
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @return Indeks w wewnętrznej tablicy pól
   */
  private int getIndex(int a, int b) {
      
     return a*settings.getColsAndRows()+b;  
      
  }
  
  
  //-----------------------------------------------------------------------------------
  // metody dodatkowe dla Minimax
  
  
  /**
   * Metoda zwracająca ilość kolumn i wierszy planszy z bieżących ustawień gry (dla PlayerComputer)
   * @return Ilość kolumn i wierszy planszy.
   */
  protected int getColsAndRows() {
      
    return settings.getColsAndRows();
      
  }
  
  /**
   * Metoda zwracająca ilość wszystkich pól planszy
   * @return Ilość wszystkich pól planszy
   */
  protected int getFieldsAmount() {
      
     return settings.getFieldsAmount();
      
  }
  
  /**
   * Metoda zwracająca listę pustych pól planszy
   * @return Lista pustych pól planszy
   */
  protected List<BoardField> getEmptyFields() {
      
     ArrayList<BoardField> emptyFields = new ArrayList<>();

     for (int i=0; i<settings.getFieldsAmount();i++) {
         
       if (fields[i].getState()==BoardField.EMPTY) emptyFields.add(fields[i]);  
         
     }
     
     return emptyFields;
      
  }
  
  
  /**
   * TO_DO! 
   * Punktacja sytuacji na planszy (dla MiniMax). 
   * @param pColor Kolor gracza dla którego liczona jest punktacja
   * @return Punktacja planszy dla danego gracza
   */
  protected int getScore(byte pColor) {
        
    return 1;
      
  }
  
  
  
}
  
  