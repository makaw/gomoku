/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import gomoku.Settings;


/**
 *
 * Szablon obiektu odpowiedzialnego za warstwę logiczną planszy 
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
  private BoardField lastField;  
  /** Aktualny stan planszy - linie poziome, pionowe i ukośne */
  private String horizLine = "", vertLine = "", sketchLLine = "", sketchRLine = "";
  /** Wszystkie rozważane ciągi kamieni */
  private final String[] allListB, allListW;
  
  /** Wiersz pustych pól */
  private static String emptyLine = "";
  
  
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
     
      for (int b=0; b<settings.getColsAndRows(); b++) {
    	        
       fields[indeks+b] = new BoardField(a, b);
      
       horizLine += "0";  
       vertLine += "0";
       
       if (b<=a) { sketchLLine += "0"; }
       if (b>=a) { sketchRLine += "0"; }
       
      }
      
      emptyLine += "0";
      
      horizLine += "|";
      vertLine += "|";
      sketchLLine += "|";
      sketchRLine += "|";
     
    }    
    
	allListB = getAllRows(BoardFieldState.BLACK);		
    allListW = getAllRows(BoardFieldState.WHITE);    
    
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
   * Metoda próbująca zmienić stan wskazanego pola planszy; w razie sukcesu 
   * zapamiętująca indeksy i stan do czasu następnej zmiany.
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param state Docelowa wartość(stan) pola
   * @return true, jeżeli stan pola został zmieniony, false jeżeli nie
   */
  public boolean setFieldState(int a, int b, BoardFieldState state) {
    
    boolean changed = false;  

    try {
         
      int index = getIndex(a, b);  
      fields[index].setState(state); 
      
      if (state != BoardFieldState.EMPTY) freeFieldsAmount--;
      else freeFieldsAmount++;
      
      changed = true;
      lastField = new BoardField(a, b, state);     
      
      StringBuilder tmp = new StringBuilder(horizLine);
      tmp.setCharAt(b*(settings.getColsAndRows()+1) + a, state.getCode());
      horizLine = tmp.toString();
      
      tmp = new StringBuilder(vertLine);
      tmp.setCharAt(a*(settings.getColsAndRows()+1) + b, state.getCode());
      vertLine = tmp.toString();      
              
            
    }
    
    // wyjątek - a lub b poza zakresem
    catch (Exception e) {   }

    
    
    return changed;

    
  }    
  
  

  /**
   * Metoda znajdująca "wygrywający" rząd zawierający wskazane pole. 
   * Sprawdzane są od góry 4 kierunki: skos góra,  pion, poziom, skos dół.
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
     
     if (!hasWon(pColor)) return null;
     
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
   * @see BoardLogic#getWinningRow(int, int, java.lang.Byte) 
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
   * Statyczna metoda zwracająca nazwę (np.A1) wskazanego pola planszy
   * @param field Pole planszy
   * @return Nazwa (np.A1) wskazanego pola
   */
  public String getFieldName(BoardField field) {
      
     return Character.toString((char)('A' + field.getA()))
    		 + Integer.toString(settings.getColsAndRows() - field.getB());
      
  }  
  
  
  /**
   * Statyczna metoda zwracająca nazwę (np.A1) pola planszy z ostatniego ruchu
   * @return Nazwa (np.A1) pola
   */
  public String getLastFieldName() {
      
     return getFieldName(lastField);
      
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
  
  
  protected int getFreeFieldsAmount() {
	  
	 return freeFieldsAmount; 
	  
  }
  
  /**
   * Metoda zwracająca listę pustych pól planszy
   * @return Lista pustych pól planszy
   */
  protected List<BoardField> getEmptyFields() {
      
     ArrayList<BoardField> emptyFields = new ArrayList<>();

     for (int i=0; i<settings.getFieldsAmount();i++) {
         
       if (fields[i].getState()==BoardFieldState.EMPTY) emptyFields.add(fields[i]);  
         
     }
     
     return emptyFields;
      
  }
  
  
  /**
   * Czy wygrywa
   * @param pColor Kolor kamieni
   * @return True jeżeli wygrana
   */
  protected boolean hasWon(BoardFieldState pColor) {	  

	String success = "";
	for (int i=0; i<settings.getPiecesInRow(); i++) success += pColor.toString();
	return horizLine.contains(success) || vertLine.contains(success);
	  
  }
  
  
  
  /**   
   * Punktacja sytuacji na planszy (dla MiniMax). 
   * @param pColor Kolor gracza dla którego liczona jest punktacja
   * @return Aktualna punktacja planszy dla danego gracza
   */
  protected int getScore(BoardFieldState pColor) {
	  
	int score = 0;
	String t = pColor.toString();

	if (hasWon(pColor)) return MoveGenerator.MAX_SCORE;
	
	String m[] = pColor == BoardFieldState.BLACK ? allListB : allListW;	
	

	for (int i=0;i<2;i++) {
		
	  String line = i == 0 ? horizLine : vertLine;
	  line = line.replace(emptyLine, "");
	  
	  for (String match : m)  {

		  int cnt = StringUtils.countMatches(match, t);
		  
		  // bonusy ...
		  boolean near1 = match.startsWith("0") && match.endsWith("0") && cnt+2 == settings.getPiecesInRow();
		  boolean near2 = cnt+1 == settings.getPiecesInRow();
		  
		  double mcnt = StringUtils.countMatches(line, match);
		  if (near1) { mcnt+=4; }
		  if (near2) { mcnt+=6; }
		  
		  score += cnt * Math.pow(2, mcnt-1);
		   
	  }
	  
	}
	  
	return score;
    
  }
  
  
  /**
   * Wszystkie możliwe ciągi bez wygrywających i z co najmniej 2-oma kamieniami
   * @param pColor Kolor kamieni
   * @return Tablica ciągów znaków
   */
  private String[] getAllRows(BoardFieldState pColor) {
	  
	 String t = pColor.toString();
	 String[] row = getAllRows(new String[] { t,  "0"}, settings.getPiecesInRow());
	 
	 List<String> tmp = new ArrayList<>();
	 
	 for (String s: row) {
		 
	   int i = StringUtils.countMatches(s, t);
	   if (i>1 && i<settings.getPiecesInRow()) tmp.add(s);
		 
	 }
	 
	 String tmps[] = new String[tmp.size()];
	 return tmp.toArray(tmps);
	  
  }
  
  
  /**
   * Wszystkie możliwe ciągi (bez powt.)
   * @param elems Elementy składowe
   * @param listLen Długość listy
   * @return Tablica możliwych ciagów znaków
   */
  private static String[] getAllRows(String[] elems, int listLen) {      
      
     if (listLen == 1) return elems;       

     String[] rows = new String[(int) Math.pow(elems.length, listLen)];
     String[] subrows = getAllRows(elems, listLen - 1);
          
     int index = 0;
     for (int i = 0; i < elems.length; i++)
       for(int j = 0; j < subrows.length; j++) {                  
         rows[index] = elems[i] + subrows[j];
         index++;
       }
         
     return rows;
          
  }

}
  
  