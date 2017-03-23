/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package game;

import gui.BoardGraphics;
import java.io.IOException;

import network.Client;
import network.Command;


/**
 *
 * Klasa modyfikująca metody obsługi zdarzeń myszy dla gracza-człowieka, wersja dla klienta zdalnego
 * 
 * @author Maciej Kawecki
 * 
 */
public class MouseHandlerLocalClient extends MouseHandler  {
    
    /** Klient serwera */
    private Client client;
    
    
    /**
     * Konstruktor 
     * @param gBoard Graficzna reprezentacja planszy
     * @param lBoard Logiczna warstwa planszy
     * @param pColor Kolor kamieni gracza
     */
    public MouseHandlerLocalClient(BoardGraphics gBoard, Board lBoard, BoardFieldState pColor) {

       super(gBoard, lBoard, pColor);
        
    }    
    
    /**
     * Konstruktor 
     * @param client Klient serwera 
     * @param gBoard Graficzna reprezentacja planszy
     * @param lBoard Logiczna warstwa planszy
     * @param pColor Kolor kamieni gracza
     */
     public MouseHandlerLocalClient(Client client, BoardGraphics gBoard, 
                                         Board lBoard, BoardFieldState pColor) {

       this(gBoard, lBoard, pColor);
       this.client = client;
        
    }
    

    /**
     * Nadpisana metoda wysyłająca do serwera komendę postawienia kamienia
     * w wybranym miejscu planszy 
     * @param a Indeks a (kolumna) pola na planszy
     * @param b Indeks b (wiersz) pola na planszy
     */
    @Override
    protected void sendMoveToServer(int a, int b) {

        if (!client.getPing().isPingOut())
        
        try {
            client.sendCommand(new Command(Command.CMD_MOVE, new BoardField(a, b, pColor)));
        } 
        catch (IOException e) {
        
          System.err.println(e);
          
        } 
        catch (ClassNotFoundException e) {}
  
    }    

    

}
