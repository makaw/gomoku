/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;
import java.util.ArrayList;

/**
 *
 * Szablon obiektu reprezentującego gracza-komputer
 * 
 * @author Maciej Kawecki
 * 
 * 
 */
public class PlayerComputer extends Player  {
     
    
   /**
    * Konstruktor - wywołanie konstruktora z bazowej klasy abstrakcyjnej, 
    * przypisanie wartości/referencji do wewnętrznych pól klasy
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy 
    * @param name Nazwa gracza
    */
   public PlayerComputer(byte pieceColor, BoardGraphics gBoard, BoardLogic lBoard, String name) {
       
      super(pieceColor, gBoard, lBoard, name);
       
   }
   
   
   
   /**
    * Wykonanie ruchu przez komputer 
    * 
    */
   @Override
   public void makeMove() {
   
       
       gBoard.setWaitMouseCursor();
       
       byte otherPieceColor = pieceColor == BoardField.BLACK ? BoardField.WHITE : BoardField.BLACK;
      
       // pobranie wygenerowanego nowego ruchu 
       BoardField move = getRandMove(lBoard.lastField);
       //move = Minimax.getInstance(lBoard, pieceColor).getMove();
                   
       // rysowanie kamienia
       gBoard.setPiece(lBoard, move.getA(), move.getB(), getColor());
       gBoard.repaint();    
       // zmiana wartosci pola (logicznego)
       lBoard.setFieldState(move.getA(), move.getB(), getColor());
   
       try {
         Thread.sleep(10);
       } catch (InterruptedException e) {
         System.err.println(e);
       }       
       
   }
   
   
   /**
    * Metoda generująca losowy ruch, najchętniej w pobliżu ostatniego ruchu przeciwnika.
    * Tymczasowa, tylko do czasu zrobienia czegoś lepszego :-)
    * @deprecated 
    * @param lastField Ostatnio zapełnione pole na planszy
    * @return Współrzędne pola na planszy
    */
   @Deprecated
   private BoardField getRandMove(BoardField lastField) {
      
      int randField;
       
      ArrayList<BoardField> emptyFieldsList = new ArrayList<>(lBoard.getEmptyFields());
      ArrayList<BoardField> nearFields = new ArrayList<>();
      
      // próba stworzenia listy pustych pól blisko ostatniego ruchu przeciwnika
      if (lastField != null) 

      for (BoardField field: emptyFieldsList) {
          
        for (int i=-2;i<=2;i+=(i==-1 ? 2 : 1)) {
            
            if  ((field.getA()+i == lastField.getA() && field.getB() == lastField.getA())
             || (field.getA() == lastField.getA() && field.getB()+i == lastField.getB())
             || (field.getA()+i == lastField.getA() && field.getB()+i == lastField.getA())) {
             
                nearFields.add(field);
              
            }
          
        }
        
      }
           
     boolean near = (nearFields.size()>0);
      
      // jeżeli znaleziono, to losowanie spośród nich
      if (near) {

         int emptyFields = nearFields.size();
         randField = (int)(Math.random() * (emptyFields-1));

      }
      
      // jeżeli nie ma żadnych, to losowanie z całej planszy
      else {
          
        int emptyFields = emptyFieldsList.size();
        randField = (int)(Math.random() * (emptyFields-1));
       
      }
      
      // udaję że myślę ;-)
      try {
         Thread.sleep(300);
       } catch (InterruptedException e) {
        System.err.println(e);
      }

      
      return (near) ? nearFields.get(randField) : emptyFieldsList.get(randField);
      
   }
   
   
}

