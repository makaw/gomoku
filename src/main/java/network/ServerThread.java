/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import gomoku.Settings;
import gomoku.SettingsVar;

/**
 *
 * Wątek serwera dedykowany konkretnemu klientowi
 * 
 * @author Maciej Kawecki
 * 
 */
public class ServerThread extends Thread {
  
  /** Numer obsługiwanego klienta (0-1) */
  private final int clientNumber;  
  /** Referencja do obiektu głównego serwera */
  private final Server server;
  /** Referencja do gniazdka klienckiego */
  private final Socket socket;
  
  
  /**
   * Konstruktor 
   * @param console Referencja do konsoli GUI
   * @param socket Rereferencja do gniazdka klienckiego
   * @param server Referencja do obiektu głównego serwera
   * @param clientNumber Numer obsługiwanego klienta
   */
  public ServerThread(Socket socket, Server server, int clientNumber)  {
      
    this.server = server;
    this.socket = socket;
    this.clientNumber = clientNumber;
    
  }  
  

  @Override
  public void run() {
      
     boolean stopThread = false;


     do {
         
      try {
          
         ObjectOutputStream output = server.getOutputStream(clientNumber);  
          
         Command command = (Command)((server.getInputStream(clientNumber)).readObject());
         
         switch (command.getCommand()) {
             
             // odpowiedź na zapytanie klienta o jego numer
             case Command.CMD_NUMBER:   
             
                 output.writeObject(new Command(Command.CMD_NUMBER, clientNumber)); 
                 output.flush();
                 break;
                 
             // odpowiedź na zapytanie klienta o ustawienia gry    
             case Command.CMD_SETTINGS:
                 
                 Settings settings = server.getSettings();
             
                 output.writeObject(new Command(Command.CMD_SETTINGS,
                		 new SettingsVar(settings.getColsAndRows(), settings.getPiecesInRow())));
                 output.flush();
                 break;
                 
             // rozłączenie klienta    
             case Command.CMD_EXIT: throw new IOException();                  
             
             // full, ping - nic nie robi
             case Command.CMD_FULL: 
             case Command.CMD_PING: break;
             
             // koniec odbioru wiadomości - odeślij żeby odblokować wątek
             case Command.CMD_STOP_MSG: 
                  
                output.writeObject(command);
                output.flush();    
                break;
            
             // pozostałe polecenia przesyłane do drugiego klienta
             default:
            
               output = server.getOutputStream(clientNumber^1);
               output.writeObject(command);
               output.flush();
               
               break;
           
         }

      } catch (IOException e) {
        
         server.getServerSpy().sendObject("ping-out", clientNumber);
          
         stopThread = true;
         
         try {
              
           socket.close();
             
         } catch (IOException ex) {}
         
             
      } 
      
      catch (ClassNotFoundException | ClassCastException | NullPointerException ex) {}
      
      
      try {
          Thread.sleep(50);
      }
      catch (InterruptedException e) {  return; }
      
         
    } while (!stopThread);
      
      
  }
    
    
}
