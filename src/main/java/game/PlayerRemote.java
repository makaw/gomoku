/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;
import java.awt.Color;
import java.io.IOException;
import network.Client;
import network.Command;

/**
 *
 * Szablon obiektu reprezentującego gracza - zdalnego klienta w grze sieciowej
 * 
 * @author Maciej Kawecki
 * 
 */
public class PlayerRemote extends Player {

   /** Referencja do obiektu klienta */ 
   private final Client client;

   /**
    * Konstruktor - wywołanie konstruktora z bazowej klasy abstrakcyjnej, 
    * przypisanie wartości/referencji do wewnętrznych pól klasy
    * @param client Referencja do obiektu klienta
    * @param pieceColor Kolor kamieni gracza
    * @param gBoard Referencja do obiektu będącego graficzną reprezentacją planszy
    * @param lBoard Referencja do obiektu logicznej warstwy planszy
    * @param name Nazwa gracza
    */   
   public PlayerRemote(Client client, BoardFieldState pieceColor, BoardGraphics gBoard, BoardLogic lBoard, String name)  {
       
      super(pieceColor, gBoard, lBoard, name);
      this.client = client;
      
   }
   
  
   /**
    * Nadpisana metoda superklasy, wymuszająca zakończenie ruchu gracza
    */
   @Override
   public void forceEndTurn() {
        
     super.forceEndTurn();
     
     try {
        client.sendCommand(new Command(Command.CMD_EXIT));        
     } catch (Exception e) {
        client.endGame();
     }
     finally {
         
       client.getPing().stopPinging();
       
     }
     
     
   }
    

    /**
     * Wykonanie ruchu przez gracza
     */  
    @Override
    public void makeMove() {
    
     Command command;   
     
      // oczekiwanie na komendy z serwera
     try { 
         
        do {
        
           command = client.getResponse();
          
           // zakończenie gry 
           if (command.getCommand() == Command.CMD_EXIT)  {
             
              forceEndTurn();
              return;
             
           }
           // odebranie wiadomości
           else if (command.getCommand() == Command.CMD_MESSAGE) {
               
             client.getConsole().newLine();
             client.getConsole().setMessageLn((String)(command.getCommandData()), new Color(0x22, 0x8b, 0x22));               
               
           }

             
        }  while (command.getCommand() != Command.CMD_MOVE);
      
        // ruch, sprawdzony już po stronie klienta zdalnego
        BoardField field = (BoardField)(command.getCommandData());
        gBoard.setPiece(lBoard, field.getA(), field.getB(), field.getState());
        gBoard.repaint();    
        // zmiana wartosci pola (logicznego)
        lBoard.setFieldState(field.getA(), field.getB(), field.getState());
        
      }
      
      catch (IOException e) {
              
         client.endGame();     
              
      }
      
    
    }
    
    
    
    
}