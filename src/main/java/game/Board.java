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
 * Warstwa logiczna planszy 
 * 
 * @author Maciej Kawecki
 * 
 */
public class Board {

  /** Ilość pozostałych na planszy wolnych pól */
  protected int freeFieldsAmount;  
  /** Referencja do ustawień gry */
  private final Settings settings;
  /** Wewnętrzna tablica zawierająca stan pól planszy */
  private final BoardField[] fields;
  /** Referencja do ostatnio zapełnionego pola planszy */
  private BoardField lastField;  
  /** Lista pustych pól */
  private final List<BoardField> emptyFields;
  /** Aktualny stan planszy  */
  private final BoardScoring scoring;
  
  
  /**
   * Konstruktor
   * @param settings Referencja do obiektu ustawień gry
   */
  public Board(Settings settings) {
    
    this.settings = settings;
    
    freeFieldsAmount = settings.getFieldsAmount();
    emptyFields = new ArrayList<>();
    fields = new BoardField[freeFieldsAmount];
    
    for (int a=0; a<settings.getColsAndRows(); a++) {
        
      int indeks = a*settings.getColsAndRows();     
      for (int b=0; b<settings.getColsAndRows(); b++) {    	
    	 fields[indeks+b] = new BoardField(a, b);
    	 emptyFields.add(fields[indeks+b]);
      }

    }        
	
    scoring = new BoardScoring(settings);
    
  }
   
  
  
  
  /**
   * Metoda pobierająca stan wskazanego pola planszy
   * @param a Indeks a (kolumna) sprawdzanego pola
   * @param b Indeks b (wiersz) sprawdzanego pola
   * @return wartość (stan) pola (puste/biały/czarny/błąd)
   */
  public BoardFieldState getFieldState(int a, int b) {
    
    BoardFieldState tmp;  
      
    try {
      tmp = fields[getIndex(a, b)].getState();
    }
    // wyjątek - a lub b poza zakresem
    catch (Exception e) {
      tmp = null; 
    }
    
    return tmp;
      
  }
  
  
  /**
   * Metoda próbująca zmienić stan wskazanego pola planszy
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param state Docelowa wartość(stan) pola
   * @return true, jeżeli stan pola został zmieniony, false jeżeli nie
   */
  public boolean setFieldState(int a, int b, BoardFieldState state) {
    
    boolean changed = false;  

    try {
         
      int index = getIndex(a, b);  
      
      if (state != BoardFieldState.EMPTY) {
    	freeFieldsAmount--;
    	emptyFields.remove(fields[index]);
      }
      else {
    	freeFieldsAmount++;
    	emptyFields.add(fields[index]);
      }
      
      fields[index].setState(state); 
      
      
      changed = true;
      lastField = fields[index];
      
      scoring.update(a, b, state);
      
    }
    
    // wyjątek - a lub b poza zakresem
    catch (Exception e) {   }
    
    return changed;

    
  }    
  
  

  /**
   * Metoda znajdująca "wygrywający" rząd zawierający wskazane pole.
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param pColor Sprawdzany kolor
   * @return null jeżeli nie ma wygranej, lub w razie wygranej lista par indeksów 
   * pól wchodzących w skład  wygrywającego rzędu, w celu oznaczenia ich na planszy.
   */
  private List<BoardField> getWinningRow(int a, int b, BoardFieldState pColor) {
      
	 int piecesNum = settings.getPiecesInRow(); 
	  
     if (settings.getFieldsAmount() - freeFieldsAmount < piecesNum)
       return null;
     
     if (!scoring.hasWon(pColor)) return null;
     
     int i,j,dir;
     List<BoardField> winRow = new ArrayList<>();
     Integer cnt = 0;   

     // przeglądanie od góry w 4 kierunkach: 0-skos góra, 1-pion, 2-poziom, 3-skos dół
     for (dir=0; dir<=3; dir++) {
      
       for (i=-piecesNum+1; i<piecesNum; i++) {
       
    	 // wsp. pola: jeżeli pion to a jest stałe, jezeli poziom to b jest stałe  
    	 int a2 = dir!=1?a+i:a;
    	 int b2 = dir!=2?(dir!=3?b+i:b-i):b;
    	 boolean checked = pColor.equals(getFieldState(a2, b2));
    	           
         if (checked && a2<settings.getColsAndRows() && b2<settings.getColsAndRows() && a2>=0 && b2>=0) {
           	 cnt++; 
         }
         else cnt = 0;
         
         // jeżeli jest już odpowiednia ilość kamieni, to wyjście z pętli         
         if (cnt.equals(piecesNum)) break;
   
       }
     
       
       // jeżeli wygrana to zwrócenie listy indeksów pol
       if (cnt.equals(piecesNum)) {
      
         for (j=0; j<piecesNum; j++) {
        	 
           BoardField fieldTmp = fields[getIndex((dir!=1?a+i-j:a), (dir!=2?(dir!=3?b+i-j:b-i+j):b))];	 
         
           winRow.add(fieldTmp); 
           
         }
         
         return winRow;
       
       }

       winRow.clear();    
       cnt = 0;
       
     }

     return null;
      
  }
  
  
  
  /**
   * Metoda znajdująca "wygrywający" rząd zawierający pole z ostatniego ruchu. 
   * @return null jeżeli nie ma wygranej, lub w razie wygranej lista par indeksów 
   * pól wchodzących w skład  wygrywającego rzędu, w celu oznaczenia ich na planszy.
   * @see Board#getWinningRow(int, int, BoardFieldState)
   */
  public List<BoardField> getWinningRow() {
    
	try {
      return getWinningRow(lastField.getA(), lastField.getB(), lastField.getState());
	}
	catch (NullPointerException e) { return null; }
      
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
  
  

  
  /**
   * Metoda zwracająca nazwę (np.A1) wskazanego pola planszy
   * @param field Pole planszy
   * @return Nazwa (np.A1) wskazanego pola
   */
  public String getFieldName(BoardField field) {
      
     return Character.toString((char)('A' + field.getA()))
    		 + Integer.toString(settings.getColsAndRows() - field.getB());
      
  }  
  
  
  /**
   * Metoda zwracająca nazwę (np.A1) pola planszy z ostatniego ruchu
   * @return Nazwa (np.A1) pola
   */
  public String getLastFieldName() {
      
     return getFieldName(lastField);
      
  }    
  
  
  //-----------------------------------------------------------------------------------
  // metody dodatkowe dla AI

  
  /**   
   * Punktacja sytuacji na planszy
   * @param pColor Kolor gracza dla którego liczona jest punktacja
   * @return Aktualna punktacja planszy dla danego gracza
   */  
  protected int getScore(BoardFieldState pColor) {
	  return scoring.getScore(pColor);
  }
  
 
  protected int getColsAndRows() {	  
	 return settings.getColsAndRows();  	  
  }
  
  protected int getFreeFieldsAmount() {
	 return freeFieldsAmount;
  }
  
  protected int getFieldsAmount() {
	 return settings.getFieldsAmount();
  }
  
  protected void setLastField(BoardField lastField) {
	 this.lastField = lastField;
  }
  
  protected List<BoardField> getEmptyFields() {
	 return new ArrayList<>(emptyFields);      
  }  
    

}
  
  