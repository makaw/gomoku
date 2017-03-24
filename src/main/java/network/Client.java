/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import game.GameState;
import gomoku.AppObserver;
import gomoku.IConf;
import gomoku.Settings;
import gomoku.SettingsVar;
import gui.Console;

/**
 *
 * Klient serwera gry
 * 
 * @author Maciej Kawecki
 * 
 */
public final class Client {        

  /** Adres IP lub nazwa hosta serwera */  
  private final String serverIP;  
  /** Gniazdko do którego klient (zdalny) został podłączony */
  private final Socket socket;
  /** Strumień wyjściowy (do pisania) */
  private final ObjectOutputStream output;
  /** Strumień wejściowy (do czytania) */
  private final ObjectInputStream input;
  /** Numer przypisany przez serwer */
  private Integer number;
  /** Ustawienia przekazane z serwera */
  private SettingsVar settingsVar;
  /** Obserwator stanu gry */
  private final AppObserver gameSpy;
  /** Referencja do konsoli GUI */
  private final Console console;
  /** Timer do pingowania serwera */
  private final Ping ping;
  /** true jeżeli klient już zakończył udział w grze */
  private boolean clientEnded;

  
  /**
   * Konstruktor szablonu obiektu klienta w grze sieciowej
   * @param serverIP Adres IP (lub nazwa hosta) serwera zdalnego
   * @param gameSpy Referencja do obserwatora stanu gry
   * @param console Referencja do konsoli GUI
   * @throws java.io.IOException Podłączanie gniazdka
   * @throws java.lang.ClassNotFoundException W razie nieprawidłowej komendy z serwera
   * @throws Exception Odmowa połączenia (komplet klientów)
   * (nie można zrzutować otrzymanych danych)
   */  
  public Client(String serverIP, AppObserver gameSpy, Console console)
		  throws IOException, ClassNotFoundException, Exception {    
   
     // podłączenie gniazdka
     this.serverIP = serverIP;
     socket = new Socket(this.serverIP, IConf.SERVER_PORT); 
     output = new ObjectOutputStream(socket.getOutputStream());
     input = new ObjectInputStream(socket.getInputStream());
     
     this.gameSpy = gameSpy;
     this.console = console;
          
     gameSpy.sendObject("socket", socket);     

     // oczekiwanie na powitanie z serwera
     Command cmd = getResponse();
     if (cmd.getCommand() == Command.CMD_EXIT)
  	   throw new Exception("odmowa po\u0142\u0105czenia: jest ju\u017c komplet klient\u00f3w");
     
     gameSpy.sendObject("socket-state", "wait");     
     while (cmd.getCommand() != Command.CMD_START) {         
       cmd = getResponse();       
     } 

     // przesłanie do serwera zapytania o numer gracza
     sendCommand(new Command(Command.CMD_NUMBER));
     do {
         
       cmd = getResponse();
       if (cmd.getCommand()==Command.CMD_NUMBER)
         number = (Integer)(cmd.getCommandData());
       
     } while (number==null);
          
     // przesłanie do serwera zapytania o ustawienia gry
     sendCommand(new Command(Command.CMD_SETTINGS));
     
     do {
         
       cmd = (Command)(input.readObject());
       if (cmd.getCommand()==Command.CMD_SETTINGS)
         settingsVar = (SettingsVar)(cmd.getCommandData());
       
     } while (settingsVar==null);  
     
     // włączenie pingowania
     ping = new Ping(this); 
     ping.startPinging();
     
     console.networkButtonsEnable(true);
     
     clientEnded = false;
     
     
  }
  
  
  public Ping getPing() {
      
    return ping;
      
  }  
  
 
  protected Socket getSocket() {
      
     return socket;  
      
  }
  

  public Console getConsole() {
      
     return console;  
      
  }
  

  
  
 /**
  * Wysłanie komendy do serwera
  * @param cmd Wysyłana komenda
  * @throws IOException Nie można wysłać komendy
  * @throws ClassNotFoundException Nieprawidłowe dane dołączone do komendy
  */
  public void sendCommand(Command cmd) throws IOException, ClassNotFoundException {
    
    if ((ping==null || !ping.isPingOut()) && cmd instanceof Command) {
      output.writeObject(cmd);
      output.flush();
    }
    
    else if (ping != null) ping.stopPinging();
  
  }  
  

  /**
   * Metoda pobiera odpowiedź serwera (komendę)
   * @return Odpowiedź serwera (komenda)
   * @throws IOException  Nie można pobrać odpowiedzi
   */
  public Command getResponse() throws IOException {
      
     try {
       return (Command)(input.readObject());
     } 
     catch (ClassCastException e) {
       return null;  
     }
     catch (ClassNotFoundException e) {
       System.err.println(e);
     }
     
     return null;
      
  }
  

  public int getNumber() {
      
     return number; 
      
  }
  
  /**
   * Metoda zwracająca ustawienia gry pobrane z serwera
   * @return Ustawienia gry pobrane z serwera
   */
  public Settings getSettings() {

    return new Settings(settingsVar);
      
  }
  
  
  /**
   * Metoda przesyła do wątku kontrolującego grę informację o zatrzymaniu gry 
   * i wyświetla komunikat
   * 
   */
  public void endGame() {
     
    if (clientEnded) return;  
      
    clientEnded = true;  
      
    gameSpy.sendObject("state", GameState.WAIT);
    
    console.newLine();
    console.setMessageLn("Zerwanie po\u0142\u0105czenia przez serwer.", Color.RED);
    console.newGameMsg();    
             
    ping.stopPinging();
    console.networkButtonsEnable(false);
            
    try {
       socket.close();
    } catch (IOException e) {
       System.err.println(e);
    }
    
    try {
      Thread.sleep(150);     
    }
    catch (Exception e) {}
    
      
  }
  
  
   
  
}



