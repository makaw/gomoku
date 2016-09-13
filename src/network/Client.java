/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import game.GameState;
import gomoku.AppObserver;
import gomoku.IConf;
import gomoku.Settings;
import gomoku.SettingsVar;
import gui.Console;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * Szablon obiektu reprezentującego klienta serwera gry
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
   * (nie można zrzutować otrzymanych danych)
   */  
  public Client(String serverIP, AppObserver gameSpy, Console console) throws IOException, ClassNotFoundException {    
   
     // podłączenie gniazdka
     this.serverIP = serverIP;
     socket = new Socket(this.serverIP, IConf.SERVER_PORT); 
     output = new ObjectOutputStream(socket.getOutputStream());
     input = new ObjectInputStream(socket.getInputStream());
     
     this.gameSpy = gameSpy;
     this.console = console;
     
     // oczekiwanie na powitanie z serwera
     do {
         
       Command cmd = getResponse();
       if (cmd.getCommand() == Command.CMD_START) break;
       
     } while (true);
     

     // przesłanie do serwera zapytania o numer gracza
     sendCommand(new Command(Command.CMD_NUMBER));
     do {
         
       Command cmd = getResponse();
       if (cmd.getCommand()==Command.CMD_NUMBER)
         number = (Integer)(cmd.getCommandData());
       
     } while (number==null);
     
     //System.out.println("my number is "+number);
     // przesłanie do serwera zapytania o ustawienia gry
     sendCommand(new Command(Command.CMD_SETTINGS));
     
     do {
         
       Command cmd = (Command)(input.readObject());
       if (cmd.getCommand()==Command.CMD_SETTINGS)
         settingsVar = (SettingsVar)(cmd.getCommandData());
       
     } while (settingsVar==null);  
     
     // włączenie pingowania
     ping = new Ping(this); 
     ping.startPinging();
     
     console.msgButtonEnable(true);
     
     clientEnded = false;
     
  }
  
  
  /**
   * Metoda pobiera referencję do timera pingującego serwer
   * @return Referencja do timera pingującego serwer
   * @since 1.1
   */
  public Ping getPing() {
      
    return ping;
      
  }  
  
  /**
   * Metoda pobiera referencję do obiektu gniazdka
   * @return Referencja do obiektu gniazdka
   */  
  protected Socket getSocket() {
      
     return socket;  
      
  }
  
  /**
   * Metoda pobiera referencję do obiektu konsoli
   * @return Referencja do obiektu konsoli
   */
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
    
    if ((ping==null || !ping.getPingOut()) && cmd instanceof Command) {
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
  
  /**
   * Metoda zwracająca numer klienta nadany przez serwer
   * @return Numer klienta nadany przez serwer
   */
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
      
    gameSpy.sendObject("state", new GameState(GameState.WAIT, ""));
    
    console.newLine();
    console.setMessageLn("Zerwanie po\u0142\u0105czenia przez serwer.", Color.RED);
    console.setMessageLn("Wybierz \"Gra\"  \u279C \"Nowa gra\" aby " +
                              "rozpocz\u0105\u0107.", Color.GRAY);
   
             
    ping.stopPinging();
    console.msgButtonEnable(false);
            
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



