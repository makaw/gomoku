/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 *
 * Klasa modyfikująca metody obsługi zdarzeń myszy dla gracza-człowieka
 * 
 * @author Maciej Kawecki
 */
public class MouseHandler extends MouseAdapter  {
    
    /** Graficzna reprezentacja planszy */
    private final BoardGraphics gBoard;
    /** Logiczna warstwa planszy */
    private final Board lBoard;
    /** Kolor kamieni gracza */
    protected final BoardFieldState pColor;
    /** Flaga blokująca obsługę zdarzeń myszy */
    protected boolean lockedFlag;
    
    /**
     * Konstruktor 
     * @param gBoard Graficzna reprezentacja planszy
     * @param lBoard Logiczna warstwa planszy
     * @param pColor Kolor kamieni gracza
     */
    public MouseHandler(BoardGraphics gBoard, Board lBoard, BoardFieldState pColor) {

       this.gBoard = gBoard; 
       this.lBoard = lBoard;
       this.pColor = pColor;
       lockedFlag = false;
        
    }
    

    @Override
    public void mouseClicked(MouseEvent me) { 
        
       // wyznaczenie indeksow macierzy - planszy
       int a = BoardGraphics.getFieldA(me.getX());
       int b = BoardGraphics.getFieldB(me.getY());
       
       // FIX
       if (a<0 || a>=lBoard.getColsAndRows() || b<0 || b>=lBoard.getColsAndRows()) return;
       
       if (lockedFlag || (lBoard.getFieldState(a,b)!=BoardFieldState.EMPTY)) return;  
       
       // w razie potrzeby tu wysłanie odpowiedniego komunikatu do serwera gry
       sendMoveToServer(a, b);          
       
       // rysowanie kamienia
       gBoard.setPiece(lBoard, a, b, pColor);
       gBoard.repaint();                  
       
       // zmiana wartosci pola (logicznego)
       lockedFlag = lBoard.setFieldState(a, b, pColor);
       
    
    } 
    
    
    /**
     * Metoda wysyłająca do serwera komendę postawienia kamienia
     * w wybranym miejscu planszy. Do nadpisania przez klasy dziedziczące.
     * @param a Indeks a (kolumna) pola na planszy
     * @param b Indeks b (wiersz) pola na planszy
     */
    protected void sendMoveToServer(int a, int b) {}
    

    
    @Override
    public void mouseMoved(MouseEvent me) {
         
       if (lockedFlag) return;
        
       // rysowanie kursora  
       gBoard.setCursor(lBoard, BoardGraphics.getFieldA(me.getX()), BoardGraphics.getFieldB(me.getY()), pColor);
       gBoard.repaint();      
      
    }

     

}
