/*
 * Gomoku
 * Maciej Kawecki 2015/16
 */
package network;

import gomoku.Settings;
import gui.GUI;
import gui.ServerGUI;
import gui.ServerGUIConsole;
import java.awt.Color;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities; 
import gomoku.IConf;


/**
 *
 * Klasa główna zawierająca metodę main() - wywołanie interfejsu graficznego, 
 * uruchomianie serwera. Reprezentuje głowny serwer.
 * 
 * @author Maciej Kawecki
 * 
 */
public class Server  implements Observer {
    
  /** Gniazdko serwera */
  private ServerSocket serverSocket;  
  /** Tablica strumieni wyjściowych */
  private final ObjectOutputStream[] output;
  /** Tablica strumieni wejściowych */
  private final ObjectInputStream[] input;
  /** Lista wątków klientów */
  List<ServerThread> serverThreadList;
  /** Lista gniazdek klienckich */
  List<Socket> serverSocketList;
  /** True jeżeli konieczny jest restart, false jeżeli nie */
  private boolean restart;
  /** Referencja do obiektu do komunikacji z innymi wątkami (GUI) - obserwatora */
  private ServerSpy serverSpy;
  /** Referencja do obiektu reprezentującego graficzny interfejs użytkownika serwera */
  private ServerGUI gui;
  /** Ustawienia gry po stronie serwera */
  private final Settings settings;
  /** Obiekt służący do komunikacji z wątkiem GUI serwera w zakresie zmiany ustawień */
  private ServerSettingsSpy settingsSpy;  
  
