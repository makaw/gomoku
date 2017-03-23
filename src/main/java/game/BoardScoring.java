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
* Ocena sytuacji na planszy przy pomocy reprezentacji znakowej
* 
* @author Maciej Kawecki
* 
*/
@SuppressWarnings("serial")
public class BoardScoring extends ArrayList<String> {

  /** Indeksy linii -  poziome, pionowe i ukośne L-R */
  private final static int HORIZ = 0, VERT = 1, SKETCH_L = 2, SKETCH_R = 3;	
	
  /** Aktualny stan planszy - linie poziome, pionowe i ukośne (repr. znakowa) */
  private final String[] boardLines = new String[] {"", "", "", ""};
  /** Wszystkie rozważane ciągi kamieni */
  private final String[] allRowsB, allRowsW; 	
  /** Wygrywające ciągi kamieni */
  private final String successB, successW;
  
  /** Aktualne ustawienia gry */
  private final Settings settings;
  
  
  /**
   * Konstruktor
   * @param settings Referencja do obiektu zawierającego ustawienia gry
   */  
  public BoardScoring(Settings settings) {
	  
	super();  
	this.settings = settings;
	
	for (int a=0; a<settings.getColsAndRows(); a++) {       
	  for (int b=0; b<settings.getColsAndRows(); b++) {	    	        	      
	     boardLines[HORIZ] += "0"; 	       
	     if (b<=a) boardLines[SKETCH_L] += "0"; else boardLines[SKETCH_L] += "x";
	     if (b>=a) boardLines[SKETCH_R] += "0"; else boardLines[SKETCH_R] += "x";	       
	   }      	      
	   boardLines[HORIZ] += "|"; 
	   boardLines[SKETCH_L] += "|";  boardLines[SKETCH_R] += "|";	     
	} 

    boardLines[SKETCH_L] += boardLines[SKETCH_R].substring(settings.getColsAndRows()+1);
    boardLines[SKETCH_R] = boardLines[SKETCH_L];
    boardLines[VERT] = boardLines[HORIZ];    
    
    successB = BoardFieldState.BLACK.getWinningRow(settings.getPiecesInRow());
    successW = BoardFieldState.WHITE.getWinningRow(settings.getPiecesInRow());    
	allRowsB = getAllRows(BoardFieldState.BLACK);		
    allRowsW = getAllRows(BoardFieldState.WHITE);    
    	
  }
  
  
  
  /**
   * Czy wygrywa
   * @param pColor Kolor kamieni
   * @return True jeżeli wygrana
   */
  public boolean hasWon(BoardFieldState pColor) {	  

	String success = pColor == BoardFieldState.BLACK ? successB : successW;  
	  
	return StringUtils.contains(boardLines[HORIZ], success)
			|| StringUtils.contains(boardLines[VERT], success)
			|| StringUtils.contains(boardLines[SKETCH_L], success)
			|| StringUtils.contains(boardLines[SKETCH_R], success);
	  
  }  
  
	
  /**
   * Aktualizacja reprezentacji znakowej (nowy ruch)
   * @param a Indeks a (kolumna) pola
   * @param b Indeks b (wiersz) pola
   * @param state Docelowa wartość(stan) pola
   */
  public void update(int a, int b, BoardFieldState state) {	  

    int cr = settings.getColsAndRows() + 1;
      
    try {
      StringBuilder tmp = new StringBuilder(boardLines[HORIZ]);
      tmp.setCharAt(b*cr + a, state.getCode());
      boardLines[HORIZ] = tmp.toString();
    
      tmp = new StringBuilder(boardLines[VERT]);
      tmp.setCharAt(a*cr + b, state.getCode());
      boardLines[VERT] = tmp.toString();      
   
      tmp = new StringBuilder(boardLines[SKETCH_L]);
      tmp.setCharAt((a+b)*cr + a, state.getCode());
      boardLines[SKETCH_L] = tmp.toString();
   
      tmp = new StringBuilder(boardLines[SKETCH_R]);
      tmp.setCharAt((a-b+cr-2)*cr + a, state.getCode());        
      boardLines[SKETCH_R] = tmp.toString();
    }
    catch (Exception e) {
     e.printStackTrace();
    }
    
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
   * @param len Długość listy
   * @return Tablica możliwych ciagów znaków
   */
  private static String[] getAllRows(String[] elems, int len) {      
      
     if (len == 1) return elems;       

     String[] rows = new String[(int) Math.pow(elems.length, len)];
     String[] subrows = getAllRows(elems, len - 1);          
     int el = elems.length, sl = subrows.length;
     
     for (int i = 0; i < el; i++)
       for(int j = 0; j < sl; j++)                   
         rows[i*sl + j] = elems[i] + subrows[j];
         
     return rows;
          
  }  
  
    
  /**   
   * Punktacja sytuacji na planszy
   * @param pColor Kolor gracza dla którego liczona jest punktacja
   * @return Aktualna punktacja planszy dla danego gracza
   */
  protected int getScore(BoardFieldState pColor) {
	  
	if (hasWon(pColor)) return MoveGenerator.MAX_SCORE;    
	  
	int score = 0;
	String pStr = pColor.toString();
	String matches[] = pColor == BoardFieldState.BLACK ? allRowsB : allRowsW;		

	for (String line : boardLines) {
		
	  line = line.replaceAll("[0x]{" + settings.getColsAndRows() + "}", "");
	  
	  for (String match : matches)  {

		  int cnt = StringUtils.countMatches(match, pStr);

		  // bonusy ...
		  boolean near1 = cnt+2 == settings.getPiecesInRow();
		  boolean near2 = near1 && match.startsWith("0") && match.endsWith("0");
		  boolean near3 = !near1 && cnt+1 == settings.getPiecesInRow();
		  
		  double mcnt =  StringUtils.countMatches(line, match);
		  if (near1) { mcnt *= 2; } 
		  if (near2) { mcnt *= 2; } 
		  if (near3) { mcnt *= 8; }  
		  
		  score += cnt * mcnt; 
		   
	  }
	  
	}
	  
	return score;
    
  }
    
	
}
