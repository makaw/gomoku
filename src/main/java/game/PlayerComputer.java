/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import gui.BoardGraphics;
import gui.GUI;
import gui.StatusBar;

/**
 *
 * Szablon obiektu reprezentującego gracza-komputer
 * 
 * @author Maciej Kawecki
 * 
 * 
 */
public class PlayerComputer extends Player  {
     
   /** Wątek wyszukiwania najlepszego ruchu */
   private SwingWorker<BoardField, Object> worker;   
   /** Pasek stanu */
   private final StatusBar statusBar;
   /** Ref. do GUI */
   private final GUI frame;
   /** Listener do zatrzymania rozgrywki */
   private final PropertyChangeListener change;
   
   
   /**
    * Konstruktor
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy 
    * @param name Nazwa gracza
    */
   public PlayerComputer(BoardFieldState pieceColor, BoardGraphics gBoard, Board lBoard, String name) {
       
      super(pieceColor, gBoard, lBoard, name);
      
      frame = (GUI)(SwingUtilities.getWindowAncestor(gBoard));

      statusBar = frame.getStatusBar();   
      
      change = new PropertyChangeListener() {     		
     		@Override
     		public void propertyChange(PropertyChangeEvent evt) {
     		  if (evt.getPropertyName().equals("cancelled")) {		    
     		    frame.cancelGame(); 		    
     		  }
     		}
     	  };

   }
   
   
   
   /**
    * Wykonanie ruchu przez komputer 
    * 
    */
   @Override
   public void makeMove() {
          
     gBoard.setWaitMouseCursor();               
     statusBar.addPropertyChangeListener(change);
             
  	 worker = new SwingWorker<BoardField, Object>() {

  	    @Override
  	    protected BoardField doInBackground() {
  	      // pobranie wygenerowanego nowego ruchu 	
  	      return MoveGenerator.getMove(lBoard, pieceColor);  	      
    	}

  	    @Override
  	    protected void done() {
  	    	
  	      try {  	    	  
			BoardField move = get();
			// rysowanie kamienia
		    gBoard.setPiece(lBoard, move.getA(), move.getB(), getColor());
		    gBoard.repaint();    
		    // zmiana wartosci pola (logicznego)
		    lBoard.setFieldState(move.getA(), move.getB(), getColor());		    		      
		  } 
  	      catch (InterruptedException | ExecutionException | CancellationException e) {	}
  	      finally {			  
  	        statusBar.enableProgress(false);
  	        statusBar.removePropertyChangeListener(change);  	        
		  }
  	      
  	    }

  	 };  	   	 
  	 
  	 statusBar.enableProgress(true);

  	 worker.execute();
  	 
  	 do {  		 
  	   try {
		 Thread.sleep(100);
  	   } catch (InterruptedException e) {
		 break;
  	   }	   		 
  	 } while (!worker.isDone());
  	   	   	 
   }
   
   
   @Override
   public void forceEndTurn() {
	   
	 super.forceEndTurn();
	 worker.cancel(true); 
	   
   }
   
  
}