  /**
   * Konstruktor obiektu serwera głównego
   * @throws InterruptedException Problem z uruchomieniem wątku GUI
   * @throws InvocationTargetException Problem z uruchomieniem wątku GUI
   */
  private Server() throws InterruptedException, InvocationTargetException {
      
    this.output = new ObjectOutputStream[2];
    this.input = new ObjectInputStream[2];
    
    serverSpy = new ServerSpy();
    serverSpy.addObserver(this);
    serverSocketList = new ArrayList<Socket>();
    serverThreadList = new ArrayList<ServerThread>();
    
    // ustawia wartości domyślne
    settings = new Settings();
    settingsSpy = new ServerSettingsSpy();
    
    GUI.setLookAndFeel();
    
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        gui = new ServerGUI(serverSpy, settingsSpy, settings); 
      }
    });    
    
    settingsSpy.addObserver(this);
  
      
  }
  
  
  
  /**
   * Metoda ustawia referencję do strumienia wyjściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @param output Strumień wyjściowy
   */
  public void setOutputStream(int clientNumber, ObjectOutputStream output) {
      
    this.output[clientNumber] = output;
      
  }
  
  /**
   * Metoda ustawia referencję do strumienia wejściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @param input Strumień wejściowy
   */
  public void setInputStream(int clientNumber, ObjectInputStream input) {
      
    this.input[clientNumber] = input;
      
  }  
  
  /**
   * Metoda zwraca referencję do strumienia wyjściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @return Referencja do strumienia wyjściowego
   */
  public ObjectOutputStream getOutputStream(int clientNumber) {
      
    
    try {
        
      return output[clientNumber];
      
    }
    catch (ArrayIndexOutOfBoundsException e) {
       
       serverRestart();
       return null;
        
    }
        
  }
  
  
  /**
   * Metoda zwraca referencję do strumienia wejściowego dla danego klienta
   * @param clientNumber Numer klienta
   * @return Referencja do strumienia wejściowego
   */  
  public ObjectInputStream getInputStream(int clientNumber) {
    
    try {
        
      return input[clientNumber];   
        
    }
      
    catch (ArrayIndexOutOfBoundsException e) {
       
       serverRestart();
       return null;
        
    }
      
  }  
  
  
  /**
   * Metoda dla statycznej klasy main; zwraca referencję do gniazdka serwerowego
   * @return Referencja do gniazdka serwerowego
   */
  private ServerSocket getServerSocket() {
      
    return serverSocket;  
      
  }
  
  /**
   * Metoda dla statycznej klasy main; 
   * zapamiętuje gniazdko klienta do późniejszego uruchomienia dedykowanego wątku serwera
   * @param socket Gniazdko klienta
   */
  private void addNewSocket(Socket socket) {
      
     serverSocketList.add(socket);        
      
  }
  
  /**
   * Metoda uruchamia wątki serwera dla każdego z podłączonych wcześniej klientów
   */
  private void startServerThreads() {
      
    int n = 0;  
    
    try {
        
      for (Socket s: serverSocketList) {
        
        // przesłanie powitania do klienta, żeby sprawdzić czy się czasem nie rozłączył  
        gui.getServerConsole().setMessageLn("Komunikat powitania do klienta "+(n+1)+"....", new Color(0xa5, 0x2a, 0x2a));
        ObjectOutputStream out = getOutputStream(n); 
        try {
           out.writeObject(new Command(Command.CMD_START));
           out.flush();
        } catch (IOException ex) {
           gui.getServerConsole().setMessageLn("Klient "+(n+1)+" roz\u0142\u0105czy\u0142 si\u0119.", Color.RED);
           serverRestart();
        }
      
        ServerThread sThread = new ServerThread(this.gui.getServerConsole(), s, this, n);
        serverThreadList.add(sThread);
        sThread.start();
        n++;
     
      }
    }
    // wyjątek rzucany, jeżeli w trakcie podłączania drugiego klienta, pierwszy klient 
    // się rozłączył i poszedł restart serwera; nic nie trzeba robić, wystarczy nie blokować
    catch (ConcurrentModificationException e) {}
           
      
  }
  
  /**
   * Metoda pobiera obiekt komunikacji z innymi wątkami (Observable)
   * @return Obiekt komunikacji z innymi wątkami (Observable)
   */
  protected ServerSpy getServerSpy() {
      
    return serverSpy;  
      
  }
  
  
  /**
   * Metoda pobiera ustawienia gry po stronie serwera
   * @return Ustawienia gry po stronie serwera
   */
  protected Settings getSettings() {
      
    return settings;
      
  }
  
  
  /**
   * Metoda aktualizująca ustawienia gry po stronie serwera,
   * oraz odbierająca informacje o zmianie stanu serwera, 
   * przy wykorzystaniu mechanizmu "Obserwatora" (Observer) + restart serwera; 
   * komunikacja z wątkiem GUI
   * @param obs Obserwowany obiekt
   * @param change Przekazany nowy stan gry 
   * @see ServerSettingsSpy
   * @see ServerSpy
   * @see gui.ServerGUI#restartGameSettings() 
   */  
  @Override
  public void update(Observable obs, Object change) {
      
   
    if (change instanceof ServerSettingsSpy) {
       
         // zmiana ustawień, restart serwera
        
        ServerSettingsSpy changedSettings = (ServerSettingsSpy)change;
      
        settings.setSettings(changedSettings.colsAndRows, changedSettings.piecesInRow, 
                             changedSettings.piecesInRowStrict);
        
        serverRestart();
        
      
    }     
    
    else if (change instanceof ServerSpy) {
         
         serverRestart();
       
     }
     
  }    
  
  
  /**
   * Restart w razie utracenia połączenia z którymś z klientów, bo to zatrzymuje rozgrywkę
   */  
  protected void serverRestart() {
      
     gui.getServerConsole().setMessageLn("Serwer zrestartowany, zerwane połączenia.", Color.RED);
     gui.getServerConsole().newLine(); 

     restart = true;
     
     if (serverThreadList.size()<2)  {
         
         try {
             serverSocket.close();
         } catch (Exception e) {
             System.err.println("IOExc restart: "+e);
         }
       
         while (!serverSocket.isClosed()) { 
         
             try {
                 Thread.sleep(10);
             } catch (Exception e) {}
           
         };
         
         setServerSocket();
         
     }
     
     for (ServerThread t: serverThreadList) t.interrupt();
     for (Socket s: serverSocketList) try {
         s.close();
         } catch (IOException ex) {}
         
     serverThreadList.clear();
     serverSocketList.clear();
     
  }
  
  
  public void setServerSocket() {
      
      try { 
          serverSocket = new ServerSocket(IConf.SERVER_PORT);
      } catch (IOException e) {
          System.err.println("Wyj\u015bcie z programu : "+e);
          System.exit(0);
      }
      
  }
 
  
      
 /** 
   * Metoda main wołana przez system w trakcie uruchamiania aplikacji. 
   * Wywołuje konstruktor, który bezpiecznie uruchamia interfejs graficzny, 
   * oczekuje na podłączenie klientów, 
   * aby w końcu uruchomić dedykowane dla nich wątki serwera.
   * Metoda jest wołana przez głowną klasę projektu Gomoku, w razie wywołania 
   * z parametrem -s.
   * @param args Parametry wejściowe (nie obsługiwane)
   * @see gomoku.Gomoku
   */
  public static void main(final String[] args) {
     
    Server server = null; 
      
    try {  
      server = new Server();    
    }

    catch (Exception e) {  
      System.out.println(e);
      System.exit(0);  
    }
    
    ServerGUIConsole console = server.gui.getServerConsole();
    console.setMessageLn("Gomoku Server v."+IConf.VERSION_SERVER, Color.GRAY);
    console.setMessageLn("--------------------------------", Color.GRAY);  
   
    server.setServerSocket();
    
    do {
    

      server.restart = false;
      
      try {
        Thread.sleep(150);
      } catch (Exception e) {}
      
      console.setMessageLn("Oczekiwanie na po\u0142\u0105czenia na porcie " + IConf.SERVER_PORT + " ...", Color.DARK_GRAY); 
      
      int clients = 0;
      
      
      while (clients<2)  {
          
        try {  
       
          Socket socket = server.getServerSocket().accept();
          console.setMessageLn("Zaakceptowano po\u0142\u0105czenie z " + socket.getInetAddress(), Color.BLUE);
     
          server.setInputStream(clients, new ObjectInputStream(socket.getInputStream()));
          server.setOutputStream(clients, new ObjectOutputStream(socket.getOutputStream()));
          // dodanie wątku na listę
          server.addNewSocket(socket);
          
          clients++;
          
        }
        
        catch (IOException e) {
        
           server.restart = true;
           break;
            
        }
     
        
        if (clients==2) {
            
           server.startServerThreads();
                 
           console.setMessageLn("Jest ju\u017c dw\u00f3ch graczy, bezczynno\u015b\u0107"
                              + " serwera g\u0142\u00f3wnego", Color.BLACK);
           console.newLine();
           
        }
      
      } 
      
      
      //if (server.restart)  continue;
      while (!server.restart) {
    
        try {
          Thread.sleep(10);
        }
        catch (Exception e) {}    

      }
      
    }  while(true);
    
       
  }
  
  
  
 
}
  
  
  
  

  